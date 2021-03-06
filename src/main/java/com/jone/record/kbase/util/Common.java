/**
 * create by dugg 20191102
 * 常用工具类
 */

package com.jone.record.kbase.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jone.record.kbase.KBaseCon;
import com.jone.record.kbase.entity.Catalog;
import com.jone.record.kbase.entity.JournalCatalog;
import com.jone.record.kbase.entity.KBaseConfig;
import com.jone.record.kbase.tool.EJournalType;
import org.apache.commons.collections.map.LinkedMap;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

    private static final Logger loger = LoggerFactory.getLogger(Common.class);

    public static String GetIamgeFiles(String value) {
        Connection _con = KBaseCon.GetInitConnect();
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

    public static JSONObject RSetToString(ResultSet rst) {
        JSONObject jsonObject = new JSONObject(new LinkedMap());
        String strContent = "";
        try {
            String strValue = "";
            while (rst.next()) {
                strValue = rst.getString("total");
                jsonObject.put("count", strValue);
            }
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
    }

    public static JSONObject ResultSetToJSONObject(ResultSet rst) {
        JSONObject jsonObject = new JSONObject(new LinkedMap());
        ResultSetMetaData rsMetaData = null;
        try {
            rsMetaData = rst.getMetaData();
            int column = rsMetaData.getColumnCount();
            String strKey = "";
            String strValue = "";
            while (rst.next()) {
                for (int i = 1; i <= column; i++) {
                    strKey = rsMetaData.getColumnName(i);
                    strValue = rst.getString(strKey);
                    if (strKey.equals("SYS_FLD_FILEPATH") || strKey.equals("SYS_FLD_COVERPATH")) {
                        strValue = strValue.replace('\\', '/');
                        strValue = Common.GetFilePath() + strValue;
                    } else if (strKey.equals("CONTENT")) {
                        String reg = "[0-9a-z]{32}\\.[a-zA-z]{3}";
                        Pattern patten = Pattern.compile(reg);//编译正则表达式
                        Matcher matcher = patten.matcher(strValue);// 指定要匹配的字符串
                        while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
                            String strText = matcher.group();
                            String img = GetIamgeFiles(strText);
                            strText = String.format("{%s}", strText);
                            String strImg = String.format("<img src=\"%s\">", img);
                            strValue = strValue.replace(strText, strImg);
                        }
                    }
                    jsonObject.put(strKey, strValue);
                }
            }
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
    }

    public static JSONObject AnalysisCatalog(ResultSet rst) {
        JSONObject jsonObject = new JSONObject(new LinkedMap());
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
            jsonObject.put("max", nodeList.size() - 1);
            TreeBuilder treeBuilder = new TreeBuilder(nodeList);
            nodeList = treeBuilder.buildTree();
            jsonObject.put("catalog", nodeList);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
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
                        strValue = strValue.replace('\\', '/');
                        strValue = Common.GetFilePath() + strValue;
                    } else if (strKey.equals("FOUNDDATE")) {
                        if (strValue.length() > 10)
                            strValue = strValue.substring(0, 10);
                    } else if (strKey.equals("TYPE")) {
                        String cnName = EJournalType.GetNameByCode(strValue);
                        jsonObject.put("cnType", cnName);
                    } else if (strKey.equals("ACCESSORIES")) {
                        strKey = "COVERPATH";
                        if (!strValue.isEmpty()) {
                            strValue = GetIamgeFiles(strValue);
                        }
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

    public static JSONObject YearBookListToJSONObject(ResultSet rst) {
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        try {
            ResultSetMetaData rsMetaData = rst.getMetaData();
            int column = rsMetaData.getColumnCount();
            String strKey = "";
            String strValue = "";
            JSONArray jsonArray = new JSONArray(new LinkedList<>());
            List<String> strYearList = new LinkedList<String>();
            while (rst.next()) {
                JSONObject object = new JSONObject(new LinkedHashMap<>());
                for (int i = 1; i <= column; i++) {
                    strKey = rsMetaData.getColumnName(i);
                    strValue = rst.getString(strKey);
                    if (strKey.equals("SYS_FLD_FILEPATH") || strKey.equals("SYS_FLD_COVERPATH")) {
                        strValue = strValue.replace('\\', '/');
                        strValue = Common.GetFilePath() + strValue;
                    } else if (strKey.equals("NAME")) {
                        String strYear = strValue.substring(strValue.length() - 4, strValue.length());
                        object.put("YEAR", strYear);
//                        strKey = "YEAR";
//                        strValue = strYear;
                    }
                    object.put(strKey, strValue);
                }
                jsonArray.add(object);
            }
            jsonObject.put("timeAround", strYearList);
            jsonObject.put("content", jsonArray);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
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
        try {
            String hostname = KBaseConfig.getCoverPath();
            strIpAddress = String.format("http://%s/gzsfzy", hostname);
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return strIpAddress;
    }

    public static String GetSearchDate(String days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - Integer.parseInt(days));
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String strDay = format.format(today);
        return strDay;
    }

    public static List<String> AnalysisYearAroundList(ResultSet rst) {
        List<String> list = new LinkedList<String>();
        try {
            ResultSetMetaData rsMetaData = rst.getMetaData();
            int column = rsMetaData.getColumnCount();
            String strKey = "";
            String strValue = "";
            while (rst.next()) {
                for (int i = 1; i <= column; i++) {
                    strKey = rsMetaData.getColumnName(i);
                    strValue = rst.getString(strKey);
                    if (strKey.equals("NAME")) {
                        int len = strValue.length();
                        String strYear = strValue.substring(len - 4, len);
                        list.add(strYear);
                    }
                }
            }
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return list;
    }

    public static Map<String, String> AnalysisYearAroundMap(ResultSet rst) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        try {
            ResultSetMetaData rsMetaData = rst.getMetaData();
            int column = rsMetaData.getColumnCount();
            String strKey = "";
            String strValue = "";
            while (rst.next()) {
                for (int i = 1; i <= column; i++) {
                    strKey = rsMetaData.getColumnName(i);
                    strValue = rst.getString(strKey);
                    if (strKey.equals("NAME")) {
                        int len = strValue.length();
                        String strYear = strValue.substring(len - 4, len);
                        map.put(strYear, strYear);
                    }
                }
            }
        } catch (Exception e) {
            loger.error("{}", e);
        }
        Map<String, String> sortMap = MapSortUtil.sortByValue(map);
        return sortMap;
    }

    public static JSONObject AnalysisJournalFullText(ResultSet rst) {
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        try {
            ResultSetMetaData rstMeta = rst.getMetaData();
            int column = rstMeta.getColumnCount();
            String strKey = "";
            String strValue = "";
            while (rst.next()) {
                for (int i = 1; i <= column; i++) {
                    strKey = rstMeta.getColumnName(i);
                    strValue = rst.getString(strKey);
                    if (strKey.equals("SYS_FLD_PARAXML")) {
                        DealFiles dealFiles = new DealFiles();
                        dealFiles.setStrServerFilePath(rst.getString("PARENTDOI"));
                        dealFiles.setType(5);
                        String strFullText = dealFiles.AnalysisJournalFullText(strValue);
                        jsonObject.put("fullText", strFullText);
                    } else {
                        jsonObject.put(strKey, strValue);
                    }
                }
            }
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonObject;
    }

    public static List<JournalCatalog> ResultSetToList(ResultSet rst) {
        List<JournalCatalog> list = new LinkedList<JournalCatalog>();
        try {
            ResultSetMetaData rstMetaData = rst.getMetaData();
            int column = rstMetaData.getColumnCount();
            String key = "", value = "";
            while (rst.next()) {
                JournalCatalog jCatalog = new JournalCatalog();
                for (int i = 1; i <= column; i++) {
                    key = rstMetaData.getColumnName(i);
                    value = rst.getString(key);
                    if (key.equals("PARENTDOI")) {
                        jCatalog.setParentGuid(value);
                    } else if (key.equals("TITLE")) {
                        jCatalog.setTitle(value);
                    } else if (key.equals("SYS_FLD_DOI")) {
                        jCatalog.setCurrentGuid(value);
                    } else if (key.equals("SYS_FLD_ORDERNUM")) {
                        jCatalog.setOrderId(value);
                    }
                }
                list.add(jCatalog);
            }
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return list;
    }

    public static List<Object> MapToJSONObjectList(Map<String, List<JournalCatalog>> map) {
        List<Object> jsonObjectList = new LinkedList<Object>();
        Iterator<Map.Entry<String, List<JournalCatalog>>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            JSONObject jsonObj = new JSONObject(new LinkedHashMap<>());
            Map.Entry<String, List<JournalCatalog>> entry = entries.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            jsonObj.put("BookId", key);
            jsonObj.put("Catalog", value);
            jsonObjectList.add(jsonObj);
        }
        return jsonObjectList;
    }

    public static String GetQueryFullTextFields(String type) {
        String strFields = "";
        // 志书和地情资料
        if (type.equals("1") || type.equals("4")) {
            strFields = "TITLE,PARENTNAME,PARENTDOI,SYS_FLD_DOI,SYS_FLD_ORDERNUM";
        } else if (type.equals("2")) {
            // 年鉴
            strFields = "TITLE,PARENTNAME,PARENTDOI,SYS_FLD_DOI,SYS_FLD_ORDERNUM";
        } else if (type.equals("3")) {  // 期刊
            strFields = "NAME,BASEID,SYS_FLD_DOI,YEAR,ISSUE,FULLTEXT";
        }
        return strFields;
    }

    public static JSONArray RestToJSONArray(ResultSet rst) {
        JSONArray jsonArray = new JSONArray(new LinkedList<>());
        try {
            ResultSetMetaData rsMetaData = rst.getMetaData();
            int column = rsMetaData.getColumnCount();
            String strKey = "";
            String strValue = "";
            while (rst.next()) {
                JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
                for (int i = 1; i <= column; i++) {
                    strKey = rsMetaData.getColumnName(i);
                    strValue = rst.getString(strKey);
                    jsonObject.put(strKey, strValue);
                }
                jsonArray.add(jsonObject);
            }
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonArray;
    }

    public static JSONArray JournalResetToJSONArray(ResultSet rst) {
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
                    if (strKey.equals("NAME")) {
                        jsonObject.put("title", strValue);
                    } else if (strKey.equals("SYS_FLD_DOI")) {
                        jsonObject.put("guid", strValue);
                    } else if (strKey.equals("BASEID")) {
                        jsonObject.put("code", strValue);
                    } else if (strKey.equals("PARENTDOI")) {
                        jsonObject.put("parentGuid", strValue);
                    } else if (strKey.equals("SYS_SYSID")) {
                        jsonObject.put("id", strValue);
                    }
                }
                jsonArray.add(jsonObject);
            }
        } catch (Exception e) {
            loger.error("{}", e);
        }
        return jsonArray;
    }

    public static JSONArray GetResultSetToJsonObject(ResultSet rst) {
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

    public static JSONObject GetPagedQueryObjectInfo(String strSQL, int pageSize) {
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
}
