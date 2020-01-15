package com.noonsnow.bigfileupload.mapper;



import com.noonsnow.bigfileupload.pojo.FileBlock;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileBlockMapper {
    @Select("select * from file_block where md5 = #{md5} order by chunk asc") //必须按分片编号升序排列
    @Results({
            @Result(id = true, column = "md5"),
            @Result(id = true, column = "chunk"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "file_name", property = "fileName"),
            @Result(column = "chunk_size", property = "chunkSize")})
    List<FileBlock> findByMd5(@Param("md5") String md5);



    @Select("select *  from file_block where md5 = #{md5} and chunk = #{chunk}")
    @Results({
            @Result(id = true, column = "md5"),
            @Result(id = true, column = "chunk"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "file_name", property = "fileName"),
            @Result(column = "chunk_size", property = "chunkSize")})
    FileBlock getByMd5AndChunk(@Param("md5") String md5, @Param("chunk") int chunk);

    @Delete("delete from file_block where md5 = #{md5}")
    void deleteByMd5(@Param("md5") String md5);

    @Insert("insert into file_block (md5,create_time,location,chunk,file_name,chunk_size) values (#{block.md5},#{block.createTime},#{block.location},#{block.chunk},#{block.fileName},#{block.chunkSize})")

    void addFileBlock(@Param("block") FileBlock fileBlock);

    @Delete(" delete from file_block where md5 = #{md5} and chunk=#{chunk}")
    void deleteByMd5AndChunk(@Param("md5") String md5, @Param("chunk") int chunk);

}
