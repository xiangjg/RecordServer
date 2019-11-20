package com.jone.record.controller;

import com.jone.record.controller.common.SystemControllerLog;
import com.jone.record.dao.RedisDao;
import com.jone.record.entity.center.CenterMsg;
import com.jone.record.entity.special.TQztSubjectsEntity;
import com.jone.record.entity.vo.UserCenterParamVo;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.UserCenterService;
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
@RequestMapping("/center")
@Api(tags = "个人中心数据接口服务")
public class UserCenterController extends BaseController {

    @Autowired
    private UserCenterService userCenterService;
    @Autowired
    private RedisDao redisDao;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "获取消息列表", notes = "输入userId,state,type")
    @SystemControllerLog(description = "获取消息列表")
    public void list(@RequestBody UserCenterParamVo param, HttpServletResponse response) {
        try {
            List<CenterMsg> centerMsgList = userCenterService.getMyCenterMsg(param.getUserId(), param.getState(), param.getType());
            printJson(ResultUtil.success(centerMsgList), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存消息", notes = "输入toId,type,title,body")
    @SystemControllerLog(description = "保存消息")
    public void save(@RequestBody CenterMsg centerMsg, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request, redisDao);
            if (null == userInfo) {
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            } else {
                centerMsg.setFromId(userInfo.getUserId().toString());
                centerMsg = userCenterService.saveCenterMsg(centerMsg);
                printJson(ResultUtil.success(centerMsg), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/read", method = RequestMethod.POST)
    @ApiOperation(value = "阅读消息", notes = "输入msgId")
    @SystemControllerLog(description = "阅读消息")
    public void readMsg(@RequestParam Integer msgId, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request, redisDao);
            if (null == userInfo) {
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            } else {
                userCenterService.readMsg(msgId, userInfo);
                printJson(ResultUtil.success(), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ApiOperation(value = "删除消息", notes = "输入msgId")
    @SystemControllerLog(description = "删除消息")
    public void delete(@RequestParam Integer msgId, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request, redisDao);
            if (null == userInfo) {
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            } else {
                userCenterService.deleteCenterMsg(msgId, userInfo);
                printJson(ResultUtil.success(), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
}
