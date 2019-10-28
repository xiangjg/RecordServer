package com.jone.record.service;

import com.jone.record.entity.common.TypeEntity;

import java.util.List;

public interface TypeService {

    TypeEntity save(TypeEntity typeEntity) throws Exception;

    TypeEntity findByType(Integer type, String ref) throws Exception;

    void deleteById(Integer id) throws Exception;

    List<TypeEntity> listAll() throws Exception;
}
