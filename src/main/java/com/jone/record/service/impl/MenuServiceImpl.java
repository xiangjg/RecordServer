package com.jone.record.service.impl;


import com.jone.record.dao.system.MenuDao;
import com.jone.record.dao.system.RoleDao;
import com.jone.record.entity.system.MenuEntity;
import com.jone.record.entity.system.RoleEntity;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.MenuService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

        return buildTree(list,0);
    }
    public static List<MenuEntity> buildTree(List<MenuEntity> list,int parentId){
        List<MenuEntity> menus=new ArrayList<MenuEntity>();
        for (MenuEntity menu : list) {

            int menuId = menu.getId();
            int pid = menu.getPid();
            menu.setHave(false);
            if (parentId == pid) {
                List<MenuEntity> menuLists = buildTree(list, menuId);
                menu.setChild(menuLists);
                menu.setHave(true);
                menus.add(menu);
            }
        }

        return menus;
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
            if(new BigInteger(role.getRights()).testBit(m.getId()))
                data.add(m);
        }
        return data;
    }

    @Override
    public List<MenuEntity> findByLevel(Integer level) throws Exception {
        return menuDao.findByLevel(level);
    }

    @Override
    public List<Map<String, Object>> listLevel() throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Integer> levels = menuDao.findLevels();
        Map<String, Object> map1 = new HashedMap();
        map1.put("level", 0);
        map1.put("desc", "根目录");
        list.add(map1);
        for (Integer level:levels
             ) {
            Map<String, Object> map = new HashedMap();
            String desc = "";
            switch (level){
                case 1:
                    desc = "一级菜单";
                    break;
                case 2:
                    desc = "二级菜单";
                    break;
                case 3:
                    desc = "三级菜单";
                    break;
                case 4:
                    desc = "四级菜单";
                    break;
                case 5:
                    desc = "五级菜单";
                    break;
                case 6:
                    desc = "六级菜单";
                    break;
                case 7:
                    desc = "七级菜单";
                    break;
                default:
                    desc = "错误等级";
                    break;
            }
            map.put("level", level);
            map.put("desc", desc);
            list.add(map);
        }
        return list;
    }
}
