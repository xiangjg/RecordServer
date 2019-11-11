package com.jone.record.dao.system;

import com.jone.record.entity.system.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemLogDao extends JpaRepository<SystemLog, Long> {

}
