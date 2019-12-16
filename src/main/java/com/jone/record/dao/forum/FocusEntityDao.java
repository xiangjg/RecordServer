package com.jone.record.dao.forum;

import com.jone.record.entity.forum.FocusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;

public interface FocusEntityDao extends JpaSpecificationExecutor<FocusEntity>,JpaRepository<FocusEntity, Integer> {

    Page<FocusEntity> findByEndDtGreaterThanEqualAndStartDtLessThanEqual(Date sTm, Date eTm, PageRequest pageRequest);
}
