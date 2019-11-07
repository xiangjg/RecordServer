
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
import org.springframework.web.bind.annotation.*;
import com.jone.record.kbase.KBaseExecute;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/books")
@Api(tags = "方志馆")
public class kbaseController extends BaseController {

    private KBaseExecute kbaseTools = null;

    public kbaseController() {
        if (null == kbaseTools) {
            kbaseTools = new KBaseExecute ();
        }
    }

    /**
     * 获取数据库的记录数
     *
     * @return 返回JSON格式字符串
     * @params 输入JSONObject对象
     * 输入参数示例：{"type":"1","cls":"0"}
     * 资源类型，type：1，志书；2，年鉴；3，地方史；4，地情资料。
     * 分类代码，cls：0，全部；1，志书；2，年鉴，3：地方史；4，地情资料
     */
    @RequestMapping(value = "/GetBooksNums", method = RequestMethod.GET)  // , produces = "application/json;charset=UTF-8"
    @ApiOperation(value = "获取数据库的记录数", notes = "输入JSONObject参数：资源类型type，分类类型cls。")
    public void GetBooksNums(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONObject jsonObject = kbaseTools.GetBooksNums (params);
            printJson (ResultUtil.success (jsonObject), response);
        } catch (Exception e) {
            logger.error ("{}", e);
            printJson (ResultUtil.error (-1, e.getMessage ()), response);
        }
    }

    /**
     * 获取志书列表
     *
     * @return 返回JSON格式字符串
     * @params 输入JSONObject对象
     * 输入参数示例：{"type":"1","state":"0","page":"1","pageSize":"10","title":"贵州省"}
     * 资源类型，type：1，志书；2，年鉴；3，地方史；4，地情资料;
     * 上下状态，state：0，下架；1，上架；
     * 显示页码，page：分页查询，当前显示第几页，默认显示第 1 页；
     * 每页显示，pageSize：每页显示记录数，默认为 10 条；
     * 检索标题，title：根据书名进行检索。
     */
    @RequestMapping(value = "/GetHistoryBook", method = RequestMethod.GET) // , produces = "application/json;charset=UTF-8"
    @ApiOperation(value = "获取书籍中列表信息", notes = "输入JSONObject参数：资源类型type，上下架state，显示页码page，每页显示数量pageSize,检索标题title")
    public void GetHistoryBook(@RequestBody JSONObject params, HttpServletResponse response) {
        try {
            JSONArray jsonArray = kbaseTools.GetHistoryBook (params);
            printJson (ResultUtil.success (jsonArray), response);
        } catch (Exception e) {
            logger.error ("{}", e);
            printJson (ResultUtil.error (-1, e.getMessage ()), response);
        }
    }

    /**
     * 更新数据库相关记录
     *
     @RequestMapping(value = "/Update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
     public int Update(String params) {
     return kbaseTools.ExecuteUpdateBySQL (params);
     }

     /**
      * 插入数据库记录
     *
     @RequestMapping(value = "/Insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
     public int InsertRecord(String params) {
     return kbaseTools.ExecuteInsertBySQL (params);
     }

     /**
      * 删除数据库相关记录
     *
     @RequestMapping(value = "/Delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
     public int DeleteRecord(String params) {
     return kbaseTools.ExecuteDeleteBySQL (params);
     }
     */
}
