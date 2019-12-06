package com.jone.record.service;

import com.jone.record.entity.system.MenuEntity;
import com.jone.record.entity.system.RoleEntity;

import java.util.List;
import java.util.Map;

public interface RoleService {

    void save(RoleEntity role) throws Exception;

    void updateRights(Integer roleId, String type, List<Integer> rights) throws Exception;

    List<RoleEntity> getAllRole()throws Exception;

    RoleEntity getRoleById(Integer roleId);

    Map<String, Object> getMenuList(Integer roleId, String type)throws Exception;
}
