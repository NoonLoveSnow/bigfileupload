package com.noonsnow.bigfileupload.pojo;

import java.util.Date;

public class FileBlock {

    private String md5;
    private Date createTime;
    private String location;
    private int chunk;
    private String fileName;
    private int chunkSize;

    public FileBlock(String md5, Date createTime, String location, int chunk, String fileName,int chunkSize) {
        this.md5 = md5;
        this.createTime = createTime;
        this.location = location;
        this.chunk = chunk;
        this.fileName = fileName;
        this.chunkSize=chunkSize;
    }

    public FileBlock() {
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getChunk() {
        return chunk;
    }

    public void setChunk(int chunk) {
        this.chunk = chunk;
    }



    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
