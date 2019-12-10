package com.jone.record.service;

import com.jone.record.entity.system.MenuEntity;
import com.jone.record.entity.vo.UserInfo;

import java.util.List;
import java.util.Map;

public interface MenuService {

    List<MenuEntity> list()throws Exception;
    void save(MenuEntity menuEntity)throws Exception;
    List<MenuEntity> userList(UserInfo user)throws Exception;
    List<MenuEntity> findByLevel(Integer level)throws Exception;
    List<Map<String, Object>> listLevel()throws Exception;
}
