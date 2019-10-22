package com.jone.record.dao.system;

import com.jone.record.entity.system.DepartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartDao extends JpaRepository<DepartEntity, Integer> {
}
