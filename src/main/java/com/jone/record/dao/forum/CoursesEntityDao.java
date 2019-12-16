package com.jone.record.dao.forum;

import com.jone.record.entity.forum.CoursesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoursesEntityDao extends JpaSpecificationExecutor<CoursesEntity>,JpaRepository<CoursesEntity, Integer> {
    List<CoursesEntity> findByState(Integer state);
    @Query("update CoursesEntity e set e.playCnt = e.playCnt + 1 where e.id = :id ")
    void updatePlay(@Param("id") Integer id);
}
