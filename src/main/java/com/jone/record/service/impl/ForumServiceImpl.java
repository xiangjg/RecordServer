package com.jone.record.service.impl;


import com.jone.record.config.Definition;
import com.jone.record.dao.file.FileDao;
import com.jone.record.dao.forum.CourseCategoryDao;
import com.jone.record.dao.forum.CoursesEntityDao;
import com.jone.record.dao.forum.EpisodesEntityDao;
import com.jone.record.dao.forum.FocusEntityDao;
import com.jone.record.entity.file.FileEntity;
import com.jone.record.entity.forum.CourseCategory;
import com.jone.record.entity.forum.CoursesEntity;
import com.jone.record.entity.forum.EpisodesEntity;
import com.jone.record.entity.forum.FocusEntity;
import com.jone.record.entity.special.SubjectsNodes;
import com.jone.record.entity.vo.PageVo;
import com.jone.record.service.ForumService;
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
import java.util.Map;

@Service(value = "forumService")
public class ForumServiceImpl implements ForumService {

    @Autowired
    private CourseCategoryDao courseCategoryDao;
    @Autowired
    private CoursesEntityDao coursesEntityDao;
    @Autowired
    private EpisodesEntityDao episodesEntityDao;
    @Autowired
    private FocusEntityDao focusEntityDao;
    @Autowired
    private FileDao fileDao;

    @Override
    public List<CourseCategory> listCourseCategory() throws Exception {
        return courseCategoryDao.findAll();
    }

    @Override
    public CourseCategory save(CourseCategory courseCategory) throws Exception {
        courseCategory = courseCategoryDao.save(courseCategory);
        return courseCategory;
    }

    @Override
    public void deleteCourseCategory(Integer id) throws Exception {
        courseCategoryDao.deleteById(id);
    }

    @Override
    public PageVo<CoursesEntity> listCourse(Integer state, Integer page, Integer size) throws Exception {
        PageVo pageData = new PageVo();
        Specification specification = new Specification<CoursesEntity>() {
            @Override
            public Predicate toPredicate(Root<CoursesEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (state >= 0)
                    predicates.add(criteriaBuilder.equal(root.get("state"), state));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "createDt"));
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
        Page<CoursesEntity> pageList = null;
        if (state >= 0)
            pageList = coursesEntityDao.findAll(specification, pageRequest);
        else
            pageList = coursesEntityDao.findAll(pageRequest);
        pageData.setPage(page);
        pageData.setSize(size);
        pageData.setTotal(pageList.getTotalElements());
        pageData.setTotalPages(pageList.getTotalPages());

        List<CoursesEntity> list = pageList.getContent();
        List<Integer> nids = new ArrayList<>();
        for (CoursesEntity n:list
        ) {
            nids.add(n.getId());
        }
        List<FileEntity> fileAllList = fileDao.findByRefIdInAndFileType(nids, Definition.TYPE_FILE_COURSE);
        for (CoursesEntity c:list
             ) {
            List<FileEntity> fileNodeList = getFileListByNid(c.getId(), fileAllList);
            if (fileNodeList != null && fileNodeList.size() > 0)
                c.setFiles(fileNodeList);
            List<EpisodesEntity> entityList = new ArrayList<>();
            if (state >= 0)
                entityList = episodesEntityDao.findByCourseIdAndState(c.getId(), state);
            else
                entityList = episodesEntityDao.findByCourseId(c.getId());
            if(entityList!=null&&entityList.size()>0)
                c.setEpisodesList(entityList);
        }
        pageData.setData(list);
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
    @Override
    public CoursesEntity save(CoursesEntity coursesEntity) throws Exception {
        CourseCategory category = courseCategoryDao.findById(coursesEntity.getCatId()).orElse(null);
        if(category==null)
            throw new Exception("没有该课程分类，请先新增课程分类");

        coursesEntity.setCreateDt(new Date());
        coursesEntity.setPlayCnt(0);
        coursesEntity.setState(Definition.TYPE_STATE_VALID);
        coursesEntity = coursesEntityDao.save(coursesEntity);
        List<EpisodesEntity> entityList = coursesEntity.getEpisodesList();
        if(entityList!=null&&entityList.size()>0){
            for (EpisodesEntity e:entityList
                 ) {
                e.setCourseId(coursesEntity.getId());
                e.setUpdateDt(new Date());
                e.setState(Definition.TYPE_STATE_VALID);
            }
            episodesEntityDao.saveAll(entityList);
        }
        return coursesEntity;
    }

    @Override
    public void deleteCourse(Integer id) throws Exception {
        CoursesEntity episodesEntity = coursesEntityDao.findById(id).orElse(null);
        if(episodesEntity!=null){
            episodesEntity.setState(Definition.TYPE_STATE_DELETE);
            coursesEntityDao.save(episodesEntity);
        }
    }

    @Override
    public List<EpisodesEntity> listEpisodesEntity(Integer courseId, Integer state) throws Exception {
        List<EpisodesEntity> entityList = new ArrayList<>();
        if (state >= 0)
            entityList = episodesEntityDao.findByCourseIdAndState(courseId, state);
        else
            entityList = episodesEntityDao.findByCourseId(courseId);
        return entityList;
    }

    @Override
    public EpisodesEntity save(EpisodesEntity episodesEntity) throws Exception {
        episodesEntity.setUpdateDt(new Date());
        episodesEntity.setPlayCnt(0);
        episodesEntity.setState(Definition.TYPE_STATE_VALID);
        return episodesEntityDao.save(episodesEntity);
    }

    @Override
    public void deleteEpisodesEntity(Integer id) throws Exception {
        EpisodesEntity episodesEntity = episodesEntityDao.findById(id).orElse(null);
        if(episodesEntity!=null){
            episodesEntity.setState(Definition.TYPE_STATE_DELETE);
            episodesEntityDao.save(episodesEntity);
        }
    }

    @Override
    public void updatePlayCount(Integer id, Integer courseId) throws Exception {
        episodesEntityDao.updatePlay(id, courseId);
        coursesEntityDao.updatePlay(id);
    }

    @Override
    public PageVo<FocusEntity> listFocus(Map<String, Object> paramm) throws Exception {
        PageVo pageData = new PageVo();
        Integer page = 1;
        Integer size = 10;
        if(paramm.get("page")!=null)
            page = Integer.parseInt(paramm.get("page").toString());
        if(paramm.get("size")!=null)
            size = Integer.parseInt(paramm.get("size").toString());
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "startDt"));
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
        Page<FocusEntity> pageList = null;
        if (paramm.get("tm")!=null){
            Date tm = (Date)paramm.get("tm");
            pageList = focusEntityDao.findByEndDtGreaterThanEqualAndStartDtLessThanEqual(tm, tm, pageRequest);
        }else
            pageList = focusEntityDao.findAll(pageRequest);
        pageData.setPage(page);
        pageData.setSize(size);
        pageData.setTotal(pageList.getTotalElements());
        pageData.setTotalPages(pageList.getTotalPages());
        pageData.setData(pageList.getContent());
        return pageData;
    }
    @Transactional
    @Override
    public FocusEntity save(FocusEntity focus) throws Exception {
        return focusEntityDao.save(focus);
    }
    @Transactional
    @Override
    public void deleteFocus(Integer id) throws Exception {
        focusEntityDao.deleteById(id);
    }
}
