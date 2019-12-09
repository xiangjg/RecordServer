package com.jone.record.service;

import com.jone.record.entity.system.MenuEntity;
import com.jone.record.entity.vo.UserInfo;

import java.util.List;

public interface MenuService {

    List<MenuEntity> list()throws Exception;
    void save(MenuEntity menuEntity)throws Exception;
    List<MenuEntity> userList(UserInfo user)throws Exception;
}
