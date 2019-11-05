package com.jone.record.controller.map;

import com.jone.record.controller.BaseController;
import com.jone.record.entity.map.PoisEntity;
import com.jone.record.entity.map.ShareEntity;
import com.jone.record.entity.special.NodeContent;
import com.jone.record.service.MapService;
import com.jone.record.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/map")
@Api(tags = "志慧地图")
public class MapController extends BaseController {

    @Autowired
    private MapService mapService;

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
    public void savePois(@RequestParam PoisEntity poisEntities, HttpServletResponse response) {
        try {
            poisEntities = mapService.save(poisEntities);
            printJson(ResultUtil.success(poisEntities), response);
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
    public void saveShare(@RequestParam ShareEntity shareEntities, HttpServletResponse response) {
        try {
            shareEntities = mapService.save(shareEntities);
            printJson(ResultUtil.success(shareEntities), response);
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
