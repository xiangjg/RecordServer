/**
 * craete by dugg 20191102
 * 执行KBase相关操作类
 */
package com.jone.record.kbase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jone.record.kbase.entity.Catalog;
import com.jone.record.kbase.util.Common;
import com.jone.record.kbase.util.DealFiles;
import com.jone.record.kbase.util.SQLBuilder;

import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class KBaseExecute {

    private static final Logger loger = LoggerFactory.getLogger(KBaseExecute.class);
    private Connection _con = null;

    public KBaseExecute() {
        if (null == _con)
            _con = KBaseCon.GetInitConnect();
    }

    public JSONObject GetBooksNums(JSONObject jsonObject) {
        JSONObject jsonObj = null;
        String strSQL = SQLBuilder.GenerateRecordNumsQuerySQL(jsonObject);
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonObj = Common.ResultSetToJSONObject(rst);
        } catch (SQLException e) {
            loger.error("{}", e);
        }
        return jsonObj;
    }

    public JSONArray GetHistoryBook(JSONObject jsonObject) {
        String strSQL = SQLBuilder.GenerateHistoryQuerySQL(jsonObject);
        if (strSQL.isEmpty()) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonArray = Common.ResultSetToJSONArray(rst);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonArray;
    }

    private JSONObject GetPagedQueryObjectInfo(String strSQL, int pageSize) {
        JSONObject titleObject = new JSONObject();
        Statement state = null;
        ResultSet rst = null;
        try {
            state = _con.createStatement();
            rst = state.executeQuery(strSQL);
            String key = "count";
            int value = 0;
            while (rst.next()) {
                value = rst.getInt("count(*)");
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

    public JSONObject GetTitleInfo(JSONObject jsonObject) {
        JSONArray jsonArray = new JSONArray();
        JSONObject titleObject = new JSONObject();

        // 获取数据库记录数SQL
        String strSQL = SQLBuilder.GeneratePagedQuerySQL(jsonObject);
        if (strSQL.isEmpty()) {
            return null;
        }
        titleObject = GetPagedQueryObjectInfo(strSQL, jsonObject.getInteger("pageSize"));

        // 获取分页查询信息SQL
        strSQL = SQLBuilder.GenerateTopicRecordInfoQuerySQL(jsonObject, titleObject.getInteger("count"));
        if (strSQL.isEmpty()) {
            return null;
        }
        Statement state = null;
        ResultSet rst = null;
        // 查询数据记录信息
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
        String strContent = null;
        List<String> list = new LinkedList<String>();
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            String strResult = "";
            String strBookGuid = "";
            while (rst.next()) {
                strResult = rst.getString("SYS_FLD_PARAXML");
                strBookGuid = rst.getString("PARENTDOI");
            }

            DealFiles dealFiles = new DealFiles();
            dealFiles.setStrServerFilePath(strBookGuid);
            dealFiles.setType(params.getIntValue("type"));
            jsonObject = dealFiles.AnalysisXmlFileInfo(strResult);

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
        JSONObject catalogObject = new JSONObject();
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

    public List<Catalog> GetReadCatalog(JSONObject jsonObject) {
        String strSQL = SQLBuilder.GenerateReadCatalogQuerySQL(jsonObject);
        if (strSQL.isEmpty()) {
            return null;
        }
        List<Catalog> catalogList = new ArrayList<Catalog>();
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            catalogList = Common.AnalysisCatalog(rst);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return catalogList;
    }


    public JSONArray GetBookListByCls(JSONObject params) {
        String strSQL = SQLBuilder.GenerateBookListQuerySQL(params);
        if (strSQL.isEmpty()) {
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
        return jsonArray;
    }


}
