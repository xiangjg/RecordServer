package com.jone.record.controller;

import com.jone.record.controller.common.SystemControllerLog;
import com.jone.record.dao.RedisDao;
import com.jone.record.entity.center.CenterBookmarks;
import com.jone.record.entity.center.CenterFavorite;
import com.jone.record.entity.center.CenterMsg;
import com.jone.record.entity.vo.PageVo;
import com.jone.record.entity.vo.UserCenterParamVo;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.UserCenterService;
import com.jone.record.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/msg/list", method = RequestMethod.POST)
    @ApiOperation(value = "获取我的消息列表", notes = "输入state,type")
    @SystemControllerLog(description = "获取消息列表")
    public void list(@RequestBody UserCenterParamVo param, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request, redisDao);
            if (null == userInfo) {
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            } else {
                PageVo<CenterMsg> centerMsgList = userCenterService.getMyCenterMsg(userInfo.getUserId(), param.getState(), param.getType(),param.getPage(), param.getSize());
                printJson(ResultUtil.success(centerMsgList), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
    @RequestMapping(value = "/msg/listNotDel", method = RequestMethod.POST)
    @ApiOperation(value = "获取我的消息列表(非删除)", notes = "")
    @SystemControllerLog(description = "获取消息列表(非删除)")
    public void listNotDel(HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request, redisDao);
            if (null == userInfo) {
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            } else {
                List<CenterMsg> centerMsgList = userCenterService.listNotDel(userInfo.getUserId().toString());
                printJson(ResultUtil.success(centerMsgList), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
    @RequestMapping(value = "/msg/listNotRead", method = RequestMethod.POST)
    @ApiOperation(value = "获取我的消息列表(未读)", notes = "")
    @SystemControllerLog(description = "获取消息列表(未读)")
    public void listNotRead(HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request, redisDao);
            if (null == userInfo) {
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            } else {
                List<CenterMsg> centerMsgList = userCenterService.listNotRead(userInfo.getUserId().toString());
                printJson(ResultUtil.success(centerMsgList), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
    @RequestMapping(value = "/msg/save", method = RequestMethod.POST)
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

    @RequestMapping(value = "/msg/read", method = RequestMethod.POST)
    @ApiOperation(value = "阅读消息", notes = "输入msgId")
    @SystemControllerLog(description = "阅读消息")
    public void readMsg(@RequestParam Integer[] msgId, HttpServletRequest request, HttpServletResponse response) {
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

    @RequestMapping(value = "/msg/del", method = RequestMethod.POST)
    @ApiOperation(value = "删除消息", notes = "输入msgId")
    @SystemControllerLog(description = "删除消息")
    public void delete(@RequestParam Integer[] msgId, HttpServletRequest request, HttpServletResponse response) {
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

    @RequestMapping(value = "/favorite/list", method = RequestMethod.POST)
    @ApiOperation(value = "获取收藏列表", notes = "输入state")
    @SystemControllerLog(description = "获取收藏列表")
    public void listFavorite(@RequestBody UserCenterParamVo param, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request, redisDao);
            if (null == userInfo) {
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            } else {
                PageVo<CenterFavorite> centerFavoritePageVo = userCenterService.listByState(param.getState(),param.getPage(), param.getSize(),userInfo);
                printJson(ResultUtil.success(centerFavoritePageVo), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/favorite/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存收藏", notes = "输入type,detail")
    @SystemControllerLog(description = "保存收藏")
    public void saveFavorite(@RequestBody CenterFavorite centerFavorite, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request, redisDao);
            if (null == userInfo) {
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            } else {
                centerFavorite = userCenterService.saveCenterFavorite(centerFavorite, userInfo);
                printJson(ResultUtil.success(centerFavorite), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/favorite/del", method = RequestMethod.POST)
    @ApiOperation(value = "删除收藏", notes = "输入id")
    @SystemControllerLog(description = "删除收藏")
    public void deleteFavorite(@RequestParam Integer id, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request, redisDao);
            if (null == userInfo) {
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            } else {
                userCenterService.deleteCenterFavorite(id, userInfo);
                printJson(ResultUtil.success(), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/bookmarks/list", method = RequestMethod.POST)
    @ApiOperation(value = "获取收藏列表", notes = "输入state,type")
    @SystemControllerLog(description = "获取收藏列表")
    public void listBookmarks(@RequestBody UserCenterParamVo param, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request, redisDao);
            if (null == userInfo) {
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            } else {
                PageVo<CenterBookmarks> centerBookmarksPageVo = userCenterService.listCenterBookmarks(param.getState(),param.getType(),param.getPage(), param.getSize(),userInfo);
                printJson(ResultUtil.success(centerBookmarksPageVo), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/bookmarks/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存收藏", notes = "输入type,title,bookId,chapterId")
    @SystemControllerLog(description = "保存收藏")
    public void saveBookmarks(@RequestBody CenterBookmarks centerFavorite, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request, redisDao);
            if (null == userInfo) {
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            } else {
                centerFavorite = userCenterService.saveCenterBookmarks(centerFavorite, userInfo);
                printJson(ResultUtil.success(centerFavorite), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/bookmarks/del", method = RequestMethod.POST)
    @ApiOperation(value = "删除收藏", notes = "输入id")
    @SystemControllerLog(description = "删除收藏")
    public void deleteBookmarks(@RequestParam Integer id, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request, redisDao);
            if (null == userInfo) {
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            } else {
                userCenterService.deleteCenterBookmarks(id, userInfo);
                printJson(ResultUtil.success(), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
}
