package com.jone.record.service.impl;

import com.jone.record.config.Definition;
import com.jone.record.dao.center.CenterBookmarksDao;
import com.jone.record.dao.center.CenterFavoriteDao;
import com.jone.record.dao.center.CenterMsgDao;
import com.jone.record.entity.center.CenterBookmarks;
import com.jone.record.entity.center.CenterFavorite;
import com.jone.record.entity.center.CenterMsg;
import com.jone.record.entity.vo.PageVo;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.UserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    @Autowired
    private CenterFavoriteDao centerFavoriteDao;
    @Autowired
    private CenterBookmarksDao centerBookmarksDao;

    @Override
    public PageVo<CenterMsg> getMyCenterMsg(Integer userId, Integer state, Integer type, int page, int size) throws Exception {
        Specification specification = new Specification<CenterMsg>() {
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
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "insertDt"));
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
        Page<CenterMsg> pageList = centerMsgDao.findAll(specification, pageRequest);
        PageVo<CenterMsg> pageVo = new PageVo<>();
        pageVo.setPage(page);
        pageVo.setSize(size);
        pageVo.setTotal(pageList.getTotalElements());
        pageVo.setTotalPages(pageList.getTotalPages());
        pageVo.setData(pageList.getContent());
        return pageVo;
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
        if(!userInfo.getUserId().toString().equals(centerMsg.getToId()))
            throw new Exception("不能删除给别人的信息");
        centerMsg.setState(Definition.TYPE_MSG_DELETE);
        centerMsgDao.save(centerMsg);
    }

    @Override
    public List<CenterMsg> listNotDel(String userId) throws Exception {
        return centerMsgDao.findByToIdAndStateNot(userId, Definition.TYPE_MSG_DELETE);
    }

    @Override
    public PageVo<CenterFavorite> listByState(Integer state, int page, int size, UserInfo userInfo) throws Exception {
        Specification specification = new Specification<CenterFavorite>() {
            @Override
            public Predicate toPredicate(Root<CenterFavorite> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("uid"), userInfo.getUserId()));
                if (state != null)
                    predicates.add(criteriaBuilder.equal(root.get("state"), state));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "insertDt"));
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
        Page<CenterFavorite> pageList = centerFavoriteDao.findAll(specification, pageRequest);
        PageVo<CenterFavorite> pageVo = new PageVo<>();
        pageVo.setPage(page);
        pageVo.setSize(size);
        pageVo.setTotal(pageList.getTotalElements());
        pageVo.setTotalPages(pageList.getTotalPages());
        pageVo.setData(pageList.getContent());
        return pageVo;
    }
    @Transactional
    @Override
    public CenterFavorite saveCenterFavorite(CenterFavorite centerFavorite, UserInfo userInfo) throws Exception {
        centerFavorite.setUid(userInfo.getUserId().toString());
        centerFavorite.setInsertDt(new Date());
        centerFavorite.setState(1);
        return centerFavoriteDao.save(centerFavorite);
    }
    @Transactional
    @Override
    public void deleteCenterFavorite(Integer id, UserInfo userInfo) throws Exception {
        CenterFavorite centerFavorite = centerFavoriteDao.findById(id).orElse(null);
        if(centerFavorite==null)
            throw new Exception("不存在此收藏ID");
        if(!userInfo.getUserId().toString().equals(centerFavorite.getUid()))
            throw new Exception("不能删除给别人的收藏");
        centerFavorite.setState(Definition.TYPE_MSG_DELETE);
        centerFavoriteDao.save(centerFavorite);
    }

    @Override
    public PageVo<CenterBookmarks> listCenterBookmarks(Integer state, Integer type, int page, int size, UserInfo userInfo) throws Exception {
        Specification specification = new Specification<CenterBookmarks>() {
            @Override
            public Predicate toPredicate(Root<CenterBookmarks> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("uid"), userInfo.getUserId()));
                if (state != null)
                    predicates.add(criteriaBuilder.equal(root.get("state"), state));
                if (type != null)
                    predicates.add(criteriaBuilder.equal(root.get("type"), type));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "insertDt"));
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
        Page<CenterBookmarks> pageList = centerBookmarksDao.findAll(specification, pageRequest);
        PageVo<CenterBookmarks> pageVo = new PageVo<>();
        pageVo.setPage(page);
        pageVo.setSize(size);
        pageVo.setTotal(pageList.getTotalElements());
        pageVo.setTotalPages(pageList.getTotalPages());
        pageVo.setData(pageList.getContent());
        return pageVo;
    }
    @Transactional
    @Override
    public CenterBookmarks saveCenterBookmarks(CenterBookmarks centerBookmarks, UserInfo userInfo) throws Exception {
        centerBookmarks.setUid(userInfo.getUserId().toString());
        centerBookmarks.setInsertDt(new Date());
        centerBookmarks.setState(1);
        return centerBookmarksDao.save(centerBookmarks);
    }
    @Transactional
    @Override
    public void deleteCenterBookmarks(Integer id, UserInfo userInfo) throws Exception {
        CenterBookmarks centerBookmarks = centerBookmarksDao.findById(id).orElse(null);
        if(centerBookmarks==null)
            throw new Exception("不存在此书签ID");
        if(!userInfo.getUserId().toString().equals(centerBookmarks.getUid()))
            throw new Exception("不能删除给别人的书签");
        centerBookmarks.setState(Definition.TYPE_MSG_DELETE);
        centerBookmarksDao.save(centerBookmarks);
    }
}
