package com.jone.record.service.impl;


import com.jone.record.dao.system.MenuDao;
import com.jone.record.entity.system.MenuEntity;
import com.jone.record.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "menuService")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public List<MenuEntity> list() throws Exception {
        return menuDao.findAll();
    }

    @Override
    public void save(MenuEntity menuEntity) throws Exception {
        menuDao.save(menuEntity);
    }
}
