package com.jone.record.dao.file;

import com.jone.record.entity.file.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface FileDao extends JpaRepository<FileEntity, Integer> {

    List<FileEntity> findByRefId(Integer refId);
    List<FileEntity> findByRefIdAndFileType(Integer refId, Integer type);
    List<FileEntity> findByRefIdInAndFileType(Collection<Integer> refId, Integer type);

    List<FileEntity> findByIdIn(Collection<Integer> ids);
}
