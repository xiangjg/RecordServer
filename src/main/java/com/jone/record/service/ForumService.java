package com.jone.record.service;

import com.jone.record.entity.forum.CourseCategory;

import java.util.List;

public interface ForumService {
    List<CourseCategory> listCourseCategory() throws Exception;

    CourseCategory save(CourseCategory courseCategory) throws Exception;

    void deleteCourseCategory(Integer id) throws Exception;
}
