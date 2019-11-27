/**
 * create by dugg 20191102
 * 常用工具类
 */

package com.jone.record.kbase.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jone.record.kbase.entity.Catalog;
import org.apache.commons.collections.map.LinkedMap;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

public class Common {

    private static final Logger loger = LoggerFactory.getLogger(Common.class);

    public static JSONObject ResultSetToJSONObject(ResultSet rst) {
        JSONObject jsonObject = new JSONObject(new LinkedMap());
        try {
            String strValue = "";
            while (rst.next()) {
                strValue = rst.getString("NUM");
                jsonObject.put("count", strValue);
            }
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
    }

    public static List<Catalog> AnalysisCatalog(ResultSet rst) {
        ResultSetMetaData rstMeta = null;
        List<Catalog> nodeList = new LinkedList<Catalog>();
        try {
            String key = "";
            String value = "";
            rstMeta = rst.getMetaData();
            int column = rstMeta.getColumnCount();
            while (rst.next()) {
                Catalog catalog = new Catalog();
                for (int i = 1; i <= column; i++) {
                    key = rstMeta.getColumnName(i);
                    value = rst.getString(key);
                    if (key.equals("SYS_FLD_ORDERNUM")) {
                        catalog.setId(value);
                    } else if (key.equals("TITLE")) {
                        catalog.setTitle(value);
                    } else if (key.equals("SYS_FLD_DOI")) {
                        catalog.setGuid(value);
                    } else if (key.equals("SYS_FLD_PARENTDOI")) {
                        catalog.setParentGuid(value);
                    }
                }
                nodeList.add(catalog);
            }
            TreeBuilder treeBuilder = new TreeBuilder(nodeList);
            nodeList = treeBuilder.buildTree();
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return nodeList;
    }

    public static JSONArray ResultSetToJSONArray(ResultSet rst) {
        JSONArray jsonArray = new JSONArray(new LinkedList<>());
        try {
            ResultSetMetaData rsMetaData = rst.getMetaData();
            int column = rsMetaData.getColumnCount();
            String strKey = "";
            String strValue = "";
            while (rst.next()) {
                JSONObject jsonObject = new JSONObject(new LinkedHashMap());
                for (int i = 1; i <= column; i++) {
                    strKey = rsMetaData.getColumnName(i);
                    strValue = rst.getString(strKey);
                    if (strKey.equals("SYS_FLD_FILEPATH") || strKey.equals("SYS_FLD_COVERPATH")) {
                        strValue = strValue.replace('\\','/');
                        strValue = Common.GetFilePath() + strValue;
                    }
                    jsonObject.put(strKey, strValue);
                }
                jsonArray.add(jsonObject);
            }
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonArray;
    }


    public static String RSetToString(ResultSet rst) {
        String strContent = "";
        try {
            String strValue = "";
            JSONObject jsonObject = new JSONObject(new LinkedMap());
            while (rst.next()) {
                strValue = rst.getString("count(*)");
                jsonObject.put("count", strValue);
            }
            strContent = jsonObject.toString();
        } catch (Exception e) {
            loger.error("{}", e);
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
            ResultSetMetaData rsMetaData = rst.getMetaData();
            int column = rsMetaData.getColumnCount();
            String strKey = "";
            String strValue = "";
            JSONArray jsonArray = new JSONArray(new LinkedList<>());
            while (rst.next()) {
                JSONObject jsonObject = new JSONObject(new LinkedMap());
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

    /**
     * 将JSON字符串解析为Map集
     */
    public static Map<String, String> AnalysisParams(String strParams) {
        Map<String, String> paramMap = new HashMap<>();
        try {
            String key = "";
            String value = "";
            JSONObject jsonObject = JSONObject.parseObject(strParams);
            Iterator iter = jsonObject.keySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                key = entry.getKey().toString();
                value = entry.getValue().toString();
                paramMap.put(key, value);
            }
        } catch (Exception e) {
            String param = String.format("解析传入参数时出错，参数为：%s", strParams);
            loger.error("{}", param);
        }
        return paramMap;
    }

    public static String DealImageFile(String param) {
        String strContent = XML.toJSONObject(param).toString();
        JSONObject jsonObject = JSONObject.parseObject(strContent);
        String key = null;
        String value = null;
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            key = entry.getKey();
            Object object = jsonObject.getString(key);
        }
        return strContent;
    }

    public static String GetFilePath() {
        String strIpAddress = "";
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
            String hostname = addr.getHostAddress();
            strIpAddress = String.format("http://%s/gzsfzy",hostname);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return strIpAddress;
    }
}
