package com.noonsnow.bigfileupload.controller;

import com.alibaba.fastjson.JSONObject;

import com.noonsnow.bigfileupload.mapper.FileBlockMapper;
import com.noonsnow.bigfileupload.mapper.UploadFileMapper;
import com.noonsnow.bigfileupload.pojo.FileBlock;
import com.noonsnow.bigfileupload.pojo.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;

//http://localhost:8081/uploadPage
@Controller
public class FileUploadController {
    @Autowired
    FileBlockMapper blockMapper;
    @Autowired
    UploadFileMapper fileMapper;

    final static String filesDir = "C:/files";
    final static String blocksDir = "C:/bolcks";

    @RequestMapping("uploadPage")
    public String page() {
        return "bigFileUpload";
    }


    @PostMapping("checkFileIsExist")//检查文件是否存在
    @ResponseBody
    public Object checkFileExist(String md5value) {
        JSONObject resp = new JSONObject();
        List<UploadFile> uploadFiles = fileMapper.getByMd5(md5value);//逻辑上只有一个文件，由于不是按主键查找返回的是一个List
        if (uploadFiles.size() == 0)
            resp.put("exist", "0");
        else
            resp.put("exist", 1);
        return resp.toString();
    }


    @PostMapping("checkChunkIsExist")//检查分片是否存在且完整
    @ResponseBody
    public Object checkChunkExist(String md5value, int chunk, int chunkSize) {

        JSONObject resp = new JSONObject();

        //查数据库，分片是否存在
        FileBlock fileBlock = blockMapper.getByMd5AndChunk(md5value, chunk);

        if (fileBlock == null) {//数据库不存在分片记录或者分片不完整，则重传
            resp.put("exist", "0");
            return resp.toString();
        }

        File block = new File(blocksDir + "/" + md5value + "/" + chunk); //前面没通过就不用执行后面的了，性能会好点吧。。
        // System.out.println("blocklen:"+block.length()+"  "+"chunkSize:"+chunkSize);
        if (block.length() != chunkSize) {//分片是否完整
            resp.put("exist", "0");
            return resp.toString();
        }

        resp.put("exist", "1");
        return resp.toString();

    }


    @PostMapping("upload")//接收上传的分片
    @ResponseBody
    public String fileUpload(String md5value, String chunks, int chunk, String id, String name,
                             String type, String lastModifiedDate, int chunkSize, MultipartFile file) {

        //-----------------------保存分片--------------------
        File blockDir = new File(blocksDir + "/" + md5value);
        blockDir.mkdirs();
        File block = new File(blockDir, String.valueOf(chunk));
        try {
            //搞了N多天，WebUploader tmd要重复发送验证通过的分片，导致多插入数据，导致合并文件变大，坑 （其实还是之前我没把MD5和chunk弄成联合主键，不然它重复插入不了的，自作孽啊）
            boolean exist = blockMapper.getByMd5AndChunk(md5value, chunk) == null ? false : true;
            if (!exist) {
                FileBlock fileBlock = new FileBlock(md5value, new Date(), block.getPath(), chunk, name, chunkSize);
                blockMapper.addFileBlock(fileBlock);//增加分片记录
            }
            file.transferTo(block);

        } catch (IOException e) {
            block.delete();
            blockMapper.deleteByMd5AndChunk(md5value, chunk);
        }

        JSONObject jresp = new JSONObject();
        jresp.put("code", 0);
        jresp.put("msg", "上传成功");
        return jresp.toString();
    }


    @RequestMapping("merge")//合并分片
    @ResponseBody
    public Object merge(String md5value, String name) {
        JSONObject resp = new JSONObject();
        //----------防止重复请求合并，如果数据库有记录，则不用合并了------------
        List<UploadFile>uploadFiles=fileMapper.getByMd5(md5value);
        if (uploadFiles.size()!=0){
            resp.put("status","OK");
            return resp.toString();
        }

        File fileDir = new File(filesDir + "/" + md5value);//文件所在目录
        fileDir.mkdirs();
        File file = new File(fileDir, name);
        List<FileBlock> blocks = blockMapper.findByMd5(md5value);//查询出所有分片
        System.out.println("blocks:" + blocks.size());
        //-----------------合并分片-----------------
        BufferedOutputStream bos = null;
        boolean fail = false;//是否异常
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file, true));
            for (FileBlock block : blocks) {
                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(block.getLocation())))) {//为了关闭每个分片的输入流
                    int len;
                    byte[] b = new byte[1024];
                    while ((len = bis.read(b)) != -1)
                        bos.write(b, 0, len);
                } catch (Exception e) {
                    e.printStackTrace();
                    resp.put("status", "FAIL");
                    return resp.toString();
                }
            }

        } catch (IOException e) {
            //--------------异常---------------
            fail = true;
            e.printStackTrace();
            resp.put("status", "FAIL");
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        if (fail) {
            file.delete();//必须删除，合并文件是按追加而不是覆盖的方式写入文件。
            return resp.toString();
        }

        if (checkTotalSize(file,blocks)) {
            //新增文件记录
            fileMapper.addUploadFile(new UploadFile(md5value, new Date(), file.getPath(), name));
            //----------------删除分片及记录----------------
            new File(blocksDir + "/" + md5value).delete();
            blockMapper.deleteByMd5(md5value);
            resp.put("status", "OK");

        }else {
            file.delete();
            resp.put("status","FAIL");
        }
        return resp.toString();
    }

    //-----------验证合并后的文件，就是验证文件大小---------------
    boolean checkTotalSize(File file,List<FileBlock> blocks){
        long total=0;
        for (FileBlock block : blocks) {
            total+=(long)block.getChunkSize();
        }
        System.out.println("total:"+total+"  "+"fileLength:"+file.length());
        return total==file.length();
    }

}
