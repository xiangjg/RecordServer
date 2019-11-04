package com.jone.record.service;

import com.jone.record.entity.file.FileEntity;
import com.jone.record.entity.vo.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    List<FileEntity> upload(List<MultipartFile> files, Integer type, UserInfo userInfo) throws Exception;

    FileEntity getFile(Integer id) throws Exception;

    void deleteFile(Integer id) throws Exception;

    List<FileEntity> listBuRefId(Integer id) throws Exception;
}
