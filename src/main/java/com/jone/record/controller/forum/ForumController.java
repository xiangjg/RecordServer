package com.jone.record.controller.forum;

import com.jone.record.controller.BaseController;
import com.jone.record.entity.forum.CourseCategory;
import com.jone.record.entity.map.ShareEntity;
import com.jone.record.service.ForumService;
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
@RequestMapping("/forum")
@Api(tags = "方志讲堂")
public class ForumController extends BaseController {

    @Autowired
    private ForumService forumService;

    @RequestMapping(value = "/listShare", method = RequestMethod.POST)
    @ApiOperation(value = "课程分类列表", notes = "输入state,sid")
    public void listShare(HttpServletResponse response) {
        try {
            List<CourseCategory> courseCategories = forumService.listCourseCategory();
            printJson(ResultUtil.success(courseCategories), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/saveShare", method = RequestMethod.POST)
    @ApiOperation(value = "保存课程分类", notes = "")
    public void saveShare(@RequestParam CourseCategory courseCategories, HttpServletResponse response) {
        try {
            courseCategories = forumService.save(courseCategories);
            printJson(ResultUtil.success(courseCategories), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/deleteShare", method = RequestMethod.POST)
    @ApiOperation(value = "删除课程分类", notes = "")
    public void deleteShare(@RequestParam Integer id, HttpServletResponse response) {
        try {
            forumService.deleteCourseCategory(id);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
}
