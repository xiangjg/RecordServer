/**
 * craete by dugg 20191102
 * 执行KBase相关操作类
 */
package com.jone.record.kbase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jone.record.kbase.entity.JournalCatalog;
import com.jone.record.kbase.util.Common;
import com.jone.record.kbase.util.DealFiles;
import com.jone.record.kbase.util.SQLBuilder;

import org.apache.commons.collections.map.LinkedMap;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class KBaseExecute {

    private static final Logger loger = LoggerFactory.getLogger(KBaseExecute.class);

    public KBaseExecute() {
        super();
    }

    private JSONArray GetResultSetToJsonObject(ResultSet rst) {
        String strContent = "";
        ResultSetMetaData metaData = null;
        int columnCount = 0;    // 获取列数
        JSONArray jsonArr = new JSONArray(new LinkedList<>());
        try {
            metaData = rst.getMetaData();
            columnCount = metaData.getColumnCount();
            while (rst.next()) {
                JSONObject jsonObj = new JSONObject(new LinkedHashMap());
                for (int i = 1; i <= columnCount; i++) {   // 遍历每一列
                    String columnName = metaData.getColumnName(i);
                    String value = rst.getString(columnName);
                    if (columnName.equals("ACCESSORIES")) {
                        columnName = "COVERPATH";
                        if (!value.isEmpty()) {
                            value = Common.GetIamgeFiles(value);
                        }
                    }
                    jsonObj.put(columnName, value);
                }
                jsonArr.add(jsonObj);
            }
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonArr;
    }

    private JSONObject GetPagedQueryObjectInfo(String strSQL, int pageSize) {
        Connection _con = KBaseCon.GetInitConnect();
        JSONObject titleObject = new JSONObject(new LinkedMap());
        Statement state = null;
        ResultSet rst = null;
        try {
            state = _con.createStatement();
            rst = state.executeQuery(strSQL);
            String key = "total";
            int value = 0;
            while (rst.next()) {
                value = rst.getInt("total");
            }
            if (0 == value) {
                return null;
            }
            titleObject.put(key, value);

            int page = 0;
            if (value <= pageSize) {
                page = 1;
            } else {
                if (value % pageSize > 0)
                    page = (value / pageSize) + 1;
                else
                    page = value / pageSize;
            }
            titleObject.put("pages", page);
        } catch (Exception e) {
            loger.error("{}", e);
        } finally {
            try {
                if (null != rst) {
                    rst.close();
                    rst = null;
                }
                if (null != state) {
                    state.close();
                    state = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return titleObject;
    }

    /**************************************************************/
    public JSONObject GetBooksNums(JSONObject jsonObject) {
        Connection _con = KBaseCon.GetInitConnect();
        JSONObject jsonObj = null;
        String strSQL = SQLBuilder.GenerateRecordNumsQuerySQL(jsonObject);
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonObj = Common.RSetToString(rst);
        } catch (SQLException e) {
            loger.error("{}", e);
        }
        return jsonObj;
    }

    public JSONObject GetHistoryBook(JSONObject params) {
        Connection _con = KBaseCon.GetInitConnect();
        String strSQL = SQLBuilder.GenerateHistoryNumsQuerySQL(params);
        if (strSQL.isEmpty()) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            int count = 0;
            while (rst.next()) {
                count = rst.getInt("total");
            }
            jsonObject.put("count", count);
        } catch (Exception e) {
            loger.error("{}", e);
        }

        strSQL = SQLBuilder.GenerateHistoryQuerySQL(params);
        if (strSQL.isEmpty()) {
            return null;
        }
        JSONArray jsonArray = new JSONArray(new LinkedList<>());
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonArray = Common.ResultSetToJSONArray(rst);
            jsonObject.put("content", jsonArray);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
    }

    public JSONObject GetTitleInfo(JSONObject params) {
        JSONArray jsonArray = new JSONArray(new LinkedList<>());
        JSONObject titleObject = new JSONObject(new LinkedMap());

        // 获取数据库记录数SQL
        String strSQL = SQLBuilder.GeneratePagedQuerySQL(params);
        if (strSQL.isEmpty()) {
            return null;
        }

        int pageSize = 0;
        if (params.containsKey("pageSize")) {
            pageSize = params.getInteger("pageSize");
        } else {
            pageSize = 10;
        }

        titleObject = GetPagedQueryObjectInfo(strSQL, pageSize);
        int total = titleObject.getInteger("total");

        // 获取分页查询信息SQL
        strSQL = SQLBuilder.GenerateTopicRecordInfoQuerySQL(params, total);
        if (strSQL.isEmpty()) {
            return null;
        }
        Statement state = null;
        ResultSet rst = null;
        // 查询数据记录信息
        Connection _con = KBaseCon.GetInitConnect();
        try {
            state = _con.createStatement();
            rst = state.executeQuery(strSQL);
            jsonArray = Common.ResultSetToJSONArray(rst);
            titleObject.put("data", jsonArray);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return titleObject;
    }

    public JSONObject GetTopicContent(JSONObject params) {
        String strSQL = SQLBuilder.GenerateTopicContentQuerySQL(params);
        if (strSQL.isEmpty()) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        List<String> list = new LinkedList<String>();
        Connection _con = KBaseCon.GetInitConnect();
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            String strResult = "";
            String strBookGuid = "";
            String strOrderId = "";
            while (rst.next()) {
                strResult = rst.getString("SYS_FLD_PARAXML");
                strBookGuid = rst.getString("PARENTDOI");
                strOrderId = rst.getString("SYS_FLD_ORDERNUM");
            }
            DealFiles dealFiles = new DealFiles();
            dealFiles.setStrServerFilePath(strBookGuid);
            dealFiles.setType(params.getIntValue("type"));
            jsonObject = dealFiles.AnalysisXmlFileInfo(strResult);
            jsonObject.put("guid", strBookGuid);
            jsonObject.put("id", strOrderId);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
    }

    public JSONObject GetBookCatalog(JSONObject params) {
        String strSQL = SQLBuilder.GenerateBookCatalogQuerySQL(params);
        if (strSQL.isEmpty()) {
            return null;
        }
        JSONObject catalogObject = new JSONObject(new LinkedMap());
        Connection _con = KBaseCon.GetInitConnect();
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            String strResult = "";
            String strField = "SYS_FLD_CATALOG";
            while (rst.next()) {
                strResult = rst.getString(strField);
            }
            String strContent = XML.toJSONObject(strResult).toString();
            catalogObject = JSONObject.parseObject(strContent);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return catalogObject;
    }

    public JSONObject GetReadCatalog(JSONObject jsonObject) {
        String strSQL = SQLBuilder.GenerateReadCatalogQuerySQL(jsonObject);
        if (strSQL.isEmpty()) {
            return null;
        }
        JSONObject jsonObj = new JSONObject(new LinkedMap());
        Connection _con = KBaseCon.GetInitConnect();
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonObj = Common.AnalysisCatalog(rst);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObj;
    }

    public JSONObject GetBookListByCls(JSONObject params) {
        JSONObject jsonObject = new JSONObject(new LinkedMap());
        jsonObject.put("pageIndex", params.getString("page"));
        String strSQL = SQLBuilder.GenerateRecordNumsQuerySQL(params);
        // 查询并添加记录总数
        int count = 0;
        Connection _con = KBaseCon.GetInitConnect();
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            while (rst.next()) {
                count = rst.getInt("total");
            }
            jsonObject.put("count", count);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        if (count <= 0) {
            String strErr = String.format("记录数为0");
            loger.error("{}", strErr);
        }
        // 查询详细的内容信息
        strSQL = SQLBuilder.GenerateBookListQuerySQL(params);
        if (strSQL.isEmpty()) {
            return null;
        }
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            JSONArray jsonArray = Common.ResultSetToJSONArray(rst);
            jsonObject.put("content", jsonArray);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
    }

    public JSONArray GetDynamicHomeInfo(JSONObject params) {
        Connection _con = KBaseCon.GetInitConnect();
        String strSQL = SQLBuilder.GenerateDynamicHomeInfoQuerySQL(params);
        if (strSQL.isEmpty()) {
            loger.error("生成SQL查询语句失败！");
            return null;
        }
        JSONArray jsonArray = new JSONArray(new LinkedList<>());
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonArray = GetResultSetToJsonObject(rst);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonArray;
    }

    public JSONObject GetDynamicTitleList(JSONObject params) {
        Connection _con = KBaseCon.GetInitConnect();
        JSONObject jsonObject = new JSONObject(new LinkedMap());
        jsonObject.put("pageIndex", params.getString("page"));

        String strSQL = SQLBuilder.GenerateGetDynamicCountQuery(params);
        if (strSQL.isEmpty()) {
            loger.error("SQL查询语句构建失败！");
            return null;
        }
        int total = 0;
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            while (rst.next()) {
                total = rst.getInt("total");
            }
        } catch (Exception e) {
            loger.error("{}", e);
        }
        if (total <= 0) {
            loger.error("满足查询条件的记录不存在！");
            return null;
        }
        jsonObject.put("count", total);

        strSQL = SQLBuilder.GenerateGetDynamicListQuerySQL(params, total);
        if (strSQL.isEmpty()) {
            loger.error("SQL查询语句构建失败！");
            return null;
        }

        JSONArray jsonArray = new JSONArray(new LinkedList<>());
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonArray = Common.ResultSetToJSONArray(rst);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        jsonObject.put("content", jsonArray);
        return jsonObject;
    }

    public JSONObject GetDynamicContent(JSONObject params) {
        Connection _con = KBaseCon.GetInitConnect();
        String strSQL = SQLBuilder.GenerateGetDynamicContentQuerySQL(params);
        if (strSQL.isEmpty()) {
            loger.error("构建查询方志动态的详细内容SQL语句失败！");
            return null;
        }

        JSONObject jsonObject = new JSONObject(new LinkedMap());
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonObject = Common.ResultSetToJSONObject(rst);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
    }

    public JSONObject GetYearBookList(JSONObject params) {
        String strSQL = SQLBuilder.GenerateGetYearBookListNumsQuerySQL(params);
        if (strSQL.isEmpty()) {
            loger.error("构建查询年鉴列表数量 SQL 语句失败！");
            return null;
        }
        Connection _con = KBaseCon.GetInitConnect();
        int count = 0;
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            while (rst.next()) {
                count = rst.getInt("NUM");
            }
        } catch (Exception e) {
            loger.error("{}", e);
        }

        // 查询年份范围
        List<String> strYearList = new LinkedList<String>();
        if (params.containsKey("year")) {
            String strYear = params.getString("year");
            if (strYear.isEmpty()) {
                strSQL = SQLBuilder.GenerateGetYearBookTimeAroundQuerySQL();
                if (strSQL.isEmpty()) {
                    loger.error("构建查询年鉴列表时间范围 SQL 语句失败！");
                    return null;
                }
                try {
                    Statement state = _con.createStatement();
                    ResultSet rst = state.executeQuery(strSQL);
                    strYearList = Common.AnalysisYearAround(rst);
                } catch (Exception e) {
                    loger.error("{}", e);
                }
            }
        }
        // 查询书籍内容
        strSQL = SQLBuilder.GenerateGetYearBookListQuerySQL(params, count);
        if (strSQL.isEmpty()) {
            loger.error("构建查询年鉴列表 SQL 语句失败！");
            return null;
        }

        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonObject = Common.YearBookListToJSONObject(rst);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        jsonObject.put("count", count);
        jsonObject.put("timeAround", strYearList);
        return jsonObject;
    }


    public JSONObject GetSingleBookInfo(JSONObject param) {
        Connection _con = KBaseCon.GetInitConnect();
        String strSQL = SQLBuilder.GenerateSingleBookInfoQuerySQL(param);
        if (strSQL.isEmpty()) {
            loger.error("构建查询单本书籍基本信息SQL语句失败！");
            return null;
        }
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonObject = Common.ResultSetToJSONObject(rst);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
    }

    public JSONObject GetJournalInfo(JSONObject params) {
        Connection _con = KBaseCon.GetInitConnect();
        // 查询满足条件的记录数
        String strSQL = SQLBuilder.GenerateJournalInfoNumsQuerySQL(params);
        if (strSQL.isEmpty()) {
            loger.error("构建查询单本书籍基本信息SQL语句失败！");
            return null;
        }
        int count = 0;
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            while (rst.next()) {
                count = rst.getInt("total");
            }
            jsonObject.put("count", count);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        // 查询满足条件的期刊信息
        strSQL = SQLBuilder.GenerateJournalInfoQuerySQL(params, count);
        try {
            JSONArray jsonArray = new JSONArray(new LinkedList<>());
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonArray = Common.ResultSetToJSONArray(rst);
            jsonObject.put("content", jsonArray);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
    }

    public JSONObject GetJournalYearInfo(JSONObject params) {
        Connection _con = KBaseCon.GetInitConnect();
        // 查询满足条件的总记录数
        String strSQL = SQLBuilder.GenerateGetJournalYearInfoNumsQuerySQL(params);
        if (strSQL.isEmpty()) {
            loger.error("构建查询单本书籍基本信息SQL语句失败！");
            return null;
        }
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        int count = 0;
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            while (rst.next()) {
                count = rst.getInt("total");
            }
            jsonObject.put("count", count);
        } catch (Exception e) {
            loger.error("{}", e);
        }

        // 查询年份范围
        List<String> strYearList = new LinkedList<String>();
        if (params.containsKey("year")) {
            String strYear = params.getString("year");
            if (strYear.isEmpty()) {
                strSQL = SQLBuilder.GenerateGetJournalYearInfoTimeAroundQuerySQL(params.getString("code"));
                if (strSQL.isEmpty()) {
                    loger.error("构建查询期刊列表时间范围 SQL 语句失败！");
                    return null;
                }
                try {
                    String strYearInfo = "";
                    Statement state = _con.createStatement();
                    ResultSet rst = state.executeQuery(strSQL);
                    while (rst.next()) {
                        strYearInfo = rst.getString("year");
                        strYearList.add(strYearInfo);
                    }

                } catch (Exception e) {
                    loger.error("{}", e);
                }
            }
        }
        jsonObject.put("timeAround", strYearList);
        // 查询期刊内容信息
        strSQL = SQLBuilder.GenerateGetJournalYearInfoQuerySQL(params, count);
        if (strSQL.isEmpty()) {
            loger.error("构建查询单本书籍基本信息SQL语句失败！");
            return null;
        }
        try {
            JSONArray jsonArray = new JSONArray(new LinkedList<>());
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonArray = Common.ResultSetToJSONArray(rst);
            jsonObject.put("content", jsonArray);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
    }

    public JSONObject GetJournalBaseInfo(JSONObject params) {
        Connection _con = KBaseCon.GetInitConnect();
        String strSQL = SQLBuilder.GenerateJournalBaseInfoQuerySQL(params);
        if (strSQL.isEmpty()) {
            loger.error("构建查询单期期刊基本信息SQL语句失败！");
            return null;
        }
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonObject = Common.ResultSetToJSONObject(rst);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
    }


    public JSONObject GetJournalBaseCatalog(JSONObject params) {
        Connection _con = KBaseCon.GetInitConnect();
        String strSQL = SQLBuilder.GenerateJournalBaseCatalogQuerySQL(params);
        if (strSQL.isEmpty()) {
            loger.error("构建查询单期期刊基本目录信息SQL语句失败！");
            return null;
        }
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            String strCatalog = "";
            while (rst.next()) {
                strCatalog = rst.getString("SYS_FLD_CATALOG");
            }
            String strContent = XML.toJSONObject(strCatalog).toString();
            jsonObject = JSONObject.parseObject(strContent);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
    }


    public JSONArray GetJournalReadCatalog(JSONObject params) {
        String strSQL = SQLBuilder.GenerateJournalReadCatalogQuerySQL(params);
        if (strSQL.isEmpty()) {
            loger.error("构建查询单期期刊阅读目录信息SQL语句失败！");
            return null;
        }
        JSONArray jsonArray = new JSONArray(new LinkedList<>());
        Connection _con = KBaseCon.GetInitConnect();
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonArray = Common.ResultSetToJSONArray(rst);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonArray;
    }

    /**
     * 查询期刊文章全文信息
     */
    public JSONObject GetJournalFullText(JSONObject params) {
        String strSQL = SQLBuilder.GenerateJournalFullTextQuerySQL(params);
        if (strSQL.isEmpty()) {
            loger.error("构建查询期刊文章全文信息SQL语句失败！");
            return null;
        }
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        Connection _con = KBaseCon.GetInitConnect();
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonObject = Common.AnalysisJournalFullText(rst);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
    }

    /**
     * 查询书籍章节目录信息
     */
    public JSONObject GetBookChapterInfo(JSONObject params) {
        Connection _con = KBaseCon.GetInitConnect();
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        String strSQL = SQLBuilder.GenerateBookChapterInfoGroupQuerySQL(params);
        if (strSQL.isEmpty()) {
            loger.error("构建询书籍章节目录信息SQL语句失败！");
            return null;
        }

        // 查询满足条件的目录列表
        List<JournalCatalog> list = new LinkedList<JournalCatalog>();
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            list = Common.ResultSetToList(rst);
        } catch (Exception e) {
            loger.error("{}", e);
        }

        // 将满足条件的目录列表进行分组
        Map<String, List<JournalCatalog>> map = new LinkedHashMap<>();
        map = list.stream().collect(Collectors.groupingBy(JournalCatalog::getParentGuid));
        // 添加书籍的本书，分页总记录数
        int mapSize = map.size();
        jsonObject.put("count", mapSize);

        // 将Map转换成List
        List<Object> ObjectList = Common.MapToJSONObjectList(map);

        // 计算分页详情
        int start = 0, end = 0;
        int pageSize = 0, page = 0;
        if (params.containsKey("pageSize")) {
            pageSize = params.getInteger("pageSize");
        } else {
            pageSize = 10;
        }
        if (params.containsKey("page")) {
            page = params.getInteger("page");
        } else {
            page = 1;
        }
        start = (page - 1) * pageSize;
        if (mapSize <= pageSize) {
            end = mapSize;
        } else {
            end = page * pageSize;
            if (end > mapSize)
                end = mapSize;
        }
        List<Object> jsonObjectList = ObjectList.subList(start, end);
        jsonObject.put("content", jsonObjectList);
        return jsonObject;
    }

    public JSONObject GetSingleBookFullTextQuery(JSONObject params) {
        Connection _con = KBaseCon.GetInitConnect();
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        String strSQL = SQLBuilder.GenerateSingleBookFullTextNumsQuerySQL(params);
        if (strSQL.isEmpty()) {
            loger.error("构建询书籍章节目录信息SQL语句失败！");
            return null;
        }
        int count = 0;
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            while (rst.next()) {
                count = rst.getInt("total");
            }
            jsonObject.put("count", count);
        } catch (Exception e) {
            loger.error("{}", e);
        }

        strSQL = SQLBuilder.GenerateSingleBookFullTextQuerySQL(params, count);
        if (strSQL.isEmpty()) {
            loger.error("构建询书籍章节目录信息SQL语句失败！");
            return null;
        }
        JSONArray jsonArray = new JSONArray(new LinkedList<>());
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonArray = Common.ResultSetToJSONArray(rst);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        jsonObject.put("content", jsonArray);
        return jsonObject;
    }

    public JSONObject GetChronicleEvents(JSONObject params) {
        Connection _con = KBaseCon.GetInitConnect();
        String strSQL = SQLBuilder.GenerateChronicleEventsNumsQuerySQL(params);
        if (strSQL.isEmpty()) {
            loger.error("构建查询大事记列表信息数量SQL语句失败！");
            return null;
        }
        int count = 0;
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            while (rst.next()) {
                count = rst.getInt("total");
            }
            jsonObject.put("count", count);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        // 构建分页查询
        strSQL = SQLBuilder.GenerateChronicleEventsQuerySQL(params, count);
        if (strSQL.isEmpty()) {
            loger.error("构建查询大事记列表信息SQL语句失败！");
            return null;
        }
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            JSONArray jsonArray = new JSONArray(new LinkedList<>());
            jsonArray = Common.ResultSetToJSONArray(rst);
            jsonObject.put("content", jsonArray);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
    }

}
