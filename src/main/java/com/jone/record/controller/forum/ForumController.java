package com.jone.record.controller.forum;

import com.jone.record.controller.BaseController;
import com.jone.record.entity.forum.CourseCategory;
import com.jone.record.entity.forum.CoursesEntity;
import com.jone.record.entity.forum.EpisodesEntity;
import com.jone.record.entity.forum.FocusEntity;
import com.jone.record.entity.map.ShareEntity;
import com.jone.record.entity.vo.PageVo;
import com.jone.record.service.ForumService;
import com.jone.record.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fzjt")
@Api(tags = "方志讲堂")
public class ForumController extends BaseController {

    @Autowired
    private ForumService forumService;

    @RequestMapping(value = "/listCategory", method = RequestMethod.POST)
    @ApiOperation(value = "课程分类列表", notes = "")
    public void listCategory(HttpServletResponse response) {
        try {
            List<CourseCategory> courseCategories = forumService.listCourseCategory();
            printJson(ResultUtil.success(courseCategories), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/saveCategory", method = RequestMethod.POST)
    @ApiOperation(value = "保存课程分类", notes = "")
    public void saveCategory(@RequestBody CourseCategory courseCategories, HttpServletResponse response) {
        try {
            courseCategories = forumService.save(courseCategories);
            printJson(ResultUtil.success(courseCategories), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/deleteCategory", method = RequestMethod.POST)
    @ApiOperation(value = "删除课程分类", notes = "")
    public void deleteCategory(@RequestParam Integer id, HttpServletResponse response) {
        try {
            forumService.deleteCourseCategory(id);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/listCourse", method = RequestMethod.POST)
    @ApiOperation(value = "课程信息列表", notes = "输入state")
    public void listCourse(@RequestParam Integer state,@RequestParam Integer page,@RequestParam Integer size, HttpServletResponse response) {
        try {
            PageVo<CoursesEntity> coursesEntities = forumService.listCourse(state, page, size);
            printJson(ResultUtil.success(coursesEntities), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/saveCourse", method = RequestMethod.POST)
    @ApiOperation(value = "保存课程信息", notes = "")
    public void saveCourse(@RequestBody CoursesEntity coursesEntity, HttpServletResponse response) {
        try {
            coursesEntity = forumService.save(coursesEntity);
            printJson(ResultUtil.success(coursesEntity), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/deleteCourse", method = RequestMethod.POST)
    @ApiOperation(value = "删除课程信息", notes = "")
    public void deleteCourse(@RequestParam Integer id, HttpServletResponse response) {
        try {
            forumService.deleteCourse(id);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/listEpisodes", method = RequestMethod.POST)
    @ApiOperation(value = "课程章节列表", notes = "输入state")
    public void listEpisodes(@RequestParam Integer courseId,@RequestParam Integer state, HttpServletResponse response) {
        try {
            List<EpisodesEntity> episodesEntities = forumService.listEpisodesEntity(courseId, state);
            printJson(ResultUtil.success(episodesEntities), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/saveEpisodes", method = RequestMethod.POST)
    @ApiOperation(value = "保存课程章节", notes = "")
    public void saveEpisodes(@RequestBody EpisodesEntity episodesEntity, HttpServletResponse response) {
        try {
            episodesEntity = forumService.save(episodesEntity);
            printJson(ResultUtil.success(episodesEntity), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/deleteEpisodes", method = RequestMethod.POST)
    @ApiOperation(value = "删除课程章节", notes = "")
    public void deleteEpisodes(@RequestParam Integer id, HttpServletResponse response) {
        try {
            forumService.deleteEpisodesEntity(id);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/updatePlayCount", method = RequestMethod.POST)
    @ApiOperation(value = "更新章节读取次数", notes = "输入章节编码id和课程编码courseId")
    public void updatePlayCount(@RequestParam Integer id,@RequestParam Integer courseId, HttpServletResponse response) {
        try {
            forumService.updatePlayCount(id, courseId);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/listFocus", method = RequestMethod.POST)
    @ApiOperation(value = "课程焦点列表", notes = "输入state")
    public void listFocus(@RequestParam Integer page,@RequestParam Integer size, HttpServletResponse response) {
        try {
            Map<String, Object> map = new HashedMap();
            map.put("page", page);
            map.put("size", size);
            PageVo<FocusEntity> listFocus = forumService.listFocus(map);
            printJson(ResultUtil.success(listFocus), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/listFocusToDay", method = RequestMethod.POST)
    @ApiOperation(value = "今日课程焦点列表", notes = "输入state")
    public void listFocusToDay(@RequestParam Integer page,@RequestParam Integer size, HttpServletResponse response) {
        try {
            Map<String, Object> map = new HashedMap();
            map.put("page", page);
            map.put("size", size);
            map.put("tm", new Date());
            PageVo<FocusEntity> listFocus = forumService.listFocus(map);
            printJson(ResultUtil.success(listFocus), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/saveFocus", method = RequestMethod.POST)
    @ApiOperation(value = "保存课程焦点", notes = "")
    public void saveFocus(@RequestBody FocusEntity focusEntity, HttpServletResponse response) {
        try {
            focusEntity = forumService.save(focusEntity);
            printJson(ResultUtil.success(focusEntity), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    @RequestMapping(value = "/deleteFocus", method = RequestMethod.POST)
    @ApiOperation(value = "删除课程焦点", notes = "")
    public void deleteFocus(@RequestParam Integer id, HttpServletResponse response) {
        try {
            forumService.deleteFocus(id);
            printJson(ResultUtil.success(), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }
}
