
/**
 * create by dugg 20191102
 * 根据条件生成SQL查询语句
 */

package com.jone.record.kbase.util;

import com.alibaba.fastjson.JSONObject;
import com.jone.record.kbase.tool.EBookTableInfo;
import com.jone.record.kbase.tool.ECatalogTableInfo;
import com.jone.record.kbase.tool.EClsInfo;
import com.jone.record.kbase.tool.EFieldInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLBuilder {

    private static final Logger loger = LoggerFactory.getLogger (SQLBuilder.class);
    private static String strLog = "";
    private static ECatalogTableInfo tableInfo;

    /**
     * 构造数据库记录查询SQL语句，查询志书分类信息
     */
    public static String GenerateRecordNumsQuerySQL(JSONObject jsonObject) {
        StringBuilder strBuilder = new StringBuilder ();
        strBuilder.append ("select count(*) as num from ");
        strBuilder.append (EBookTableInfo.GetTableNameByCode (jsonObject.getString ("type")));
        strBuilder.append (" where SYS_FLD_CLASSFICATION='");
        strBuilder.append (EClsInfo.GetClsCodeByCode (jsonObject.getString ("cls")));
        strBuilder.append ("'");
        return strBuilder.toString ().toUpperCase ();
    }

    public static String GenerateReadCatalogQuerySQL(JSONObject jsonObject){
        String strFields = "SYS_SYSID,TITLE,SYS_FLD_DOI,SYS_FLD_PARENTDOI";
        String strTableName = ECatalogTableInfo.GetTableNameByCode (jsonObject.getString ("type"));
        StringBuilder strBuilder = new StringBuilder ();
        strBuilder.append ("select ");
        strBuilder.append (strFields);
        strBuilder.append (" from ");
        strBuilder.append (strTableName);
        strBuilder.append (" where PARENTDOI='");
        strBuilder.append (jsonObject.getString ("id"));
        strBuilder.append ("'");
        return strBuilder.toString ().toUpperCase ();
    }

    public static String GenerateHistoryQuerySQL(JSONObject jsonObject) {
        StringBuilder strBuilder = new StringBuilder ();
        strBuilder.append ("select ");
        strBuilder.append (EFieldInfo.GetFieldsByCode (jsonObject.getString ("type")));
        strBuilder.append (" from ");
        strBuilder.append (EBookTableInfo.GetTableNameByCode (jsonObject.getString ("type")));
        strBuilder.append (" where ");
        // 取上架上架状态的志书
        strBuilder.append ("ISONLINE=");
        strBuilder.append (jsonObject.getString ("state"));
        // 标题查询
        if (!jsonObject.getString ("title").isEmpty ()) {
            strBuilder.append (" and name='");
            strBuilder.append (jsonObject.getString ("title"));
            strBuilder.append ("'");
        }
        // 分页查询显示 默认 第1页，每页10条
        strBuilder.append (" limit ");
        if (jsonObject.getString ("page").isEmpty ()) {
            if (jsonObject.getString ("pageSize").isEmpty ()) {
                strBuilder.append ("0,10");
            } else {
                strBuilder.append ("0,");
                strBuilder.append (jsonObject.getString ("pageSize"));
            }
        } else {
            int page = Integer.parseInt (jsonObject.getString ("page"));
            int pageSize = Integer.parseInt (jsonObject.getString ("pageSize"));
            if (pageSize == 0) {
                strBuilder.append ("0,10");
                strBuilder.append (",10");
            } else {
                String strPage = "";
                if (page > 0) {
                    strPage = String.format ("%d", (page - 1) * pageSize);
                } else {
                    strPage = "0";
                }
                strBuilder.append (strPage);
                strBuilder.append (",");
                strBuilder.append (pageSize);
            }
        }
        return strBuilder.toString ().toUpperCase ();
    }


    /**
     * 构造数据库记录查询语句，根据条件查询
     * 根据志书的分类号进行分类查询
     public static String GenerateRecordNumsSQL(Map<String, String> paramMap) {
     String strSQL = "";
     String tableName = tableInfo.GetTableNameByCode (paramMap.get ("type"));
     StringBuilder strBuilder = new StringBuilder ();
     strBuilder.append ("select count(*) from ");
     strBuilder.append (tableName);
     strBuilder.append (" where ");
     strBuilder.append ("SYS_FLD_CLASSFICATION='");
     String strClsCode = EClsInfo.GetClsCodeByCode (paramMap.get ("cls"));
     strBuilder.append (strClsCode);
     strBuilder.append ("'");
     strSQL = strBuilder.toString ().toUpperCase ();
     strLog = String.format ("查询数据库记录数的SQL语句:%s", strSQL);
     loger.info (strLog);
     return strSQL;
     }

     /**
     * 根据传入参数生成SQL查询语句
     *
     * @paramMap Map集合
     * @Return 返回SQL查询语句

    public static String GenerateQuerySQLByParams(Map<String, String> paramMap) {
    String strSQL = "";
    StringBuilder strBuilder = new StringBuilder ();
    strBuilder.append ("select * from ");
    strBuilder.append (ETableInfo.GetTableNameByCode (paramMap.get ("type")));
    strBuilder.append (paramMap.get ("fields"));
    strBuilder.append (paramMap.get ("tableName"));
    // 判断是否存在查询条件

    // 判断是否存在分页查询
    if (paramMap.containsKey ("page")) {
    strBuilder.append (" limit ");
    int pages = Integer.parseInt (paramMap.get ("page")) * Integer.parseInt (paramMap.get ("pageSize"));
    strBuilder.append (pages);
    strBuilder.append (",");
    strBuilder.append (paramMap.get ("pageSize"));
    }
    strSQL = strBuilder.toString ();
    strLog = String.format ("查询记录的SQL语句:%s", strSQL);
    loger.info (strLog);
    return strSQL;
    }

    /**
     * 根据传入参数生成SQL更新语句
     *
     * @paramMap Map集合
     * @Return 返回SQL查询语句

    public static String GenerateUpdateSQLByParams(Map<String, String> paramMap) {
    String strSQL = "";
    StringBuilder strBuilder = new StringBuilder ();
    strSQL = strBuilder.toString ();
    // UPDATE USERINFO SET USERNAME=,PASSWORD=,PHONE= WHERE
    strBuilder.append ("update ");

    if (paramMap.containsKey ("tableName")) {
    strBuilder.append (paramMap.get ("tableName"));
    } else {

    }


    strLog = String.format ("更新记录的SQL语句:%s", strSQL);
    loger.info (strLog);
    return strSQL;
    }

    /**
     * 根据传入参数生成SQL插入语句
     *
     * @paramMap Map集合
     * @Return 返回SQL查询语句

    public static String GenerateInsertSQLByParams(Map<String, String> paramMap) {
    String strSQL = "";
    StringBuilder strBuilder = new StringBuilder ();
    strSQL = strBuilder.toString ();

    strLog = String.format ("插入数据的SQL语句:%s", strSQL);
    loger.info (strLog);
    return strSQL;
    }

    /**
     * 根据传入参数生成SQL删除语句
     *
     * @paramMap Map集合
     * @Return 返回SQL查询语句

    public static String GenerateDeleteSQLByParams(Map<String, String> paramMap) {
    String strSQL = "";
    StringBuilder strBuilder = new StringBuilder ();
    strBuilder.append ("delete from ");
    strBuilder.append (paramMap.get ("tableName"));
    strBuilder.append (" where id=");
    strBuilder.append (paramMap.get ("id"));
    strSQL = strBuilder.toString ().toUpperCase ();
    strLog = String.format ("删除记录的SQL语句:%s", strSQL);
    loger.info (strLog);
    return strSQL;
    }
     */
}
