package com.jone.record.dao.special;

import com.jone.record.entity.special.SubjectsNodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SubjectsNodesDao extends JpaRepository<SubjectsNodes, Integer> {

    List<SubjectsNodes> findByStateAndSidOrderByOrder(Integer state, Integer sid);
    List<SubjectsNodes> findBySidOrderByOrder(Integer sid);
    @Transactional
    @Modifying
    @Query("update SubjectsNodes r set r.state = :state where r.id = :id ")
    Integer updateState(@Param("id") Integer id, @Param("state") Integer state);
}
