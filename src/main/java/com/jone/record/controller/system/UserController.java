package com.jone.record.controller.system;

import com.jone.record.controller.BaseController;
import com.jone.record.dao.RedisDao;
import com.jone.record.entity.system.UserEntity;
import com.jone.record.entity.vo.PageParamVo;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.RoleService;
import com.jone.record.service.UserService;
import com.jone.record.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api(tags = "用户信息相关接口")
public class UserController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisDao redisDao;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "用户信息列表", notes = "可用关键字keyword进去模糊查询，返回分页列表数据")
    public void list(@RequestBody PageParamVo pageParamVo, HttpServletResponse response) {
        try {
            Page<UserEntity> userList = userService.listUser(pageParamVo);
            printJson(ResultUtil.success(userList), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(@RequestBody UserEntity user, HttpServletResponse response) {
        try {
            userService.addUser(user);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(@RequestParam Integer userId, HttpServletResponse response, HttpServletRequest request) {
        UserInfo cUser = getRedisUser(request, redisDao);
        if (cUser == null || cUser.getRole().getId() != 1)
            printJson(ResultUtil.error(-1, "该用户无操作权限"), response);
        else {
            try {
                userService.delUser(userId);
                printJson(ResultUtil.success(), response);
            } catch (Exception e) {
                logger.error("{}", e);
                printJson(ResultUtil.error(-1, e.getMessage()), response);
            }
        }
    }

//    @RequestMapping(value = "/deleteUsers")
//    public void deleteUsers(@RequestParam String loginNames, HttpServletResponse response, HttpServletRequest request) {
//        UserInfo cUser = getRedisUser(request, redisDao);
//        if (cUser == null || cUser.getRole().getId() != 1)
//            printJson(ResultUtil.error(-1, "该用户无操作权限"), response);
//        else {
//            try {
//                userService.deleteUsers(loginNames.split(","));
//                printJson(ResultUtil.success(), response);
//            } catch (Exception e) {
//                logger.error("{}", e);
//                printJson(ResultUtil.error(-1, e.getMessage()), response);
//            }
//        }
//    }

//    @RequestMapping(value = "/getUserById")
//    public void getUserById(@RequestParam String loginName, HttpServletResponse response) {
//        try {
//            Map<String, Object> map = new HashMap<>();
//            UserEntity user = userService.queryUserByLoginName(loginName);
//            if (user != null)
//                map.put("user", user);
//            map.put("roleList", roleService.getAllRole());
//            printJson(ResultUtil.success(map), response);
//        } catch (Exception e) {
//            logger.error("{}", e);
//            printJson(ResultUtil.error(-1, e.getMessage()), response);
//        }
//    }

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public void getUserInfo(@RequestParam String pwd, HttpServletResponse response, HttpServletRequest request) {
        UserInfo cUser = getRedisUser(request, redisDao);
        try {
            UserEntity user = userService.findUserByLoginNameAndPwd(cUser.getLoginName(), pwd);
            printJson(ResultUtil.success(user), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/update/pwd", method = RequestMethod.POST)
    public void updatePassword(@RequestParam String password, HttpServletResponse response, HttpServletRequest request) {
        UserInfo cUser = getRedisUser(request, redisDao);
        try {
            if (cUser == null) {
                printJson(ResultUtil.error(-1, "登录超时"), response);
            } else {
                userService.updatePassword(cUser.getLoginName(), password);
                printJson(ResultUtil.success(), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/reset/password", method = RequestMethod.POST)
    public void resetPassword(@RequestParam String loginName, HttpServletResponse response, HttpServletRequest request) {
        UserInfo cUser = getRedisUser(request, redisDao);
        if (cUser == null || cUser.getRole().getId() != 1)
            printJson(ResultUtil.error(-1, "该用户无操作权限"), response);
        else {
            try {
                userService.resetPassword(loginName);
                printJson(ResultUtil.success(), response);
            } catch (Exception e) {
                logger.error("{}", e);
                printJson(ResultUtil.error(-1, e.getMessage()), response);
            }
        }
    }

    @RequestMapping(value = "/getUserInfoAndRole", method = RequestMethod.POST)
    public void getUserInfoAndRole(HttpServletResponse response, HttpServletRequest request) {
        UserInfo cUser = getRedisUser(request, redisDao);
        try {
            if (cUser != null)
                printJson(ResultUtil.success(cUser), response);
            else
                printJson(ResultUtil.error(-1, "登录超时"), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
}
