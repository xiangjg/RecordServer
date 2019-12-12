package com.jone.record.service;

import com.jone.record.entity.special.NodeContent;
import com.jone.record.entity.special.SubjectsNodes;
import com.jone.record.entity.special.TQztSubjectsEntity;
import com.jone.record.entity.vo.PageVo;
import com.jone.record.entity.vo.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SpecialBaseService {

    PageVo<TQztSubjectsEntity> listByState(Integer state, Integer page, Integer size) throws Exception;

    TQztSubjectsEntity save(TQztSubjectsEntity subjectsEntity, UserInfo user) throws Exception;

    void delete(Integer id) throws Exception;

    List<SubjectsNodes> listByStateAndSid(Integer state, Integer sid) throws Exception;

    SubjectsNodes save(SubjectsNodes subjectsNodes) throws Exception;

    void deleteSubjectsNodes(Integer id) throws Exception;

    List<NodeContent> listByStateAndNid(Integer state, Integer nid) throws Exception;

    NodeContent save(NodeContent nodeContent) throws Exception;

    void deleteNodeContent(Integer id) throws Exception;
}
