package com.jone.record.dao.system;

import com.jone.record.entity.system.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<RoleEntity, Integer> {
}
