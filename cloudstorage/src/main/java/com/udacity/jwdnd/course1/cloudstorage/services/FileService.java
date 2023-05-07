package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> findAllFiles(){
        return this.fileMapper.findAllFiles();
    }

    public File findFileByName(String filename){
        return this.fileMapper.findFileByName(filename);
    }

    public boolean fileIsAvailable(File file){
        if(this.findFileByName(file.getFilename()) == null){
            return false;
        }
        return true;
    }

    public int insert(File file){
        if(Integer.parseInt(file.getFilesize()) >= 1000000){
            throw new MaxUploadSizeExceededException(Integer.parseInt(file.getFilesize()));
        }
        if(fileIsAvailable(file)) {
            return -1;
        }
        return this.fileMapper.insert(file);
    }

    public void deleteFile(Integer fileId){
        this.fileMapper.deleteFile(fileId);
    }

    public File findFilebyID(Integer fileId){
        return this.fileMapper.findFileByID(fileId);
    }
}
