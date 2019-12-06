package com.jone.record.service;

import com.jone.record.entity.system.MenuEntity;

import java.util.List;

public interface MenuService {

    List<MenuEntity> list()throws Exception;
    void save(MenuEntity menuEntity)throws Exception;
}
