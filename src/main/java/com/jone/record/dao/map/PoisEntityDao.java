package com.jone.record.dao.map;

import com.jone.record.entity.map.PoisEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PoisEntityDao extends JpaRepository<PoisEntity, Integer> {
    List<PoisEntity> findByState(Integer state);
}
