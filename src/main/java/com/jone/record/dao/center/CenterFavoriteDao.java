package com.jone.record.dao.center;

import com.jone.record.entity.center.CenterFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CenterFavoriteDao extends JpaSpecificationExecutor<CenterFavorite>,JpaRepository<CenterFavorite, Integer> {
}
