package com.jone.record.dao.special;

import com.jone.record.entity.special.TQztSubjectsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TQztSubjectsDao extends JpaRepository<TQztSubjectsEntity, Integer> {

    List<TQztSubjectsEntity> findByStateOrderByNumAsc(Short state);
}
