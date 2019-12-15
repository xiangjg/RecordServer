package com.jone.record.dao.forum;

import com.jone.record.entity.forum.CoursesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CoursesEntityDao extends JpaSpecificationExecutor<CoursesEntity>,JpaRepository<CoursesEntity, Integer> {
    List<CoursesEntity> findByState(Integer state);
}
