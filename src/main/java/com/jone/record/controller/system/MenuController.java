package com.jone.record.controller.system;

import com.jone.record.controller.BaseController;
import com.jone.record.entity.system.MenuEntity;
import com.jone.record.service.MenuService;
import com.jone.record.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/menu")
@Api(tags = "菜单管理相关接口")
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "菜单信息列表", notes = "")
    public void list(HttpServletResponse response) {
        try {
            List<MenuEntity> menuList = menuService.list();
            printJson(ResultUtil.success(menuList), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(@RequestBody MenuEntity menuEntity, HttpServletResponse response) {
        try {
            menuService.save(menuEntity);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
}
