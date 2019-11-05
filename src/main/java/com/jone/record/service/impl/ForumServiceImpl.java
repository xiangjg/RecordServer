package com.jone.record.service.impl;


import com.jone.record.dao.forum.CourseCategoryDao;
import com.jone.record.entity.forum.CourseCategory;
import com.jone.record.entity.map.ShareEntity;
import com.jone.record.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "forumService")
public class ForumServiceImpl implements ForumService {

    @Autowired
    private CourseCategoryDao courseCategoryDao;

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
}
