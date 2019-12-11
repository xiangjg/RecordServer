package com.jone.record.dao.common;

import com.jone.record.entity.common.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TypeDao extends JpaRepository<TypeEntity, Integer> {

    TypeEntity findByTypeAndRef(Integer type, String ref);
    TypeEntity findByType(Integer type);
    List<TypeEntity> findByRef(String ref);


}
