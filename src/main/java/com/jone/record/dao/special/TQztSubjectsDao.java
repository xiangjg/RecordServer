package com.jone.record.dao.special;

import com.jone.record.entity.special.TQztSubjectsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

public interface TQztSubjectsDao extends JpaRepository<TQztSubjectsEntity, Integer> {

    List<TQztSubjectsEntity> findByStateOrderByNumAsc(Integer state);
    @Transactional
    @Modifying
    @Query("update TQztSubjectsEntity r set r.state = :state where r.id = :id ")
    Integer updateState(@Param("id") Integer id, @Param("state") Integer state);
}
