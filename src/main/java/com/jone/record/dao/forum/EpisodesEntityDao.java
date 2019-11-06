package com.jone.record.dao.forum;

import com.jone.record.entity.forum.EpisodesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodesEntityDao extends JpaRepository<EpisodesEntity, Integer> {
    List<EpisodesEntity> findByCourseIdAndState(Integer courseId, Integer state);
}
