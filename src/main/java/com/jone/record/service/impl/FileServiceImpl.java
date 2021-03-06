package com.jone.record.service.impl;

import com.jone.record.dao.common.TypeDao;
import com.jone.record.dao.file.FileDao;
import com.jone.record.entity.common.TypeEntity;
import com.jone.record.entity.file.FileEntity;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "fileService")
public class FileServiceImpl implements FileService {

    @Value("${filePath}")
    private String filePath;
    @Autowired
    private FileDao fileDao;
    @Autowired
    private TypeDao typeDao;

    @Override
    public List<FileEntity> upload(List<MultipartFile> files, Integer type, Integer refId, UserInfo userInfo) throws Exception {
        List<FileEntity> list = new ArrayList<>();
        TypeEntity typeEntity = typeDao.findByType(type);
        if (typeEntity == null)
            throw new Exception("没有该类型定义");
        if (files == null || files.size() == 0)
            throw new Exception("没有上传文件");
        Date now = new Date();
        int index = 1;
        for (MultipartFile file : files
        ) {
            byte[] data = file.getBytes();
            if (data.length > 0) {
                FileEntity fileEntity = new FileEntity();
                String name = file.getOriginalFilename();
                String fileName = System.currentTimeMillis() + "_" + index;

                fileEntity.setContentType(file.getContentType());
                fileEntity.setFileName(fileName);
                fileEntity.setName(name);
                fileEntity.setFileType(type);
                fileEntity.setSize(new Double(data.length / 1024));
                fileEntity.setCreateTime(now);
                fileEntity.setUserId(userInfo.getUserId());
                fileEntity.setUserName(userInfo.getUserName());
                fileEntity.setRefId(refId);

                File file1 = new File(filePath + File.separator + type + File.separator + fileName);
                FileUtils.writeByteArrayToFile(file1, data);
                list.add(fileEntity);

                index++;
            }
        }
        fileDao.saveAll(list);
        return list;
    }

    @Override
    public List<FileEntity> upload(List<MultipartFile> files, Integer type,List<Integer> ids, UserInfo userInfo) throws Exception {
        List<FileEntity> list = fileDao.findByIdIn(ids);
        Date now = new Date();
        int index = 1;

        for (int i = 0; i < files.size(); i++
        ) {
            MultipartFile file = files.get(i);
            byte[] data = file.getBytes();
            if (data.length > 0) {
                FileEntity fileEntity = findFileById(ids.get(i), list);

                //删除原文件
                File delFile = new File(filePath + File.separator + fileEntity.getFileType() + File.separator + fileEntity.getFileName());
                if(delFile.exists())
                    delFile.delete();

                String name = file.getOriginalFilename();
                String fileName = System.currentTimeMillis() + "_" + index;

                fileEntity.setContentType(file.getContentType());
                fileEntity.setFileName(fileName);
                fileEntity.setName(name);
                fileEntity.setFileType(type);
                fileEntity.setSize(new Double(data.length / 1024));
                fileEntity.setCreateTime(now);
                fileEntity.setUserId(userInfo.getUserId());
                fileEntity.setUserName(userInfo.getUserName());

                File file1 = new File(filePath + File.separator + type + File.separator + fileName);
                FileUtils.writeByteArrayToFile(file1, data);
                list.add(fileEntity);

                index++;
            }
        }
        fileDao.saveAll(list);
        return list;
    }

    private FileEntity findFileById(Integer id, List<FileEntity> list) {
        FileEntity f = null;
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getId() == id){
                f = list.get(i);
                break;
            }
        }
        return f;
    }

    @Override
    public FileEntity getFile(Integer id) throws Exception {
        FileEntity file = fileDao.findById(id).orElse(null);
        if (file != null) {
            File f = new File(filePath + File.separator + file.getFileType() + File.separator + file.getFileName());
            if (f.exists())
                file.setData(FileUtils.readFileToByteArray(f));
        }
        return file;
    }

    @Transactional
    @Override
    public void deleteFile(Integer id) throws Exception {
        FileEntity fileEntity = fileDao.findById(id).orElse(null);
        if (fileEntity != null) {
            File file = new File(filePath + File.separator + fileEntity.getFileType() + File.separator + fileEntity.getFileName());
            if (file.exists())
                file.delete();
            fileDao.delete(fileEntity);
        }
    }

    @Override
    public List<FileEntity> listByRefIdAndType(Integer id, Integer type) throws Exception {
        return fileDao.findByRefIdAndFileType(id, type);
    }
}
