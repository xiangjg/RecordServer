package com.jone.record.service.impl;


import com.jone.record.dao.forum.CourseCategoryDao;
import com.jone.record.dao.forum.CoursesEntityDao;
import com.jone.record.dao.forum.EpisodesEntityDao;
import com.jone.record.entity.forum.CourseCategory;
import com.jone.record.entity.forum.CoursesEntity;
import com.jone.record.entity.forum.EpisodesEntity;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "forumService")
public class ForumServiceImpl implements ForumService {

    @Autowired
    private CourseCategoryDao courseCategoryDao;
    @Autowired
    private CoursesEntityDao coursesEntityDao;
    @Autowired
    private EpisodesEntityDao episodesEntityDao;

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
        for (CoursesEntity c:list
             ) {
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

    @Override
    public CoursesEntity save(CoursesEntity coursesEntity) throws Exception {
        coursesEntity = coursesEntityDao.save(coursesEntity);
        List<EpisodesEntity> entityList = coursesEntity.getEpisodesList();
        if(entityList!=null&&entityList.size()>0){
            for (EpisodesEntity e:entityList
                 ) {
                e.setCourseId(coursesEntity.getId());
                e.setUpdateDt(new Date());
            }
            episodesEntityDao.saveAll(entityList);
        }
        return coursesEntity;
    }

    @Override
    public void deleteCourse(Integer id) throws Exception {
        coursesEntityDao.deleteById(id);
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
        return episodesEntityDao.save(episodesEntity);
    }

    @Override
    public void deleteEpisodesEntity(Integer id) throws Exception {
        episodesEntityDao.deleteById(id);
    }

    @Override
    public void updatePlayCount(Integer id, Integer courseId) throws Exception {
        episodesEntityDao.updatePlay(id, courseId);
    }
}
