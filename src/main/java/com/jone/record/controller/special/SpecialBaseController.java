package com.jone.record.controller.special;

import com.jone.record.controller.BaseController;
import com.jone.record.entity.special.SubjectsNodes;
import com.jone.record.entity.special.TQztSubjectsEntity;
import com.jone.record.service.SpecialBaseService;
import com.jone.record.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/type")
@Api(tags = "专题基础信息")
public class SpecialBaseController extends BaseController {

    @Autowired
    private SpecialBaseService specialBaseService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation(value = "基础信息列表", notes = "输入state")
    public void list(@RequestParam Short state, HttpServletResponse response) {
        try {
            List<TQztSubjectsEntity> subjectsEntityList = specialBaseService.listByState(state);
            printJson(ResultUtil.success(subjectsEntityList), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存基础信息", notes = "")
    public void save(@RequestParam TQztSubjectsEntity tQztSubjectsEntity, HttpServletResponse response) {
        try {
            TQztSubjectsEntity subjectsEntity = specialBaseService.save(tQztSubjectsEntity);
            printJson(ResultUtil.success(subjectsEntity), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ApiOperation(value = "删除基础信息", notes = "")
    public void delete(@RequestParam Integer id, HttpServletResponse response) {
        try {
            specialBaseService.delete(id);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/listNodes", method = RequestMethod.GET)
    @ApiOperation(value = "栏目信息列表", notes = "输入state,sid")
    public void listNodes(@RequestParam Integer state,@RequestParam Integer sid, HttpServletResponse response) {
        try {
            List<SubjectsNodes> subjectsEntityList = specialBaseService.listByStateAndSid(state, sid);
            printJson(ResultUtil.success(subjectsEntityList), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/saveNode", method = RequestMethod.POST)
    @ApiOperation(value = "保存栏目信息", notes = "")
    public void saveNode(@RequestParam SubjectsNodes subjectsNodes, HttpServletResponse response) {
        try {
            SubjectsNodes subjectsEntity = specialBaseService.save(subjectsNodes);
            printJson(ResultUtil.success(subjectsEntity), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/deleteNode", method = RequestMethod.GET)
    @ApiOperation(value = "删除栏目信息", notes = "")
    public void deleteNode(@RequestParam Integer id, HttpServletResponse response) {
        try {
            specialBaseService.deleteSubjectsNodes(id);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
}
