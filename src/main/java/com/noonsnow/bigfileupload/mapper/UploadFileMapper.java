package com.noonsnow.bigfileupload.mapper;


import com.noonsnow.bigfileupload.pojo.UploadFile;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UploadFileMapper {
    @Select("select  * from file where md5 = #{md5}")
    @Results({@Result(column = "create_time",property = "createTime"),
            @Result(column = "file_name",property = "fileName")})
    List<UploadFile> getByMd5(@Param("md5") String md5);

    @Insert("insert into file (md5,create_time,location,file_name) values (#{file.md5},#{file.createTime},#{file.location},#{file.fileName})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void addUploadFile(@Param("file") UploadFile uploadFile);
}
