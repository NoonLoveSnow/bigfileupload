package com.noonsnow.bigfileupload.pojo;

import java.util.Date;

public class UploadFile {
    private int id;
    private String md5;
    private Date createTime;
    private String location;
    private String fileName;

    public UploadFile(String md5, Date createTime, String location, String fileName) {
        this.md5 = md5;
        this.createTime = createTime;
        this.location = location;
        this.fileName = fileName;
    }

    public UploadFile() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
