
/**
 * create by dugg 20191106
 * KBase数据库查询数据相关类
 */

package com.jone.record.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jone.record.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import com.jone.record.kbase.KBaseExecute;

import javax.servlet.http.HttpServletResponse;


@CrossOrigin
@RestController
@RequestMapping("/books")
@Api(tags = "方志馆")
public class kbaseController extends BaseController {

    private KBaseExecute kbaseTools = null;
    private static final Logger loger = LoggerFactory.getLogger(KBaseExecute.class);

    public kbaseController() {
        if (null == kbaseTools) {
            kbaseTools = new KBaseExecute();
        }
    }

    /**
     * 获取数据表记录数
     *
     * @return 返回JSON格式字符串
     * @params 输入JSONObject对象
     * 输入参数示例：{"type":"1","cls":"0"}
     * 资源类型，type：1，志书；2，年鉴；3，地方史；4，地情资料。
     * 分类代码，cls：0，全部；1，志书；2，年鉴，3：地方史；4，地情资料
     */
    @RequestMapping(value = "/GetBooksNums", method = RequestMethod.POST)
    @ApiOperation(value = "获取数据表记录数", notes = "输入JSONObject参数：资源类型type，分类类型cls。")
    public void GetBooksNums(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetBooksNums(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 获取志书列表
     *
     * @return 返回JSON格式字符串
     * @params 输入JSONObject对象
     * 输入参数示例：{"type":"1","state":"1","page":"1","pageSize":"10","title":"贵州省*"}
     * 资源类型，type：1，志书；2，年鉴；3，地方史；4，地情资料;
     * 上下状态，state：0，下架；1，上架；
     * 显示页码，page：分页查询，当前显示第几页，默认显示第 1 页；
     * 每页显示，pageSize：每页显示记录数，默认为 10 条；
     * 检索标题，title：根据书名进行检索。
     */
    @RequestMapping(value = "/GetHistoryBook", method = RequestMethod.POST)
    @ApiOperation(value = "根据标题获取书籍列表信息", notes = "输入JSONObject参数：资源类型type，上下架state，显示页码page，每页显示数量pageSize,检索标题title")
    public void GetHistoryBook(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONArray jsonArray = kbaseTools.GetHistoryBook(params);
            printJson(ResultUtil.success(jsonArray), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 获取书籍碎片化阅化阅读时的目录信息
     *
     * @return 返回JSON格式的字符串
     * @params 输入JSONObject对象，格式为 {"type": "1","id": "5D5A3DEA-1609-40c6-A6EF-76D069D7A764"}
     * @type 资源类型：1，志书；2，年鉴；3，地方史；4，地情资料;
     * @id 书籍的为唯一标识符，为GUID格式
     */
    @RequestMapping(value = "/GetReadCatalog", method = RequestMethod.POST)
    @ApiOperation(value = "获取碎片化阅读目录", notes = "输入JSONObject参数：资源类型type，书籍的ID-id")
    public void GetReadCatalog(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetReadCatalog(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 获取书籍基本信息时的目录信息
     *
     * @return 返回JSON格式的字符串
     * @params 输入JSONObject对象，格式为 {"type": "1","id": "5D5A3DEA-1609-40c6-A6EF-76D069D7A764"}
     * @type 资源类型：1，志书；2，年鉴；3，地方史；4，地情资料;
     * @id 书籍的为唯一标识符，为GUID格式
     */
    @RequestMapping(value = "/GetBookCatalog", method = RequestMethod.POST)
    @ApiOperation(value = "获取书籍基本目录信息", notes = "输入JSONObject参数：资源类型-type，书籍的ID-id")
    public void GetBookCatalog(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetBookCatalog(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }


    /**
     * 黔专题标题内容检索
     *
     * @return 返回JSON格式字符串
     * @type 资源类型，1 志书；2 年鉴；3 地方志；4 地情资料
     * @id id，传入书籍的GUID值，如果为空，则在整个章节表中查找相关数据
     * @keyword 传入的检索关键词
     * @page 显示页码，当前显示第几页，默认显示第一页
     * @pageSize 显示条数，当前页的显示条数，默认为是10条
     * @params 输入JSONObject对象，格式为 {"type":"1", "id":"5D5A3DEA-1609-40c6-A6EF-76D069D7A764","keyword":"地质构造","page":"1","pageSize":"10"}
     */
    @RequestMapping(value = "/GetTitleInfo", method = RequestMethod.POST)
    @ApiOperation(value = "获取专题标题目录信息", notes = "输入JSONObject参数：资源类型-type，书籍ID-id，检索词-keyword，显示页码-page，每页显示数量-pageSize")
    public void GetTitleInfo(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetTitleInfo(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }


    /**
     * 查询全文内容信息
     *
     * @return 返回JSON格式字符串，
     * @params 输入JSONObject，JSON格式字符串，如； {"type": "1","guid": "5D5A3DEA-1609-40c6-A6EF-76D069D7A764","id":"4"}
     * @type 资源类型
     * @guid 书籍的GUID
     * @id 章节的排序ID
     */
    @RequestMapping(value = "/GetFullTextInfo", method = RequestMethod.POST)
    @ApiOperation(value = "查询全文内容", notes = "输入JSONObject参数：资源类型-type，书籍ID-guid，章节排序ID-id")
    public void GetFullTextInfo(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetTopicContent(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }


    /**
     * 根据志书的分类列表
     *
     * @return 返回JSON格式字符串
     * @params 输入JSONObject，JSON格式字符串，如； {"type": "1","state":"1","cls": "3","page":"1","pageSize":"10"}
     * @type 资源类型：1 志书；2 年鉴；3 地方志；4 地情资料
     * @state 上下架状态：1，上架；0，下架
     * @id 分类号：全部：0；省志：1；市志：2；县志：3；乡村志：4
     * @page 显示页码，当前显示第几页，默认显示第一页
     * @pageSize 显示条数，当前页的显示条数，默认为是10条
     */
    @RequestMapping(value = "/GetBookList", method = RequestMethod.POST)
    @ApiOperation(value = "查询书籍的分类列表", notes = "输入JSONObject参数：资源类型-type，上下架-state，资源分类-cls，显示页码-page，每页显示数量-pageSize")
    public void GetBookList(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetBookListByCls(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 获取方志动态首页的展示信息
     *
     * @return 返回满足条件的内容
     * @params JSON字符串，如：{"num":"4"}
     * @num 需要返回记录的条数
     */
    @RequestMapping(value = "/GetDynamicHomeInfo", method = RequestMethod.POST)
    @ApiOperation(value = "获取方志动态首页显示内容", notes = "输入JSONObject参数：记录数-num")
    public void GetDynamicHomeInfo(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONArray jsonArray = kbaseTools.GetDynamicHomeInfo(params);
            printJson(ResultUtil.success(jsonArray), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }


    /**
     * 查询方志动态的标题信息
     *
     * @params: JSON字符串，如：{"state":"1", "day": "10","page": "1","pageSize": "10"}
     * @return: 输出记录元数据信息的JSON串
     * @state 通过审核的记录 （ 1，通过审核的记录；0，未审核的记录）
     * @num 近 day 天的记录
     * @page 显示页码，默认显示第 1 页
     * @pzgeSize 每一页显示记录数，默认显示 10 条
     */
    @RequestMapping(value = "/GetDynamicTitleList", method = RequestMethod.POST)
    @ApiOperation(value = "查询方志动态的标题信息", notes = "输入JSONObject参数：发布状态-state,最近天数-day,显示页数-page,显示条数-pageSize")
    public void GetDynamicTitleList(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetDynamicTitleList(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 查询方志动态详细内容
     *
     * @return 返回当前记录的元数据信息
     * @params JSON字符串，如：{"id":"192"}
     * @ID 查询记录的ID
     */
    @RequestMapping(value = "/getDynamicContent", method = RequestMethod.POST)
    @ApiOperation(value = "查询方志动态详细内容", notes = "输入JSONObject参数：记录ID-id")
    public void getDynamicContent(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetDynamicContent(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            loger.error("{}", e);
        }
    }
}
