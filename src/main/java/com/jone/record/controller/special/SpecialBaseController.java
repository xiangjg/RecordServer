package com.jone.record.controller.special;

import com.jone.record.controller.BaseController;
import com.jone.record.dao.RedisDao;
import com.jone.record.entity.special.NodeContent;
import com.jone.record.entity.special.SubjectsNodes;
import com.jone.record.entity.special.TQztSubjectsEntity;
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
import java.util.List;

@RestController
@RequestMapping("/specialBase")
@Api(tags = "专题基础信息")
public class SpecialBaseController extends BaseController {

    @Autowired
    private SpecialBaseService specialBaseService;
    @Autowired
    private RedisDao redisDao;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
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
    @ApiOperation(value = "保存基础信息", notes = "输入基础信息tQztSubjectsEntity及图片文件file")
    public void save(@RequestParam TQztSubjectsEntity tQztSubjectsEntity, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request,redisDao);
            if(null==userInfo){
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            }else {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = multiRequest.getFiles("file");
            TQztSubjectsEntity subjectsEntity = specialBaseService.save(tQztSubjectsEntity, files, userInfo);
            printJson(ResultUtil.success(subjectsEntity), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
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

    @RequestMapping(value = "/listNodes", method = RequestMethod.POST)
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

    @RequestMapping(value = "/deleteNode", method = RequestMethod.POST)
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

    @RequestMapping(value = "/listNodeContent", method = RequestMethod.POST)
    @ApiOperation(value = "栏目内容列表", notes = "输入state,sid")
    public void listNodeContent(@RequestParam Integer state,@RequestParam Integer nid, HttpServletResponse response) {
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
    public void saveNodeContent(@RequestParam NodeContent nodeContent, HttpServletResponse response) {
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
