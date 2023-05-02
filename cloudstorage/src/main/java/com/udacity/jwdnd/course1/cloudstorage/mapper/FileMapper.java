package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES")
    List<File> findAllFiles();

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    public void deleteFile(Integer fileId);

    @Select("SELECT * FROM FILES WHERE filename=#{filename}")
    public File findFileByName(String filename);

    @Select("SELECT * FROM FILES WHERE fileId=#{fileId}")
    public File findFileByID(Integer fileId);
}
