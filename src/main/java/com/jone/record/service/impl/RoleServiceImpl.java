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
        if(role.getId()!=null){
            RoleEntity r = roleDao.findById(role.getId()).orElse(null);
            if(r!=null){
                r.setDesc(role.getDesc());
                if(role.getId()!=1)
                    r.setName(role.getName());
                roleDao.save(r);
            }
        }else{
            roleDao.save(role);
        }
    }

    @Override
    public void updateRights(Integer roleId, String type, List<Integer> rights) throws Exception {
        BigInteger newRights = BigIntegerUtils.sumRights(rights);
        switch (type) {
            case "menu":
                roleDao.updateRights(roleId, newRights.toString());
                break;
            case "query":
                roleDao.updateQuery(roleId, newRights.toString());
                break;
            case "add":
                roleDao.updateAdd(roleId, newRights.toString());
                break;
            case "change":
                roleDao.updateChange(roleId, newRights.toString());
                break;
            case "del":
                roleDao.updateDel(roleId, newRights.toString());
                break;
            default:
                break;
        }

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
        BigInteger val = BigInteger.ZERO;
        switch (type) {
            case "menu":
                if(role.getRights()!=null)
                    val = new BigInteger(role.getRights());
                break;
            case "query":
                if(role.getQuery()!=null)
                    val = new BigInteger(role.getQuery());
                break;
            case "add":
                if(role.getAdd()!=null)
                    val = new BigInteger(role.getAdd());
                break;
            case "change":
                if(role.getChange()!=null)
                    val = new BigInteger(role.getChange());
                break;
            case "del":
                if(role.getDel()!=null)
                    val = new BigInteger(role.getDel());
                break;
            default:
                break;
        }
        for (MenuEntity menu : menuEntityList
        ) {
            if (val.testBit(menu.getId())) {
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

    @Override
    public void delete(Integer roleId) throws Exception {
        roleDao.deleteById(roleId);
    }
}
