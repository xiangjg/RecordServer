package com.jone.record.dao.forum;

import com.jone.record.entity.forum.EpisodesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface EpisodesEntityDao extends JpaRepository<EpisodesEntity, Integer> {
    List<EpisodesEntity> findByCourseIdAndState(Integer courseId, Integer state);

    @Transactional
    @Modifying
    @Query("update EpisodesEntity e set e.playCnt = e.playCnt + 1 where e.id = :id and e.courseId = :courseId")
    void updatePlay(@Param("id") Integer id, @Param("courseId") Integer courseId);
}
