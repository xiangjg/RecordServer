package com.jone.record.dao.forum;

import com.jone.record.entity.forum.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseCategoryDao extends JpaRepository<CourseCategory, Integer> {
}
