package com.jone.record.service;

import com.jone.record.entity.special.SubjectsNodes;
import com.jone.record.entity.special.TQztSubjectsEntity;
import com.jone.record.entity.vo.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SpecialBaseService {

    List<TQztSubjectsEntity> listByState(Short state) throws Exception;

    TQztSubjectsEntity save(TQztSubjectsEntity subjectsEntity,List<MultipartFile> files, UserInfo user) throws Exception;

    void delete(Integer id) throws Exception;

    List<SubjectsNodes> listByStateAndSid(Integer state, Integer sid) throws Exception;

    SubjectsNodes save(SubjectsNodes subjectsNodes) throws Exception;

    void deleteSubjectsNodes(Integer id) throws Exception;
}
