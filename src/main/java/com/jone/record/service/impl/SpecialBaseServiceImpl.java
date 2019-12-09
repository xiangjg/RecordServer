package com.jone.record.service.impl;

import com.jone.record.config.Definition;
import com.jone.record.dao.special.NodeContentDao;
import com.jone.record.dao.special.SubjectsNodesDao;
import com.jone.record.dao.special.TQztSubjectsDao;
import com.jone.record.entity.file.FileEntity;
import com.jone.record.entity.special.NodeContent;
import com.jone.record.entity.special.SubjectsNodes;
import com.jone.record.entity.special.TQztSubjectsEntity;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.FileService;
import com.jone.record.service.SpecialBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
    public List<TQztSubjectsEntity> listByState(Integer state) throws Exception {
        List<TQztSubjectsEntity> subjectsEntityList = new ArrayList<>();
        if (state >= 0) {
            subjectsEntityList = tQztSubjectsDao.findByStateOrderByNumAsc(state);
        } else {
            subjectsEntityList = tQztSubjectsDao.findAll();
        }
        if (subjectsEntityList != null && subjectsEntityList.size() > 0) {
            for (TQztSubjectsEntity s : subjectsEntityList
            ) {
                List<FileEntity> fileList = fileService.listByRefIdAndType(s.getId(), Definition.TYPE_FILE_SPECIAL);
                if (fileList != null && fileList.size() > 0)
                    s.setFiles(fileList);
            }
        }
        return subjectsEntityList;
    }
    @Transactional
    @Override
    public TQztSubjectsEntity save(TQztSubjectsEntity subjectsEntity, List<MultipartFile> files, UserInfo user) throws Exception {
        subjectsEntity = tQztSubjectsDao.save(subjectsEntity);
        if (files != null && files.size() > 0) {
            fileService.upload(files, Definition.TYPE_FILE_SPECIAL, user);
        }
        List<SubjectsNodes> nodes = subjectsEntity.getListNode();
        if(nodes!=null&&nodes.size()>0){
            for (SubjectsNodes node:nodes
                 ) {
                List<NodeContent> contents = node.getListContent();
                node.setSid(subjectsEntity.getId());
                node =  subjectsNodesDao.save(node);
                if(contents!=null&&contents.size()>0){
                    for (NodeContent content:contents
                    ) {
                        content.setNid(node.getId());
                        nodeContentDao.save(content);
                    }
                }
            }
        }
        return subjectsEntity;
    }

    @Override
    public void delete(Integer id) throws Exception {
        tQztSubjectsDao.updateState(id, Definition.TYPE_STATE_DELETE);
    }

    @Override
    public List<SubjectsNodes> listByStateAndSid(Integer state, Integer sid) throws Exception {
        if (state >= 0)
            return subjectsNodesDao.findByStateAndSidOrderByOrder(state, sid);
        else
            return subjectsNodesDao.findBySidOrderByOrder(sid);
    }

    @Override
    public SubjectsNodes save(SubjectsNodes subjectsNodes) throws Exception {
        return subjectsNodesDao.save(subjectsNodes);
    }

    @Override
    public void deleteSubjectsNodes(Integer id) throws Exception {
        subjectsNodesDao.updateState(id, Definition.TYPE_STATE_DELETE);
    }

    @Override
    public List<NodeContent> listByStateAndNid(Integer state, Integer nid) throws Exception {
        if (state >= 0)
            return nodeContentDao.findByStateAndNid(state, nid);
        else
            return nodeContentDao.findByNid(nid);
    }

    @Override
    public NodeContent save(NodeContent nodeContent) throws Exception {
        return nodeContentDao.save(nodeContent);
    }

    @Override
    public void deleteNodeContent(Integer id) throws Exception {
        nodeContentDao.updateState(id, Definition.TYPE_STATE_DELETE);
    }
}
