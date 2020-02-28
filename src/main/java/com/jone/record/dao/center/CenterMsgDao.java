package com.jone.record.dao.center;

import com.jone.record.entity.center.CenterMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CenterMsgDao extends JpaSpecificationExecutor<CenterMsg>,JpaRepository<CenterMsg, Integer> {

    List<CenterMsg> findByToId(String userId);
    List<CenterMsg> findByToIdAndStateNot(String userId, Integer state);
    List<CenterMsg> findByToIdAndState(String userId, Integer state);
}
