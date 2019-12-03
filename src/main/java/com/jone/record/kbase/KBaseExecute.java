/**
 * craete by dugg 20191102
 * 执行KBase相关操作类
 */
package com.jone.record.kbase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jone.record.kbase.util.Common;
import com.jone.record.kbase.util.DealFiles;
import com.jone.record.kbase.util.SQLBuilder;

import org.apache.commons.collections.map.LinkedMap;
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
                            value = GetIamgeFiles(value);
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

    private String GetIamgeFiles(String value) {
        String[] strArr = value.split(";");
        String strImagePath = strArr[0];
        String urlId = strImagePath.substring(0, strImagePath.indexOf("."));
        String strSQL = String.format("select url from BINARYDATA where urlid='%s'", urlId);
        String strImageFile = "";
        ResultSet rst = null;
        PreparedStatement st = null;
        try {
            st = _con.prepareStatement(strSQL);
            rst = st.executeQuery();
            while (rst.next()) {
                strImageFile = rst.getString("url");
            }
        } catch (SQLException e) {
            loger.error("{}", e);
        } finally {
            try {
                if (null != rst) {
                    rst.close();
                }
                if (null != st) {
                    st.close();
                }
            } catch (Exception e) {
                loger.error("{}", e);
            }
        }
        return strImageFile;
    }

    /**************************************************************/
    public JSONObject GetBooksNums(JSONObject jsonObject) {
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

    public JSONArray GetHistoryBook(JSONObject jsonObject) {
        String strSQL = SQLBuilder.GenerateHistoryQuerySQL(jsonObject);
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

    private JSONObject GetPagedQueryObjectInfo(String strSQL, int pageSize) {
        JSONObject titleObject = new JSONObject(new LinkedMap());
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
        JSONArray jsonArray = new JSONArray(new LinkedList<>());
        JSONObject titleObject = new JSONObject(new LinkedMap());

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
        List<String> list = new LinkedList<String>();
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
        String strValue = "";
        try {
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            while (rst.next()) {
                strValue = rst.getString("num");
            }
            jsonObject.put("total", strValue);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        if (Integer.parseInt(strValue) <= 0) {
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


    /**
     * 生成方志动态首页显示内容
     */
    public JSONArray GetDynamicHomeInfo(JSONObject params) {
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
        return jsonObject;
    }

    /**
     * 查询方志动态的详细内容
     * */
    public JSONObject GetDynamicContent(JSONObject params) {
        String strSQL = SQLBuilder.GenerateGetDynamicContentQuerySQL(params);
        if (strSQL.isEmpty()) {
            loger.error("构建查询方志动态的详细内容SQL语句失败！");
            return null;
        }

        JSONObject jsonObject = new JSONObject(new LinkedMap());
        try{
            Statement state = _con.createStatement();
            ResultSet rst = state.executeQuery(strSQL);
            jsonObject = Common.ResultSetToJSONObject(rst);
        }catch (Exception e){
            loger.error("{}",e);
        }
        return jsonObject;
    }


}
