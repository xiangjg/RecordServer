package com.jone.record.dao.system;

import com.jone.record.entity.system.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuDao extends JpaRepository<MenuEntity, Integer> {
}
