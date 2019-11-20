package com.jone.record.service.impl;

import com.jone.record.config.Definition;
import com.jone.record.dao.center.CenterMsgDao;
import com.jone.record.entity.center.CenterMsg;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.UserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "userCenterService")
public class UserCenterServiceImpl implements UserCenterService {

    @Autowired
    private CenterMsgDao centerMsgDao;

    @Override
    public List<CenterMsg> getMyCenterMsg(Integer userId, Integer state, Integer type) throws Exception {
        Specification querySpecifi = new Specification<CenterMsg>() {
            @Override
            public Predicate toPredicate(Root<CenterMsg> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (userId != null)
                    predicates.add(criteriaBuilder.equal(root.get("toId"), userId));
                if (state != null)
                    predicates.add(criteriaBuilder.equal(root.get("state"), state));
                if (type != null)
                    predicates.add(criteriaBuilder.equal(root.get("type"), type));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return centerMsgDao.findAll(querySpecifi);
    }

    @Transactional
    @Override
    public CenterMsg saveCenterMsg(CenterMsg centerMsg) throws Exception {
        centerMsg.setState(Definition.TYPE_MSG_NO_READ);
        centerMsg.setInsertDt(new Date());
        return centerMsgDao.save(centerMsg);
    }
    @Transactional
    @Override
    public void readMsg(Integer msgId, UserInfo userInfo) throws Exception {
        CenterMsg centerMsg = centerMsgDao.findById(msgId).orElse(null);
        if(centerMsg==null)
            throw new Exception("不存在此消息ID");
        if(!userInfo.getUserId().equals(centerMsg.getToId()))
            throw new Exception("不能阅读给别人的信息");
        centerMsg.setReadDt(new Date());
        centerMsg.setState(Definition.TYPE_MSG_READ);
        centerMsgDao.save(centerMsg);
    }
    @Transactional
    @Override
    public void deleteCenterMsg(Integer msgId, UserInfo userInfo) throws Exception {
        CenterMsg centerMsg = centerMsgDao.findById(msgId).orElse(null);
        if(centerMsg==null)
            throw new Exception("不存在此消息ID");
        if(!userInfo.getUserId().equals(centerMsg.getToId()))
            throw new Exception("不能删除给别人的信息");
        centerMsg.setState(Definition.TYPE_MSG_DELETE);
        centerMsgDao.save(centerMsg);
    }
}
