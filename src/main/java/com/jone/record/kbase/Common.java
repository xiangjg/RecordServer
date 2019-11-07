/**
 * create by dugg 20191102
 * 常用工具类
 */

package com.jone.record.kbase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

public class Common {

    private static final Logger loger = LoggerFactory.getLogger (Common.class);

    public static JSONObject ResultSetToJSONObject(ResultSet rst) {
        JSONObject jsonObject = new JSONObject ();
        jsonObject.clear ();
        try {
            String strValue = "";
            while (rst.next ()) {
                strValue = rst.getString ("NUM");
                jsonObject.put ("count", strValue);
            }
        } catch (Exception e) {
            loger.error ("数据集转换为JSON出错！");
            e.printStackTrace ();
        }
        return jsonObject;
    }

    public static JSONArray ResultSetToJSONArray(ResultSet rst) {
        JSONArray jsonArray = new JSONArray ();
        jsonArray.clear ();
        try {
            ResultSetMetaData rsMetaData = rst.getMetaData ();
            int column = rsMetaData.getColumnCount ();
            String strKey = "";
            String strValue = "";
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
        } catch (Exception e) {
            loger.error ("数据集转换为JSON出错！");
            e.printStackTrace ();
        }
        return jsonArray;
    }


    public static String RSetToString(ResultSet rst) {
        String strContent = "";
        try {
            String strValue = "";
            JSONObject jsonObject = new JSONObject ();
            while (rst.next ()) {
                strValue = rst.getString ("count(*)");
                jsonObject.put ("count", strValue);
            }
            strContent = jsonObject.toString ();
        } catch (Exception e) {
            loger.error ("数据集转换为JSON出错！");
            e.printStackTrace ();
        }
        return strContent;
    }

    /**
     * ResultSet结果集转换为JSON字符串
     *
     * @return 输出JSON字符串
     * @rst 输入ResultSet结果集
     */
    public static String ResultSetToString(ResultSet rst) {
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
            loger.error ("数据集转换为JSON出错！");
            e.printStackTrace ();
        }
        return strContent;
    }

    /**
     * 将JSON字符串解析为Map集
     */
    public static Map<String, String> AnalysisParams(String strParams) {
        Map<String, String> paramMap = new HashMap<> ();
        paramMap.clear ();
        try {
            String key = "";
            String value = "";
            JSONObject jsonObject = JSONObject.parseObject (strParams);

            Iterator iter = jsonObject.entrySet ().iterator ();
            while (iter.hasNext ()) {
                Map.Entry entry = (Map.Entry) iter.next ();
                key = entry.getKey ().toString ();
                value = entry.getValue ().toString ();
                paramMap.put (key, value);
            }


//            Set<String> keySet = new HashSet<String> ();
//            Iterator<String> it = keySet.iterator ();
//            while (it.hasNext ()) {
//                key = it.next ();
//                value = jsonObject.getString (key);
//                paramMap.put (key, value);
//            }

//            Iterator iterator = jsonObj.keys ();
//            String key = "";
//            String value = "";
//            while (iterator.hasNext ()) {
//                key = (String) iterator.next ();
//                value = jsonObj.getString (key);
//  //
        } catch (Exception e) {
            String param = String.format ("解析传入参数时出错，参数为：%s", strParams);
            loger.error (param);
            e.printStackTrace ();
        }
        return paramMap;
    }
}
