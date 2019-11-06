package com.jone.record.service;

import com.jone.record.entity.forum.CourseCategory;
import com.jone.record.entity.forum.CoursesEntity;
import com.jone.record.entity.forum.EpisodesEntity;

import java.util.List;

public interface ForumService {
    List<CourseCategory> listCourseCategory() throws Exception;

    CourseCategory save(CourseCategory courseCategory) throws Exception;

    void deleteCourseCategory(Integer id) throws Exception;

    List<CoursesEntity> listCourse(Integer state) throws Exception;

    CoursesEntity save(CoursesEntity coursesEntity) throws Exception;

    void deleteCourse(Integer id) throws Exception;

    List<EpisodesEntity> listEpisodesEntity(Integer courseId, Integer state) throws Exception;

    EpisodesEntity save(EpisodesEntity episodesEntity) throws Exception;

    void deleteEpisodesEntity(Integer id) throws Exception;

    void updatePlayCount(Integer id, Integer courseId) throws Exception;
}
