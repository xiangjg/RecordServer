package com.jone.record.dao.map;

import com.jone.record.entity.map.ShareEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShareEntityDao extends JpaRepository<ShareEntity, Integer> {
    List<ShareEntity> findByState(Integer state);
}
