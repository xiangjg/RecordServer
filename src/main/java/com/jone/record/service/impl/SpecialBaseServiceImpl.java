package com.jone.record.service.impl;

import com.jone.record.dao.special.SubjectsNodesDao;
import com.jone.record.dao.special.TQztSubjectsDao;
import com.jone.record.entity.special.SubjectsNodes;
import com.jone.record.entity.special.TQztSubjectsEntity;
import com.jone.record.service.SpecialBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "specialBaseService")
public class SpecialBaseServiceImpl implements SpecialBaseService {

    @Autowired
    private TQztSubjectsDao tQztSubjectsDao;
    @Autowired
    private SubjectsNodesDao subjectsNodesDao;

    @Override
    public List<TQztSubjectsEntity> listByState(Short state) throws Exception {
        return tQztSubjectsDao.findByStateOrderbyOrderAsc(state);
    }

    @Override
    public TQztSubjectsEntity save(TQztSubjectsEntity subjectsEntity) throws Exception {
        return tQztSubjectsDao.save(subjectsEntity);
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
}
