package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File findOneById(Integer fileId);

    @Select("SELECT * FROM FILES WHERE filename = #{fileName}")
    File findOneByFileName(String fileName);

    @Select("SELECT filename FROM FILES WHERE userId = #{userId}")
    List<File> findAllByUserId(Integer userId);

    @Insert("INSERT INTO FILES(filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Delete("DELETE FROM FILES WHERE filename = #{fileName}")
    int delete(String fileName);
}
