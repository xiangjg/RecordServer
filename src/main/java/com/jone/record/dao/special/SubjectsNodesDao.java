package com.jone.record.dao.special;

import com.jone.record.entity.special.SubjectsNodes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectsNodesDao extends JpaRepository<SubjectsNodes, Integer> {

    List<SubjectsNodes> findByStateAndSidOrderByOrder(Integer state, Integer sid);
}
