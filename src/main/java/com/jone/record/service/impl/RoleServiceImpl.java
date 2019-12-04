package com.jone.record.service.impl;

import com.jone.record.dao.system.MenuDao;
import com.jone.record.dao.system.RoleDao;
import com.jone.record.entity.system.MenuEntity;
import com.jone.record.entity.system.RoleEntity;
import com.jone.record.service.RoleService;
import com.jone.record.util.BigIntegerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;

    @Override
    public void save(RoleEntity role) throws Exception {
        roleDao.save(role);
    }

    @Override
    public void updateRights(Integer roleId, List<MenuEntity> rights) throws Exception {
        BigInteger newRights = getRights(rights);
        roleDao.updateRights(roleId, newRights);
    }

    @Override
    public RoleEntity getRoleMenuInfo(RoleEntity role, String type) throws Exception {
        List<MenuEntity> mis = menuDao.findAll();
        role.setMenus(mis);
        for (MenuEntity mi : role.getMenus()
        ) {
            BigInteger val = null;
            switch (type) {
                case "menu":
                    if(role.getRights()!=null)
                        val = role.getRights();
                    break;
                case "query":
                    if(role.getQuery()!=null)
                        val = role.getQuery();
                    break;
                case "add":
                    if(role.getAdd()!=null)
                        val = role.getAdd();
                    break;
                case "change":
                    if(role.getChange()!=null)
                        val = role.getChange();
                    break;
                case "del":
                    if(role.getDel()!=null)
                        val = role.getDel();
                    break;
                default:
                    break;
            }
            if (val != null && val.testBit(mi.getId()))
                mi.setHave(true);
            else
                mi.setHave(false);
        }
        return role;
    }

    private BigInteger getRights(List<MenuEntity> rights) {
        List<Integer> list = new ArrayList<>();
        for (MenuEntity mi : rights
        ) {
            if (mi.getHave())
                list.add(mi.getId());
        }
        return BigIntegerUtils.sumRights(list);
    }

    @Override
    public List<RoleEntity> getAllRole() throws Exception {
        return roleDao.findAll();
    }

    @Override
    public RoleEntity getRoleById(Integer roleId) {
        return roleDao.findById(roleId).orElse(null);
    }

    @Override
    public Map<String, Object> getMenuList(Integer roleId, String type) throws Exception {
        Map<String, Object> ret = new HashMap<>();
        RoleEntity role = getRoleById(roleId);
        List<MenuEntity> menuEntityList = menuDao.findAll();
        List<Integer> checkList = new ArrayList<>();
        Collections.sort(menuEntityList);
        List<Integer> pidList = menuDao.findPids();
        for (MenuEntity menu : menuEntityList
        ) {
            if (role.getRights().testBit(menu.getId())) {
                if (!pidList.contains(menu.getId()))
                    checkList.add(menu.getId());
                menu.setHave(true);
            } else
                menu.setHave(false);
        }

        ret.put("menu", createMenuNode(menuEntityList, 0));
        ret.put("check", checkList);

        return ret;
    }

    private List<MenuEntity> createMenuNode(List<MenuEntity> list, int fid) {
        List<MenuEntity> resultList = new ArrayList<>();
        if (list == null || list.size() == 0 || fid < 0) {
            return null;
        }
        for (MenuEntity tree : list) {
            if (tree.getPid() == fid) {
                resultList.add(tree);
                tree.setChild(createMenuNode(list, tree.getId()));
            }
        }
        return resultList;
    }
}
