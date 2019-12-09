package com.jone.record.dao.special;

import com.jone.record.entity.special.NodeContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NodeContentDao extends JpaRepository<NodeContent, Integer> {

    List<NodeContent> findByStateAndNid(Integer state, Integer nid);
    List<NodeContent> findByNid(Integer nid);
    @Transactional
    @Modifying
    @Query("update NodeContent r set r.state = :state where r.id = :id ")
    Integer updateState(@Param("id") Integer id, @Param("state") Integer state);
}
