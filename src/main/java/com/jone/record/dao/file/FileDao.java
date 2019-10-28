package com.jone.record.dao.file;

import com.jone.record.entity.file.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDao extends JpaRepository<FileEntity, Integer> {
}
