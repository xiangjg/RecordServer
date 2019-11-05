package com.jone.record.service.impl;

import com.jone.record.config.Definition;
import com.jone.record.dao.special.NodeContentDao;
import com.jone.record.dao.special.SubjectsNodesDao;
import com.jone.record.dao.special.TQztSubjectsDao;
import com.jone.record.entity.special.NodeContent;
import com.jone.record.entity.special.SubjectsNodes;
import com.jone.record.entity.special.TQztSubjectsEntity;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.FileService;
import com.jone.record.service.SpecialBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service(value = "specialBaseService")
public class SpecialBaseServiceImpl implements SpecialBaseService {

    @Autowired
    private TQztSubjectsDao tQztSubjectsDao;
    @Autowired
    private SubjectsNodesDao subjectsNodesDao;
    @Autowired
    private FileService fileService;
    @Autowired
    private NodeContentDao nodeContentDao;

    @Override
    public List<TQztSubjectsEntity> listByState(Short state) throws Exception {
        List<TQztSubjectsEntity> subjectsEntityList = tQztSubjectsDao.findByStateOrderByNumAsc(state);
        for (TQztSubjectsEntity s:subjectsEntityList
             ) {
            s.setFiles(fileService.listByRefIdAndType(s.getId(),Definition.TYPE_FILE_SPECIAL));
        }
        return subjectsEntityList;
    }

    @Override
    public TQztSubjectsEntity save(TQztSubjectsEntity subjectsEntity, List<MultipartFile> files, UserInfo user) throws Exception {
        subjectsEntity = tQztSubjectsDao.save(subjectsEntity);
        if(files!=null&&files.size()>0){
            fileService.upload(files, Definition.TYPE_FILE_SPECIAL, user);
        }
        return subjectsEntity;
    }

    @Override
    public void delete(Integer id) throws Exception {
        tQztSubjectsDao.deleteById(id);
    }

    @Override
    public List<SubjectsNodes> listByStateAndSid(Integer state, Integer sid) throws Exception {
        return subjectsNodesDao.findByStateAndSidOrderByOrder(state, sid);
    }

    @Override
    public SubjectsNodes save(SubjectsNodes subjectsNodes) throws Exception {
        return subjectsNodesDao.save(subjectsNodes);
    }

    @Override
    public void deleteSubjectsNodes(Integer id) throws Exception {
        subjectsNodesDao.deleteById(id);
    }

    @Override
    public List<NodeContent> listByStateAndNid(Integer state, Integer nid) throws Exception {
        return nodeContentDao.findByStateAndNid(state, nid);
    }

    @Override
    public NodeContent save(NodeContent nodeContent) throws Exception {
        return nodeContentDao.save(nodeContent);
    }

    @Override
    public void deleteNodeContent(Integer id) throws Exception {
        nodeContentDao.deleteById(id);
    }
}
