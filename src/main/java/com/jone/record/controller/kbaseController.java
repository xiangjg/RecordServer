
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


/**
 * 资源类型约定
 * 志书   1
 * 年鉴   2
 * 期刊   3
 * 地情资料     4
 * 多媒体       5
 * 后续有资源往后追加
 */


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
     * 资源类型，type：志书 1,年鉴 2,期刊 3,地情资料 4,多媒体 5。
     * 分类代码，cls：0，志书 1,年鉴 2,期刊 3,地情资料 4,多媒体 5
     */
    @RequestMapping(value = "/GetBooksNums", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
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
     * 输入参数示例：{"type":"1","state":"1","page":"1","pageSize":"10","keyword":"贵州省*"}
     * 资源类型，type：志书 1,年鉴 2,期刊 3,地情资料 4,多媒体 5。
     * 上下状态，state：0，下架；1，上架；
     * 显示页码，page：分页查询，当前显示第几页，默认显示第 1 页；
     * 每页显示，pageSize：每页显示记录数，默认为 10 条；
     * 检索标题，title：根据书名进行检索。
     */
    @RequestMapping(value = "/GetBooksInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "根据标题获取书籍列表信息", notes = "输入JSONObject参数：资源类型-type,上下架-state,检索标题-keyword,显示页码-page,每页显示数量-pageSize")
    public void GetBooksInfo(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetBooksInfo(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            logger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 获取书籍碎片化阅化阅读时的目录信息
     *
     * @return 返回JSON格式的字符串
     * @params 输入JSONObject对象，格式为
     * {"type": "1","id": "1C220505-E8B6-4205-8F3A-22168B707488"},
     * {"type": "2","id": "30C7D25D-124F-47a6-8EE5-F4EBB12FE1C3"},
     * {"type":"3","code":"ydfz","id":"A02BE975-2543-4a13-B5AF-EFD6DB199685"},
     * {"type": "4","id": "03F3AB45-C7C3-4135-87E7-FD770F4A77A9"},
     * @type 资源类型：志书 1,年鉴 2,期刊 3,地情资料 4,多媒体 5。
     * @id 书籍的为唯一标识符，为GUID格式
     */
    @RequestMapping(value = "/GetReadCatalog", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
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
     * @params 输入JSONObject对象，
     * {"type": "1","id": "5D5A3DEA-1609-40c6-A6EF-76D069D7A764"},
     * {"type": "2","id": "30C7D25D-124F-47a6-8EE5-F4EBB12FE1C3"}
     * {"type": "3","code":"ydfz","id":"A02BE975-2543-4a13-B5AF-EFD6DB199685"}
     * {"type": "4","id": "03F3AB45-C7C3-4135-87E7-FD770F4A77A9"},
     * @type 资源类型：志书 1,年鉴 2,期刊 3,地情资料 4,多媒体 5。
     * @id 书籍的为唯一标识符，为GUID格式
     */
    @RequestMapping(value = "/GetBookCatalog", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
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
     * @type 资源类型，志书 1,年鉴 2,期刊 3,地情资料 4,多媒体 5
     * @id id，传入书籍的GUID值，如果为空，则在整个章节表中查找相关数据
     * @keyword 传入的检索关键词
     * @page 显示页码，当前显示第几页，默认显示第一页
     * @pageSize 显示条数，当前页的显示条数，默认为是10条
     * @params 输入JSONObject对象，格式为 {"type":"1", "id":"5D5A3DEA-1609-40c6-A6EF-76D069D7A764","keyword":"地质构造","page":"1","pageSize":"10"}
     */
    @RequestMapping(value = "/GetTitleInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
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
     * @params 输入JSONObject，JSON格式字符串，如；
     * {"type": "1","guid": "0F18B68A-A0BA-4d51-906D-A3DD64006FE4","id":"4"}
     * {"type": "2","guid": "985ECE7E-39BC-4535-BE90-3E765CE56A3F","id":"52"}
     * {"type": "3","code": "GYWE", "id": "bb6b5b1a-749f-4fbd-96b7-9faa97446eff"}
     * {"type": "4","guid": "4D7EB3D5-CBBB-4561-9867-0E2706B8E174","id":"7"}
     * @type 资源类型 志书 1,年鉴 2,期刊 3,地情资料 4,多媒体 5。
     * @guid 书籍的GUID
     * @id 章节的排序ID
     */
    @RequestMapping(value = "/GetFullTextInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "查询全文内容", notes = "输入JSONObject参数：资源类型-type，书籍ID-guid，章节排序ID-id")
    public void GetFullTextInfo(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetFullTextInfo(params);
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
     * @type 资源类型：志书 1,年鉴 2,期刊 3,地情资料 4,多媒体 5。
     * @state 上下架状态：1，上架；0，下架
     * @id 分类号：全部：0；省志：1；市志：2；县志：3；乡村志：4
     * @page 显示页码，当前显示第几页，默认显示第一页
     * @pageSize 显示条数，当前页的显示条数，默认为是10条
     */
    @RequestMapping(value = "/GetBookList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "查询书籍的分类列表", notes = "输入JSONObject参数：资源类型-type，上下架-state，资源分类-cls，显示页码-page，每页显示数量-pageSize")
    public void GetBookList(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetBookList(params);
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
    @RequestMapping(value = "/GetDynamicHomeInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
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
    @RequestMapping(value = "/GetDynamicTitleList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
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
    @RequestMapping(value = "/GetDynamicContent", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "查询方志动态详细内容", notes = "输入JSONObject参数：记录ID-id")
    public void GetDynamicContent(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetDynamicContent(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            loger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 查询年鉴列表
     *
     * @return 返回JSON格式字符串
     * @params 输入JSONObject对象，如：
     * {"type":"2","code":"","year":"2008","page":"1","pageSize":"10"}      // 年鉴
     * {"type":"3","code":"zdfz","year":"","page":"1","pageSize":"10"}      // 期刊
     * @typ 资源类型   年鉴 2；期刊 3。
     * @code 资源代码，期刊必填，如："zdfz"，年鉴为空值
     * @year 输入查询年限
     * @page 查询页索引，默认查询第 1 页
     * @pageSize 每页显示数据条数，默认显示 1 条
     */
    @RequestMapping(value = "GetYearBookList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "查询年鉴列表信息", notes = "输入JSONObject对象，资源类型-type，期刊代码-code（年鉴为空），查询年份-year，查询页索引-page，每页显示数据条数-pageSize")
    public void GetYearBookList(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetYearListInfo(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            loger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 查询单本书籍的信息
     *
     * @return 返回书籍的基本信息
     * @params 输入JSONObject对象，如：{"type":"1","id":"2811FAE6-17BF-48e8-B1A4-A93A525F5B04","keyword":"贵州"}
     * @type 书籍类型：志书 1,年鉴 2,期刊 3,地情资料 4,多媒体 5
     * @id 书籍的GUID，为GUID格式的字符串，"2811FAE6-17BF-48e8-B1A4-A93A525F5B04"
     * @keyword 查询关键词，可以为空
     */
    @RequestMapping(value = "GetSingleBookInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "查询单本书籍的信息", notes = "输入JSONObject对象，资源类型-type，书籍的GUID-id")
    public void GetSingleBookInfo(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetSingleBookInfo(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            loger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 查询期刊基本信息
     *
     * @return 返回JSON格式字符串
     * @params 输入JSONObject对象，格式为 {"keyword":"贵州文史","page":"1","pageSize":"10"}
     * @keyword 查询的关键词
     * @page 显示页索引
     * @pageSize 每页显示记录数
     */
    @RequestMapping(value = "GetJournalInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "查询期刊基本信息", notes = "输入JSONObject对象，查询关键词-keyword，显示页索引-page，每页显示记录数-pageSize")
    public void GetJournalInfo(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetJournalInfo(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            loger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 查询期刊的年期信息
     *
     * @params 输入JSONObject对象，格式为 {"code":"zdfz","year":"","page":"1","pageSize":"10"}
     * @id 期刊ID号
     * @year 期刊年份
     * page 查询页索引，默认查询第 1 页
     * @pageSize 每页显示记录数，默认显示 10 条
     */
    @RequestMapping(value = "GetJournalYearInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "查询期刊的年期信息", notes = "输入JSONObject对象，期刊号-code，期刊年份-year，查询页索引-page，每页显示记录数-pageSize")
    public void GetJournalYearInfo(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetJournalYearInfo(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            loger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 查询单期期刊的基本信息
     *
     * @return 返回期刊的基本信息，为JSON格式字符串
     * @params 输入JSONObject对象，格式为 {"code":"ydfz","id":"A02BE975-2543-4a13-B5AF-EFD6DB199685"}
     * @code 期刊号, 如："zdfz"
     * @id 期刊的GUID，如："A02BE975-2543-4a13-B5AF-EFD6DB199685"
     */
    @RequestMapping(value = "GetJournalBaseInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "查询单期期刊的基本信息", notes = "输入JSONObject对象，期刊号-code，期刊的GUID-id")
    public void GetJournalBaseInfo(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetJournalBaseInfo(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            loger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 查询单期期刊的基本目录信息
     *
     * @return 返回期刊的基本信息，为JSON格式字符串
     * @params 输入JSONObject对象，格式为 {"code":"ydfz","id":"A02BE975-2543-4a13-B5AF-EFD6DB199685"}
     * @code 期刊号, 如："zdfz"
     * @id 期刊的GUID，如："A02BE975-2543-4a13-B5AF-EFD6DB199685"
     */
    @RequestMapping(value = "GetJournalBaseCatalog", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "查询单期期刊的基本目录信息", notes = "输入JSONObject对象，期刊号-code，期刊的GUID-id")
    public void GetJournalBaseCatalog(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetJournalBaseCatalog(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            loger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 查询单期期刊的阅读目录信息
     *
     * @return 返回期刊的基本信息，为JSON格式字符串
     * @params 输入JSONObject对象，格式为 {"code":"ydfz","id":"A02BE975-2543-4a13-B5AF-EFD6DB199685"}
     * @code 期刊号, 如："zdfz"
     * @id 期刊的GUID，如："A02BE975-2543-4a13-B5AF-EFD6DB199685"
     */
    @RequestMapping(value = "GetJournalReadCatalog", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "查询单期期刊的阅读目录信息", notes = "输入JSONObject对象，期刊号-code，期刊的GUID-id")
    public void GetJournalReadCatalog(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetJournalReadCatalog(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            loger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 查询期刊文章全文
     *
     * @return 返回期刊的基本信息，为JSON格式字符串
     * @params 输入JSONObject对象，格式为 {"id": "bb6b5b1a-749f-4fbd-96b7-9faa97446eff","code": "GYWE"}
     * @code 期刊号, 如："GYWE"
     * @id 期刊的GUID，如："bb6b5b1a-749f-4fbd-96b7-9faa97446eff"
     */
    @RequestMapping(value = "GetJournalFullText", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "查询期刊文章全文", notes = "输入JSONObject对象，期刊号-code，期刊的GUID-id")
    public void GetJournalFullText(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetJournalFullText(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            loger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 查询书籍的目录信息
     *
     * @return 返回JSON格式字符串
     * @params 输入JSONObject对象，格式为 {"type":"1","keyword":"贵州","page":"1","pageSize":"10"}
     * @type 资源类型，志书-1,年鉴-2,期刊-3(不在此范围内),地情资料-4,多媒体-5
     * @keyword 检索关键词
     * @page 显示页索引，默认为显示第 1 页
     * @pageSize 每页显示记录数，默认为 10 条
     */
    @RequestMapping(value = "GetBookChapterInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "查询书籍的目录信息", notes = "输入JSONObject对象,资源类型-type,检索关键词-keyword,显示页索引-page,每页显示记录数-pageSize")
    public void GetBookChapterInfo(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetBookChapterInfo(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            loger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 书籍的目录检索
     *
     * @return 返回JSON格式字符串
     * @params 输入JSONObject对象，格式为
     * {"type":"1","id":"D11B35A9-9665-44b1-A27A-189AE27071FB","keyword":"贵州","page":"10","pageSize":"10"}
     * {"type":"2","id":"985ECE7E-39BC-4535-BE90-3E765CE56A3F","keyword":"贵州","page":"1","pageSize":"10"}
     * @type 资源类型 志书 1,年鉴 2,期刊 3,地情资料 4,多媒体 5。
     * @id 书籍的GUID
     * @keyword 检索关键词
     * @page 显示页索引，默认为显示第 1 页
     * @pageSize 每页显示记录数，默认为 10 条
     */
    @RequestMapping(value = "GetBookListCatalog", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "书籍的目录检索", notes = "输入JSONObject对象,资源类型-type,书籍的GUID-id,检索关键词-keyword,显示页索引-page,每页显示记录数-pageSize")
    public void GetBookListCatalog(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetBookListCatalog(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            loger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 查询大事记信息列表
     *
     * @return 返回JSON格式的字符串
     * @params 输入JSONObject对象，格式为 {"startTime":"2015-01-01","endTime":"2015-09-31","page":"1","pageSize":"10"}
     * @startTime 起始时间
     * @endTime 终止时间
     * @page 显示页索引，默认为显示第 1 页
     * @pageSize 每页显示记录数，默认为 10 条
     */
    @RequestMapping(value = "GetChronicleEvents", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "查询大事记信息列表", notes = "输入JSONObject对象,起始时间-startTime,终止时间-endTime,显示页索引-page,每页显示记录数-pageSize")
    public void GetChronicleEvents(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetChronicleEvents(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            loger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

    /**
     * 单本书全文检索
     *
     * @return 返回JSON格式的字符串
     * @params 输入JSONObject对象，格式为 {"type":"1","id":"2E2B7D0D-0AD0-44ea-A4B4-97487D96C101","keyword":"贵阳","page":"1","pageSize":"10"}
     * @type 资源类型，志书 1,年鉴 2,期刊 3,地情资料 4,多媒体 5。
     * @id 书籍GUID
     * @keyword 关键词
     * @page 显示页索引，默认为显示第 1 页
     * @pageSize 每页显示记录数，默认为 10 条
     */
    @RequestMapping(value = "QueryFullText", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiOperation(value = "单本书全文检索", notes = "输入JSONObject对象,资源类型-type,书籍GUID-id,关键词-keyword,显示页索引-page,每页显示记录数-pageSize")
    public void QueryFullText(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.QueryFullText(params);
            printJson(ResultUtil.success(jsonObject), response);
        } catch (Exception e) {
            loger.error("{}", e);
            printJson(ResultUtil.error(-1, e.getMessage()), response);
        }
    }

}