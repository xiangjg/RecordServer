package com.jone.record.dao.center;

import com.jone.record.entity.center.CenterBookmarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CenterBookmarksDao extends JpaSpecificationExecutor<CenterBookmarks>,JpaRepository<CenterBookmarks, Integer> {
}
