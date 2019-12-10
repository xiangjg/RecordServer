package com.jone.record.dao.system;

import com.jone.record.entity.system.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuDao extends JpaRepository<MenuEntity, Integer> {

    @Query(value = "select distinct m.pid from MenuEntity m")
    List<Integer> findPids();

    List<MenuEntity> findByLevel(Integer level);

    @Query(value = "select distinct m.level from MenuEntity m")
    List<Integer> findLevels();
}
