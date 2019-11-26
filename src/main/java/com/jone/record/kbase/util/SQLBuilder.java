package com.jone.record.kbase.util;

import com.alibaba.fastjson.JSONObject;
import com.jone.record.kbase.tool.EBookTableInfo;
import com.jone.record.kbase.tool.ECatalogTableInfo;
import com.jone.record.kbase.tool.EClsInfo;
import com.jone.record.kbase.tool.EFieldInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLBuilder {

    private static final Logger loger = LoggerFactory.getLogger(SQLBuilder.class);
    private static String strLog = "";
    private static ECatalogTableInfo tableInfo;

    /**
     * 生成查询数据库记录总数，根据查询条件
     */
    public static String GeneratePagedQuerySQL(JSONObject params) {
        String strSQL = "";
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select count(*) from ");
        strBuilder.append(ECatalogTableInfo.GetTableNameByCode(params.getString("type")));
        // 添加查询条件
        if (!params.getString("keyword").isEmpty()) {
            strBuilder.append(" where title % '");
            strBuilder.append(params.getString("keyword"));
            strBuilder.append("'");
        }
        strSQL = strBuilder.toString().toUpperCase();
        return strSQL;
    }

    /**
     * 生成专题库目录分页查询语句
     */
    public static String GenerateTopicRecordInfoQuerySQL(JSONObject params, int count) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select ");
        strBuilder.append("TITLE,PARENTNAME,SYS_FLD_PARENTDOI,SYS_FLD_ABSTRACT,SYS_FLD_DOI");
        strBuilder.append(" from ");
        strBuilder.append(ECatalogTableInfo.GetTableNameByCode(params.getString("type")));
        // 添加查询条件
        if (!params.getString("keyword").isEmpty()) {
            strBuilder.append(" where title % '");
            strBuilder.append(params.getString("keyword"));
            strBuilder.append("'");
        }
        // 判断分页
        int pageSize = 0;
        pageSize = params.getInteger("pageSize");
        if (count > pageSize) {
            int page = params.getInteger("page");
            int startPage = (page - 1) * pageSize;
            int endPage = page * pageSize;
            strBuilder.append(" limit ");
            strBuilder.append(startPage);
            strBuilder.append(",");
            strBuilder.append(endPage);
        }
        String strSQL = strBuilder.toString().toUpperCase();
        return strSQL;
    }

    /**
     * 构造数据库记录查询SQL语句，查询志书分类信息
     */
    public static String GenerateRecordNumsQuerySQL(JSONObject params) {
        String strSQL = "";
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select count(*) as num from ");
        strBuilder.append(EBookTableInfo.GetTableNameByCode(params.getString("type")));
        strBuilder.append(" where SYS_FLD_CLASSFICATION='");
        strBuilder.append(EClsInfo.GetClsCodeByCode(params.getString("cls")));
        strBuilder.append("'");
        return strBuilder.toString().toUpperCase();
    }

    public static String GenerateTopicContentQuerySQL(JSONObject params) {
        String strSQL = null;
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select SYS_FLD_PARAXML,PARENTDOI from ");
        strBuilder.append(ECatalogTableInfo.GetTableNameByCode(params.getString("type")));
        strBuilder.append(" where SYS_FLD_DOI='");
        strBuilder.append(params.getString("id"));
        strBuilder.append("'");
        strSQL = strBuilder.toString().toUpperCase();
        return strSQL;
    }

    public static String GenerateBookCatalogQuerySQL(JSONObject params) {
        String strField = "SYS_FLD_CATALOG";
        String id = params.getString("id");
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select ");
        strBuilder.append(strField);
        strBuilder.append(" from ");
        strBuilder.append(EBookTableInfo.GetTableNameByCode(params.getString("type")));
        strBuilder.append(" where SYS_FLD_DOI='");
        strBuilder.append(params.getString("id"));
        strBuilder.append("'");
        return strBuilder.toString().toUpperCase();
    }

    public static String GenerateReadCatalogQuerySQL(JSONObject params) {
        String strFields = "SYS_SYSID,TITLE,SYS_FLD_DOI,SYS_FLD_PARENTDOI";
        String strTableName = ECatalogTableInfo.GetTableNameByCode(params.getString("type"));
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select ");
        strBuilder.append(strFields);
        strBuilder.append(" from ");
        strBuilder.append(strTableName);
        strBuilder.append(" where PARENTDOI='");
        strBuilder.append(params.getString("id"));
        strBuilder.append("'");
        return strBuilder.toString().toUpperCase();
    }

    public static String GenerateHistoryQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select ");
        strBuilder.append(EFieldInfo.GetFieldsByCode(params.getString("type")));
        strBuilder.append(" from ");
        strBuilder.append(EBookTableInfo.GetTableNameByCode(params.getString("type")));
        strBuilder.append(" where ");
        // 取上架上架状态的志书
        strBuilder.append("ISONLINE=");
        strBuilder.append(params.getString("state"));
        // 标题查询
        if (!params.getString("title").isEmpty()) {
            strBuilder.append(" and name='");
            strBuilder.append(params.getString("title"));
            strBuilder.append("'");
        }
        // 分页查询显示 默认 第1页，每页10条
        strBuilder.append(" limit ");
        if (params.getString("page").isEmpty()) {
            if (params.getString("pageSize").isEmpty()) {
                strBuilder.append("0,10");
            } else {
                strBuilder.append("0,");
                strBuilder.append(params.getString("pageSize"));
            }
        } else {
            int page = Integer.parseInt(params.getString("page"));
            int pageSize = Integer.parseInt(params.getString("pageSize"));
            if (pageSize == 0) {
                strBuilder.append("0,10");
                strBuilder.append(",10");
            } else {
                String strPage = "";
                if (page > 0) strPage = String.format("%d", (page - 1) * pageSize);
                else {
                    strPage = "0";
                }
                strBuilder.append(strPage);
                strBuilder.append(",");
                strBuilder.append(pageSize);
            }
        }
        return strBuilder.toString().toUpperCase();
    }


    public static String GenerateBookListQuerySQL(JSONObject params){
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select ");
        strBuilder.append(EFieldInfo.GetFieldsByCode(params.getString("type")));
        strBuilder.append(" from ");
        strBuilder.append(EBookTableInfo.GetTableNameByCode(params.getString("type")));
        strBuilder.append(" where ");
        // 取上架上架状态的志书
        strBuilder.append("ISONLINE=");
        strBuilder.append(params.getString("state"));
        // 根据分类号查询
        if (params.containsKey("cls")){
            strBuilder.append(" and SYS_FLD_CLASSFICATION='");
            strBuilder.append(EClsInfo.GetClsCodeByCode(params.getString("cls")));
            strBuilder.append("'");
        }
        // 分页查询显示 默认 第1页，每页10条
        strBuilder.append(" limit ");
        if (params.getString("page").isEmpty()) {
            if (params.getString("pageSize").isEmpty()) {
                strBuilder.append("0,10");
            } else {
                strBuilder.append("0,");
                strBuilder.append(params.getString("pageSize"));
            }
        } else {
            int page = Integer.parseInt(params.getString("page"));
            int pageSize = Integer.parseInt(params.getString("pageSize"));
            if (pageSize == 0) {
                strBuilder.append("0,10");
                strBuilder.append(",10");
            } else {
                String strPage = "";
                if (page > 0) strPage = String.format("%d", (page - 1) * pageSize);
                else {
                    strPage = "0";
                }
                strBuilder.append(strPage);
                strBuilder.append(",");
                strBuilder.append(pageSize);
            }
        }
        return strBuilder.toString().toUpperCase();
    }

}
