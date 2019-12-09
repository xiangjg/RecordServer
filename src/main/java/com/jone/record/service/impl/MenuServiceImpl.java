package com.jone.record.service.impl;


import com.jone.record.dao.system.MenuDao;
import com.jone.record.dao.system.RoleDao;
import com.jone.record.entity.system.MenuEntity;
import com.jone.record.entity.system.RoleEntity;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service(value = "menuService")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    public List<MenuEntity> list() throws Exception {
        List<MenuEntity> list = menuDao.findAll();
        Collections.sort(list);
        return list;
    }

    @Override
    public void save(MenuEntity menuEntity) throws Exception {
        menuDao.save(menuEntity);
    }

    @Override
    public List<MenuEntity> userList(UserInfo user) throws Exception {
        List<MenuEntity> data = new ArrayList<>();
        List<MenuEntity> list = list();
        Collections.sort(list);
        RoleEntity role = roleDao.findById(user.getRole().getId()).orElse(null);
        for (MenuEntity m:list
             ) {
            if(role.getRights().testBit(m.getId()))
                data.add(m);
        }
        return data;
    }
}
