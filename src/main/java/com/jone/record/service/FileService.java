package com.jone.record.service;

import com.jone.record.entity.file.FileEntity;
import com.jone.record.entity.vo.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    List<FileEntity> upload(List<MultipartFile> files, Integer type, Integer refId, UserInfo userInfo) throws Exception;

    List<FileEntity> upload(List<MultipartFile> files, Integer type, List<Integer> ids, UserInfo userInfo) throws Exception;

    FileEntity getFile(Integer id) throws Exception;

    void deleteFile(Integer id) throws Exception;

    List<FileEntity> listByRefIdAndType(Integer id, Integer type) throws Exception;
}
