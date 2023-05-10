package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public File findOneByFileId(Integer fileId) {
        return fileMapper.findOneById(fileId);
    }

    public File findOneByFileName(String filename) {
        return fileMapper.findOneByFileName(filename);
    }

    public List<File> findAllByUserId(Integer userId) {
        return fileMapper.findAllByUserId(userId);
    }

    public int createFile(File file) {
        return fileMapper.insert(new File(null, file.getFileName(), file.getContentType(),
                file.getFileSize(), file.getFileData(), file.getUserId()));
    }

    public void deleteByFileName(String fileName) {
        fileMapper.delete(fileName);
    }
}
