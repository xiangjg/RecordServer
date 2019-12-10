package com.jone.record.controller.system;

import com.jone.record.controller.BaseController;
import com.jone.record.dao.RedisDao;
import com.jone.record.entity.system.RoleEntity;
import com.jone.record.entity.system.UserEntity;
import com.jone.record.entity.vo.RoleRightsVo;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.RoleService;
import com.jone.record.service.UserService;
import com.jone.record.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
@Api(tags = "角色管理相关接口")
public class RoleController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisDao redisDao;

    @RequestMapping(value = "/list")
    @ApiOperation(value = "角色信息列表", notes = "")
    public void list(HttpServletResponse response){
        try {
            List<RoleEntity> roleList = roleService.getAllRole();
            printJson(ResultUtil.success(roleList),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/getRoleRights")
    @ApiOperation(value = "获取角色权限", notes = "")
    public void getRole(@RequestParam Integer id, @RequestParam String type, HttpServletResponse response){
        try {
            Map<String, Object> data = roleService.getMenuList(id,type);
            printJson(ResultUtil.success(data),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/saveRight")
    @ApiOperation(value = "保存角色权限", notes = "")
    public void getRole(@RequestBody RoleRightsVo roleRightsVo, HttpServletResponse response){
        try {
            roleService.updateRights(roleRightsVo.getId(),roleRightsVo.getType(),roleRightsVo.getCheckArr());
            printJson(ResultUtil.success(),response);
        }catch (Exception e){
            logger.error("{}",e);
            printJson(ResultUtil.error(-1,e.getMessage()),response);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(@RequestParam Integer roleId, HttpServletResponse response, HttpServletRequest request) {
        if (roleId == 1)
            printJson(ResultUtil.error(-1, "不能删除超级管理员"), response);
        else {
            try {
                roleService.delete(roleId);
                printJson(ResultUtil.success(), response);
            } catch (Exception e) {
                logger.error("{}", e);
                printJson(ResultUtil.error(-1, e.getMessage()), response);
            }
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(@RequestBody RoleEntity role, HttpServletResponse response) {
        try {
            roleService.save(role);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
}
