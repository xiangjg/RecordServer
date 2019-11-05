package com.jone.record.dao.special;

import com.jone.record.entity.special.NodeContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NodeContentDao extends JpaRepository<NodeContent, Integer> {

    List<NodeContent> findByStateAndNid(Integer state, Integer nid);
}
