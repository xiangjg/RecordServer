/**
 * craete by dugg 20191102
 * 执行KBase相关操作类
 */
package com.jone.record.kbase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jone.record.kbase.entity.Catalog;
import com.jone.record.kbase.util.Common;
import com.jone.record.kbase.util.SQLBuilder;

import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.json.XML.toJSONObject;

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

    public JSONObject GetBookCatalog(JSONObject jsonObject) {
        String strSQL = SQLBuilder.GenerateBookCatalogQuerySQL(jsonObject);
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


    public String ResultSetToString(ResultSet rst) {
        String strContent = "";
        try {
            ResultSetMetaData rsMetaData = rst.getMetaData();
            int column = rsMetaData.getColumnCount();
            String strKey = "";
            String strValue = "";
            JSONArray jsonArray = new JSONArray();
            while (rst.next()) {
                JSONObject jsonObject = new JSONObject();
                for (int i = 1; i <= column; i++) {
                    strKey = rsMetaData.getColumnName(i);
                    strValue = rst.getString(strKey);
                    jsonObject.put(strKey, strValue);
                }
                jsonArray.add(jsonObject);
            }
            strContent = jsonArray.toString();
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return strContent;
    }
}
