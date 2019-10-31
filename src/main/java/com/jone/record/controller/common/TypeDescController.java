package com.jone.record.controller.common;

import com.jone.record.controller.BaseController;
import com.jone.record.entity.common.TypeEntity;
import com.jone.record.service.TypeService;
import com.jone.record.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/type")
@Api(tags = "类型定义相关接口")
public class TypeDescController extends BaseController {

    @Autowired
    private TypeService typeService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value="保存类型定义", notes="ref:关联信息(例如：文件类型传入file),desc:类型定义名称,type:类型值")
    public void save(@RequestBody TypeEntity typeEntity, HttpServletResponse response) {
        try {
            typeService.save(typeEntity);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value="删除类型定义", notes="传入类型id")
    public void delete(@RequestParam Integer id, HttpServletResponse response, HttpServletRequest request) {
        try {
            typeService.deleteById(id);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.POST)
    @ApiOperation(value="获取所有类型数据列表", notes="")
    public void listAll(HttpServletResponse response) {
        try {
            printJson(ResultUtil.success(typeService.listAll()), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
}
