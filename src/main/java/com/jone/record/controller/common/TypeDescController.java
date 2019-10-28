package com.jone.record.controller.common;

import com.jone.record.controller.BaseController;
import com.jone.record.entity.common.TypeEntity;
import com.jone.record.service.TypeService;
import com.jone.record.util.ResultUtil;
import io.swagger.annotations.Api;
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
    public void listAll(@RequestBody TypeEntity typeEntity, HttpServletResponse response) {
        try {
            printJson(ResultUtil.success(typeService.listAll()), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
}
