package com.jone.record.service.impl;

import com.jone.record.config.Definition;
import com.jone.record.dao.file.FileDao;
import com.jone.record.dao.special.NodeContentDao;
import com.jone.record.dao.special.SubjectsNodesDao;
import com.jone.record.dao.special.TQztSubjectsDao;
import com.jone.record.entity.file.FileEntity;
import com.jone.record.entity.special.NodeContent;
import com.jone.record.entity.special.SubjectsNodes;
import com.jone.record.entity.special.TQztSubjectsEntity;
import com.jone.record.entity.vo.PageVo;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.FileService;
import com.jone.record.service.SpecialBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private FileDao fileDao;

    @Override
    public PageVo<TQztSubjectsEntity> listByState(Integer state, Integer page, Integer size) throws Exception {
        PageVo pageData = new PageVo();
        if(page == null)
            page = 1;
        if(size == null)
            size = 10;
        Specification specification = new Specification<TQztSubjectsEntity>() {
            @Override
            public Predicate toPredicate(Root<TQztSubjectsEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (state >= 0)
                    predicates.add(criteriaBuilder.equal(root.get("state"), state));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "num"));
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
        Page<TQztSubjectsEntity> pageList = null;
        if (state >= 0)
            pageList = tQztSubjectsDao.findAll(specification, pageRequest);
        else
            pageList = tQztSubjectsDao.findAll(pageRequest);
        pageData.setPage(page);
        pageData.setSize(size);
        pageData.setTotal(pageList.getTotalElements());
        pageData.setTotalPages(pageList.getTotalPages());

        List<TQztSubjectsEntity> subjectsEntityList = pageList.getContent();
        if (subjectsEntityList != null && subjectsEntityList.size() > 0) {
            for (TQztSubjectsEntity s : subjectsEntityList
            ) {
                List<FileEntity> fileList = fileService.listByRefIdAndType(s.getId(), Definition.TYPE_FILE_SPECIAL);
                if (fileList != null && fileList.size() > 0)
                    s.setFiles(fileList);
                List<SubjectsNodes> nodeList = new ArrayList<>();
                if (state >= 0)
                    nodeList = subjectsNodesDao.findByStateAndSidOrderByOrder(state, s.getId());
                 else
                    nodeList = subjectsNodesDao.findBySidOrderByOrder(s.getId());
                 List<Integer> nids = new ArrayList<>();
                for (SubjectsNodes n:nodeList
                     ) {
                    nids.add(n.getId());
                }
                List<NodeContent> contentAllList = new ArrayList<>();
                if (state >= 0)
                    contentAllList = nodeContentDao.findByStateAndNidIn(state, nids);
                else
                    contentAllList = nodeContentDao.findByNidIn(nids);
                List<FileEntity> fileNodeAllList = fileDao.findByRefIdInAndFileType(nids, Definition.TYPE_FILE_COLUMN);
                for (SubjectsNodes node:nodeList
                     ) {
                    List<FileEntity> fileNodeList = getFileListByNid(node.getId(), fileNodeAllList);
                    if (fileNodeList != null && fileNodeList.size() > 0)
                        node.setFiles(fileNodeList);

                    List<NodeContent> contentList = getContentListByNid(node.getId(), contentAllList);
                    node.setListContent(contentList);
                }
                s.setListNode(nodeList);
            }
        }
        pageData.setData(subjectsEntityList);
        return pageData;
    }
    private List<FileEntity> getFileListByNid(Integer nid, List<FileEntity> allList){
        List<FileEntity> list = new ArrayList<>();
        for (FileEntity n:allList
        ) {
            if(n.getRefId().equals(nid))
                list.add(n);
        }
        return list;
    }

    private List<NodeContent> getContentListByNid(Integer nid, List<NodeContent> allList){
        List<NodeContent> list = new ArrayList<>();
        for (NodeContent n:allList
             ) {
            if(n.getNid().equals(nid))
                list.add(n);
        }
        return list;
    }

    @Transactional
    @Override
    public TQztSubjectsEntity save(TQztSubjectsEntity subjectsEntity, UserInfo user) throws Exception {
        if (subjectsEntity.getCreator() == null)
            subjectsEntity.setCreator(user.getUserName());
        if (subjectsEntity.getCreateDate() == null)
            subjectsEntity.setCreateDate(new Date());
        subjectsEntity.setState(Definition.TYPE_STATE_VALID);
        subjectsEntity = tQztSubjectsDao.save(subjectsEntity);
        List<SubjectsNodes> nodes = subjectsEntity.getListNode();
        if (nodes != null && nodes.size() > 0) {
            for (SubjectsNodes node : nodes
            ) {
                List<NodeContent> contents = node.getListContent();
                node.setSid(subjectsEntity.getId());
                node.setState(Definition.TYPE_STATE_VALID);
                node = subjectsNodesDao.save(node);
                if (contents != null && contents.size() > 0) {
                    for (NodeContent content : contents
                    ) {
                        content.setNid(node.getId());
                        content.setState(Definition.TYPE_STATE_VALID);
                        nodeContentDao.save(content);
                    }
                }
            }
        }
        return subjectsEntity;
    }
    @Transactional
    @Override
    public void delete(Integer id) throws Exception {
        tQztSubjectsDao.updateState(id, Definition.TYPE_STATE_DELETE);
    }

    @Override
    public List<SubjectsNodes> listByStateAndSid(Integer state, Integer sid) throws Exception {
        List<SubjectsNodes> list = new ArrayList<>();
        if (state >= 0)
            list = subjectsNodesDao.findByStateAndSidOrderByOrder(state, sid);
        else
            list = subjectsNodesDao.findBySidOrderByOrder(sid);

        List<Integer> nids = new ArrayList<>();
        for (SubjectsNodes n:list
        ) {
            nids.add(n.getId());
        }
        List<NodeContent> contentAllList = new ArrayList<>();
        if (state >= 0)
            contentAllList = nodeContentDao.findByStateAndNidIn(state, nids);
        else
            contentAllList = nodeContentDao.findByNidIn(nids);
        List<FileEntity> fileNodeAllList = fileDao.findByRefIdInAndFileType(nids, Definition.TYPE_FILE_COLUMN);
        for (SubjectsNodes node:list
        ) {
            List<FileEntity> fileNodeList = getFileListByNid(node.getId(), fileNodeAllList);
            if (fileNodeList != null && fileNodeList.size() > 0)
                node.setFiles(fileNodeList);

            List<NodeContent> contentList = getContentListByNid(node.getId(), contentAllList);
            node.setListContent(contentList);
        }
        return list;
    }
    @Transactional
    @Override
    public SubjectsNodes save(SubjectsNodes subjectsNodes) throws Exception {
        subjectsNodes = subjectsNodesDao.save(subjectsNodes);
        List<NodeContent> contents = subjectsNodes.getListContent();
        if (contents != null && contents.size() > 0) {
            for (NodeContent content : contents
            ) {
                content.setNid(subjectsNodes.getId());
                content.setState(Definition.TYPE_STATE_VALID);
                nodeContentDao.save(content);
            }
        }
        return subjectsNodes;
    }
    @Transactional
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
    @Transactional
    @Override
    public NodeContent save(NodeContent nodeContent) throws Exception {
        return nodeContentDao.save(nodeContent);
    }
    @Transactional
    @Override
    public void deleteNodeContent(Integer id) throws Exception {
        nodeContentDao.updateState(id, Definition.TYPE_STATE_DELETE);
    }
}
