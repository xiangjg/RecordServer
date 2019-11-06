package com.jone.record.service.impl;


import com.jone.record.dao.forum.CourseCategoryDao;
import com.jone.record.dao.forum.CoursesEntityDao;
import com.jone.record.dao.forum.EpisodesEntityDao;
import com.jone.record.entity.forum.CourseCategory;
import com.jone.record.entity.forum.CoursesEntity;
import com.jone.record.entity.forum.EpisodesEntity;
import com.jone.record.entity.map.ShareEntity;
import com.jone.record.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return courseCategoryDao.save(courseCategory);
    }

    @Override
    public void deleteCourseCategory(Integer id) throws Exception {
        courseCategoryDao.deleteById(id);
    }

    @Override
    public List<CoursesEntity> listCourse(Integer state) throws Exception {
        return coursesEntityDao.findByState(state);
    }

    @Override
    public CoursesEntity save(CoursesEntity coursesEntity) throws Exception {
        return coursesEntityDao.save(coursesEntity);
    }

    @Override
    public void deleteCourse(Integer id) throws Exception {
        coursesEntityDao.deleteById(id);
    }

    @Override
    public List<EpisodesEntity> listEpisodesEntity(Integer courseId, Integer state) throws Exception {
        return episodesEntityDao.findByCourseIdAndState(courseId, state);
    }

    @Override
    public EpisodesEntity save(EpisodesEntity episodesEntity) throws Exception {
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
