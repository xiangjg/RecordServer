package com.jone.record.service;

import com.jone.record.entity.map.PoisEntity;
import com.jone.record.entity.map.ShareEntity;
import com.jone.record.entity.special.NodeContent;

import java.util.List;

public interface MapService {

    List<PoisEntity> listPoisByState(Integer state) throws Exception;

    PoisEntity save(PoisEntity poisEntity) throws Exception;

    void deletePoisEntity(Integer id) throws Exception;

    List<ShareEntity> listShareByState(Integer state) throws Exception;

    ShareEntity save(ShareEntity shareEntity) throws Exception;

    void deleteShareEntity(Integer id) throws Exception;
}
