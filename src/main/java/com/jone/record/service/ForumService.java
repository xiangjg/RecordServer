package com.jone.record.service;

import com.jone.record.entity.forum.CourseCategory;
import com.jone.record.entity.forum.CoursesEntity;
import com.jone.record.entity.forum.EpisodesEntity;
import com.jone.record.entity.forum.FocusEntity;
import com.jone.record.entity.vo.PageVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ForumService {
    List<CourseCategory> listCourseCategory() throws Exception;

    CourseCategory save(CourseCategory courseCategory) throws Exception;

    void deleteCourseCategory(Integer id) throws Exception;

    PageVo<CoursesEntity> listCourse(Integer state, Integer page, Integer size) throws Exception;

    CoursesEntity save(CoursesEntity coursesEntity) throws Exception;

    void deleteCourse(Integer id) throws Exception;

    List<EpisodesEntity> listEpisodesEntity(Integer courseId, Integer state) throws Exception;

    EpisodesEntity save(EpisodesEntity episodesEntity) throws Exception;

    void deleteEpisodesEntity(Integer id) throws Exception;

    void updatePlayCount(Integer id, Integer courseId) throws Exception;

    PageVo<FocusEntity> listFocus(Map<String, Object> paramm)throws Exception;

    FocusEntity save(FocusEntity focus)throws Exception;

    void deleteFocus(Integer id)throws Exception;
}
