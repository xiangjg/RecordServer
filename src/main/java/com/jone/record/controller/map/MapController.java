package com.jone.record.controller.map;

import com.jone.record.controller.BaseController;
import com.jone.record.dao.RedisDao;
import com.jone.record.entity.map.PoisEntity;
import com.jone.record.entity.map.ShareEntity;
import com.jone.record.entity.special.NodeContent;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.MapService;
import com.jone.record.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/zhdt")
@Api(tags = "志慧地图、志慧共享接口")
public class MapController extends BaseController {

    @Autowired
    private MapService mapService;
    @Autowired
    private RedisDao redisDao;

    @RequestMapping(value = "/listPois", method = RequestMethod.POST)
    @ApiOperation(value = "兴趣点列表", notes = "输入state,sid")
    public void listPois(@RequestParam Integer state, HttpServletResponse response) {
        try {
            List<PoisEntity> poisEntities = mapService.listPoisByState(state);
            printJson(ResultUtil.success(poisEntities), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/savePois", method = RequestMethod.POST)
    @ApiOperation(value = "保存兴趣点", notes = "")
    public void savePois(@RequestBody PoisEntity poisEntities, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request, redisDao);
            if (null == userInfo) {
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            } else {
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                List<MultipartFile> files = multiRequest.getFiles("file");
                poisEntities = mapService.save(poisEntities, files);
                printJson(ResultUtil.success(poisEntities), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/deletePois", method = RequestMethod.POST)
    @ApiOperation(value = "删除兴趣点", notes = "")
    public void deletePois(@RequestParam Integer id, HttpServletResponse response) {
        try {
            mapService.deletePoisEntity(id);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/listShare", method = RequestMethod.POST)
    @ApiOperation(value = "共享列表", notes = "输入state,sid")
    public void listShare(@RequestParam Integer state, HttpServletResponse response) {
        try {
            List<ShareEntity> shareEntities = mapService.listShareByState(state);
            printJson(ResultUtil.success(shareEntities), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/saveShare", method = RequestMethod.POST)
    @ApiOperation(value = "保存共享", notes = "")
    public void saveShare(@RequestBody ShareEntity shareEntities, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request, redisDao);
            if (null == userInfo) {
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            } else {
                shareEntities.setCreator(userInfo.getUserId().toString());
                shareEntities.setInsertDt(new Date());
                shareEntities.setState(0);
                shareEntities.setAuditId(null);
                shareEntities.setAuditDt(null);
                shareEntities = mapService.save(shareEntities);
                printJson(ResultUtil.success(shareEntities), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
    @RequestMapping(value = "/auditShare", method = RequestMethod.POST)
    @ApiOperation(value = "审核共享信息", notes = "")
    public void auditShare(@RequestParam Integer shareId, @RequestParam Integer state, HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = getRedisUser(request, redisDao);
            if (null == userInfo) {
                printJson(ResultUtil.error(-2, "用户登录没有登录,或session过期"), response);
            } else {
                ShareEntity shareEntities = mapService.auditShare(shareId, state, userInfo);
                printJson(ResultUtil.success(shareEntities), response);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
    @RequestMapping(value = "/deleteShare", method = RequestMethod.POST)
    @ApiOperation(value = "删除共享", notes = "")
    public void deleteShare(@RequestParam Integer id, HttpServletResponse response) {
        try {
            mapService.deleteShareEntity(id);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
}
