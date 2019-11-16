/**
 * craete by dugg 20191102
 * 执行KBase相关操作类
 */
package com.jone.record.kbase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jone.record.kbase.entity.Catalog;
import com.jone.record.kbase.util.Common;
import com.jone.record.kbase.util.SQLBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KBaseExecute {

    private static final Logger loger = LoggerFactory.getLogger (KBaseExecute.class);

    private Connection _con = null;

    public KBaseExecute() {
        if (null == _con)
            _con = KBaseCon.GetInitConnect ();
    }

    public JSONObject GetBooksNums(JSONObject jsonObject) {
        JSONObject jsonObj = null;
        String strSQL = SQLBuilder.GenerateRecordNumsQuerySQL (jsonObject);
        try {
            Statement state = _con.createStatement ();
            ResultSet rst = state.executeQuery (strSQL);
            jsonObj = Common.ResultSetToJSONObject (rst);
        } catch (SQLException e) {
            loger.error ("{}", e);
        }
        return jsonObj;
    }

    public JSONArray GetHistoryBook(JSONObject jsonObject) {
        String strSQL = SQLBuilder.GenerateHistoryQuerySQL (jsonObject);
        if (strSQL.isEmpty ())
        {
            return  null;
        }

        JSONArray jsonArray = new JSONArray ();
        try {
            Statement state = _con.createStatement ();
            ResultSet rst = state.executeQuery (strSQL);
            jsonArray = Common.ResultSetToJSONArray (rst);
        } catch (Exception e) {
            loger.error ("{}", e);
        }
        return jsonArray;
    }

    public List<Catalog> GetReadCatalog(JSONObject jsonObject){
        String strSQL = SQLBuilder.GenerateReadCatalogQuerySQL (jsonObject);
        if (strSQL.isEmpty ())
        {
            return  null;
        }
        List<Catalog> catalogList = new ArrayList<Catalog> ();
        try{
            Statement state = _con.createStatement ();
            ResultSet rst = state.executeQuery (strSQL);
            catalogList = Common.AnalysisCatalog(rst);
        }catch (Exception e){
            loger.error ("{}", e);
        }
        return catalogList;
    }


    public String ResultSetToString(ResultSet rst) {
        String strContent = "";
        try {
            ResultSetMetaData rsMetaData = rst.getMetaData ();
            int column = rsMetaData.getColumnCount ();
            String strKey = "";
            String strValue = "";
            JSONArray jsonArray = new JSONArray ();
            while (rst.next ()) {
                JSONObject jsonObject = new JSONObject ();
                jsonObject.clear ();
                for (int i = 1; i <= column; i++) {
                    strKey = rsMetaData.getColumnName (i);
                    strValue = rst.getString (strKey);
                    jsonObject.put (strKey, strValue);
                }
                jsonArray.add (jsonObject);
            }
            strContent = jsonArray.toString ();
        } catch (Exception e) {
            loger.error ("{}", e);
        }
        return strContent;
    }


    /**
     * 根据数据库分类代码获取记录数
     *
    public String GetRecordNums(String params) {
        Map<String, String> paramMap = Common.AnalysisParams (params);
        String strSQL = SQLBuilder.GenerateRecordNumsSQL (paramMap);
        String strContent = "";
        try {
            Statement state = _con.createStatement ();
            ResultSet rst = state.executeQuery (strSQL);
            strContent = Common.RSetToString (rst);
        } catch (SQLException e) {
            loger.error ("{}", e);
        }
        return strContent;
    }

    /**
     * 根据SQL语句查询结果集
     *
    public String ExecuteQueryBySQL(String params) {
        Map<String, String> paramMap = Common.AnalysisParams (params);
        String strSQL = SQLBuilder.GenerateQuerySQLByParams (paramMap);
        String strContent = "";
        try {
            Statement state = _con.createStatement ();
            ResultSet rst = state.executeQuery (strSQL);
            strContent = Common.ResultSetToString (rst);
        } catch (SQLException e) {
            loger.error ("{}", e);
        }
        return strContent;
    }

    /**
     * 根据SQL语句更新数据记录集
     *
    public int ExecuteUpdateBySQL(String params) {
        Map<String, String> paramMap = Common.AnalysisParams (params);
        String strSQL = SQLBuilder.GenerateUpdateSQLByParams (paramMap);
        int id = 0;
        try {
            Statement state = _con.createStatement ();
            id = state.executeUpdate (strSQL);
        } catch (SQLException e) {
            loger.error ("{}", e);
        }
        return id;
    }

    /**
     * 根据SQL语句执行数据插入操作
     *
    public int ExecuteInsertBySQL(String params) {
        Map<String, String> paramMap = Common.AnalysisParams (params);
        String strSQL = SQLBuilder.GenerateInsertSQLByParams (paramMap);
        int id = 0;
        try {
            Statement state = _con.createStatement ();
            id = state.executeUpdate (strSQL);
        } catch (SQLException e) {
            loger.error ("{}", e);
        }
        return id;
    }

    /**
     * 根据SQL语句执行数据删除操作
     *
    public int ExecuteDeleteBySQL(String params) {
        Map<String, String> paramMap = Common.AnalysisParams (params);
        String strSQL = SQLBuilder.GenerateDeleteSQLByParams (paramMap);
        int id = 0;
        try {
            Statement state = _con.createStatement ();
            id = state.executeUpdate (strSQL);
        } catch (SQLException e) {
            loger.error ("{}", e);
        }
        return id;
    }
*/

    public String GetInfo() {
        String strContent = "";
        String strSQL = "SELECT * FROM USERINFO ORDER BY ID LIMIT 0,10";
        try {
            Statement state = _con.createStatement ();
            ResultSet rst = state.executeQuery (strSQL);
            strContent = ResultSetToString (rst);
        } catch (SQLException e) {
            loger.error ("{}", e);
        }
        return strContent;
    }

}
