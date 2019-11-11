package com.jone.record.service.impl;

import com.jone.record.dao.system.SystemLogDao;
import com.jone.record.entity.system.SystemLog;
import com.jone.record.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service(value = "systemLogService")
public class SystemLogServiceImpl implements SystemLogService {

    @Autowired
    private SystemLogDao systemLogDao;

    @Transactional
    @Override
    public void save(SystemLog systemLog) throws Exception {
        systemLogDao.save(systemLog);
    }
}
