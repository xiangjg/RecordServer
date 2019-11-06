package com.jone.record.dao.forum;

import com.jone.record.entity.forum.CoursesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoursesEntityDao extends JpaRepository<CoursesEntity, Integer> {
    List<CoursesEntity> findByState(Integer state);
}
