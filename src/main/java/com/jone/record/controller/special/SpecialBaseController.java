package com.jone.record.controller.special;

import com.jone.record.controller.BaseController;
import com.jone.record.controller.common.SystemControllerLog;
import com.jone.record.dao.RedisDao;
import com.jone.record.entity.special.NodeContent;
import com.jone.record.entity.special.SubjectsNodes;
import com.jone.record.entity.special.TQztSubjectsEntity;
import com.jone.record.entity.vo.PageVo;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.SpecialBaseService;
import com.jone.record.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/qzt")
@Api(tags = "专题基础信息")
public class SpecialBaseController extends BaseController {

    @Autowired
    private SpecialBaseService specialBaseService;
    @Autowired
    private RedisDao redisDao;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "基础信息列表", notes = "输入state")
    @SystemControllerLog(description = "查看基础信息列表")
    public void list(@RequestParam Integer state,@RequestParam Integer page,@RequestParam Integer size, HttpServletResponse response) {
        try {
            PageVo<TQztSubjectsEntity> subjectsEntityList = specialBaseService.listByState(state, page, size);
            printJson(ResultUtil.success(subjectsEntityList), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存基础信息", notes = "输入基础信息tQztSubjectsEntity及图片文件file")
    @SystemControllerLog(description = "保存基础信息")
    public void save(@RequestBody TQztSubjectsEntity tQztSubjectsEntity, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request, redisDao);
            if (null == userInfo) {
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            } else {
                TQztSubjectsEntity subjectsEntity = specialBaseService.save(tQztSubjectsEntity, userInfo);
                printJson(ResultUtil.success(subjectsEntity), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除基础信息", notes = "")
    @SystemControllerLog(description = "删除基础信息")
    public void delete(@RequestParam Integer id, HttpServletResponse response) {
        try {
            specialBaseService.delete(id);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/listNodes", method = RequestMethod.POST)
    @ApiOperation(value = "栏目信息列表", notes = "输入state,sid")
    @SystemControllerLog(description = "栏目信息列表")
    public void listNodes(@RequestParam Integer state, @RequestParam Integer sid, HttpServletResponse response) {
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
    @SystemControllerLog(description = "保存栏目信息")
    public void saveNode(@RequestBody SubjectsNodes subjectsNodes, HttpServletResponse response) {
        try {
            SubjectsNodes subjectsEntity = specialBaseService.save(subjectsNodes);
            printJson(ResultUtil.success(subjectsEntity), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/deleteNode", method = RequestMethod.POST)
    @ApiOperation(value = "删除栏目信息", notes = "")
    @SystemControllerLog(description = "删除栏目信息")
    public void deleteNode(@RequestParam Integer id, HttpServletResponse response) {
        try {
            specialBaseService.deleteSubjectsNodes(id);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/listNodeContent", method = RequestMethod.POST)
    @ApiOperation(value = "栏目内容列表", notes = "输入state,sid")
    @SystemControllerLog(description = "栏目内容列表")
    public void listNodeContent(@RequestParam Integer state, @RequestParam Integer nid, HttpServletResponse response) {
        try {
            List<NodeContent> nodeContentList = specialBaseService.listByStateAndNid(state, nid);
            printJson(ResultUtil.success(nodeContentList), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/saveNodeContent", method = RequestMethod.POST)
    @ApiOperation(value = "保存栏目内容", notes = "")
    @SystemControllerLog(description = "保存栏目内容")
    public void saveNodeContent(@RequestBody NodeContent nodeContent, HttpServletResponse response) {
        try {
            nodeContent = specialBaseService.save(nodeContent);
            printJson(ResultUtil.success(nodeContent), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/deleteNodeContent", method = RequestMethod.POST)
    @ApiOperation(value = "删除栏目内容", notes = "")
    @SystemControllerLog(description = "删除栏目内容")
    public void deleteNodeContent(@RequestParam Integer id, HttpServletResponse response) {
        try {
            specialBaseService.deleteNodeContent(id);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
}
