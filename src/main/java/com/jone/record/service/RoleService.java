package com.jone.record.service;

import com.jone.record.entity.system.MenuEntity;
import com.jone.record.entity.system.RoleEntity;

import java.util.List;

public interface RoleService {

    void save(RoleEntity role) throws Exception;

    void updateRights(Integer roleId, List<MenuEntity> rights) throws Exception;

    /**
     *
     * @param role
     * @param type menu query add change del
     * @return
     * @throws Exception
     */
    RoleEntity getRoleMenuInfo(RoleEntity role, String type) throws Exception;
}
