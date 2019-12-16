package com.jone.record.kbase.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jone.record.kbase.tool.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

public class SQLBuilder {

    private static final Logger loger = LoggerFactory.getLogger(SQLBuilder.class);
    private static String strLog = "";
    private static ECatalogTableInfo tableInfo;
    private static final String _tableName = "TEXTDATA";

    /**
     * 生成查询数据库记录总数，根据查询条件
     */
    public static String GeneratePagedQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select count(*) as total from ");
        strBuilder.append(ECatalogTableInfo.GetTableNameByCode(params.getString("type")));

        // 添加查询条件 关键词
        String strKeyword = "";
        if (params.containsKey("keyword")) {
            strKeyword = params.getString("keyword");
            if (!strKeyword.isEmpty()) {
                strBuilder.append(" where title % '");
                strBuilder.append(strKeyword);
                strBuilder.append("'");
            }
        }

        // 添加查询条件 书籍ID
        if (params.containsKey("id")) {
            String strId = params.getString("id");
            if (!strId.isEmpty()) {
                if (!strKeyword.isEmpty()) {
                    strBuilder.append(" and PARENTDOI='");
                    strBuilder.append(strId);
                    strBuilder.append("'");
                } else {
                    strBuilder.append(" where PARENTDOI='");
                    strBuilder.append(strId);
                    strBuilder.append("'");
                }
            }
        }
        return strBuilder.toString().toUpperCase();
    }

    /**
     * 生成专题库目录分页查询语句
     */
    public static String GenerateTopicRecordInfoQuerySQL(JSONObject params, int count) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select ");
        strBuilder.append("TITLE,PARENTDOI,PARENTNAME,SYS_FLD_PARENTDOI,SYS_FLD_ABSTRACT,SYS_FLD_DOI");
        strBuilder.append(" from ");
        strBuilder.append(ECatalogTableInfo.GetTableNameByCode(params.getString("type")));
        // 添加查询条件
        if (params.containsKey("keyword")) {
            if (!params.getString("keyword").isEmpty()) {
                strBuilder.append(" where title % '");
                strBuilder.append(params.getString("keyword"));
                strBuilder.append("'");
            }
        }
        // 判断分页
        int pageSize = 0;
        if (params.containsKey("pageSize")) {
            pageSize = params.getInteger("pageSize");
            if (count > pageSize) {
                if (params.containsKey("page")) {
                    int page = params.getInteger("page");
                    int startPage = (page - 1) * pageSize;
                    strBuilder.append(" limit ");
                    strBuilder.append(startPage);
                    strBuilder.append(",");
                    strBuilder.append(pageSize);
                }
            } else {
                strBuilder.append(" limit 0,");
                strBuilder.append(count);
            }
        } else {
            strBuilder.append(" limit 0,10");
        }
        return strBuilder.toString().toUpperCase();
    }

    /**
     * 构造数据库记录查询SQL语句，查询志书分类信息
     */
    public static String GenerateRecordNumsQuerySQL(JSONObject params) {
        String strSQL = "";
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select count(*) as total from ");
        strBuilder.append(EBookTableInfo.GetTableNameByCode(params.getString("type")));
        strBuilder.append(" where SYS_FLD_CLASSFICATION='");
        strBuilder.append(EClsInfo.GetClsCodeByCode(params.getString("cls")));
        strBuilder.append("'");
        if (params.containsKey("state")) {
            strBuilder.append(" and ISONLINE='");
            strBuilder.append(params.getString("state"));
            strBuilder.append("'");
        }
        return strBuilder.toString().toUpperCase();
    }

    public static String GenerateTopicContentQuerySQL(JSONObject params) {
        String strSQL = null;
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select TITLE,SYS_FLD_PARAXML,PARENTDOI,SYS_FLD_ORDERNUM from ");
        // 根据传入的ID判断返回内容
        strBuilder.append(ECatalogTableInfo.GetTableNameByCode(params.getString("type")));
        strBuilder.append(" where PARENTDOI='");
        strBuilder.append(params.getString("guid"));
        if (params.getString("type").equals("2")) {
            strBuilder.append("' and (FULLTEXT = * not FULLTEXT is null)");
        } else {
            strBuilder.append("' and (CONTENT = * not CONTENT is null)");
        }
        strBuilder.append(" and SYS_FLD_ORDERNUM>=");
        strBuilder.append(params.getString("id"));
        strBuilder.append(" order by SYS_FLD_ORDERNUM limit 1");
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
        String strFields = "SYS_FLD_ORDERNUM,TITLE,SYS_FLD_DOI,SYS_FLD_PARENTDOI";
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

    public static String GenerateHistoryNumsQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select count(*) as total from ");
        strBuilder.append(EBookTableInfo.GetTableNameByCode(params.getString("type")));
        strBuilder.append(" where ");
        // 添加发布状态
        String strState = "";
        if (params.containsKey("state")) {
            strState = params.getString("state");
            if (!strState.isEmpty()) {
                strBuilder.append("ISONLINE=");
                strBuilder.append(strState);
            }
        } else {
            strBuilder.append("ISONLINE=1");
        }
        // 标题查询
        if (params.containsKey("keyword")) {
            if (!params.getString("keyword").isEmpty()) {
                strBuilder.append(" and name='");
                strBuilder.append(params.getString("keyword"));
                strBuilder.append("'");
            }
        }

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
        if (params.containsKey("state")) {
            String strState = params.getString("state");
            if (!strState.isEmpty()) {
                strBuilder.append("ISONLINE=");
                strBuilder.append(strState);
            }
        } else {
            strBuilder.append("ISONLINE=1");
        }
        // 标题查询
        if (params.containsKey("keyword")) {
            if (!params.getString("keyword").isEmpty()) {
                strBuilder.append(" and name='");
                strBuilder.append(params.getString("keyword"));
                strBuilder.append("'");
            }
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


    public static String GenerateBookListQuerySQL(JSONObject params) {
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
        if (params.containsKey("cls")) {
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


    /**
     * 生成方志动态首页查询内容SQL语句
     */
    public static String GenerateDynamicHomeInfoQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        String nums = params.getString("num");
        strBuilder.append("select ID,TITLE,SOURCE,PUBDATE,DIGEST,ACCESSORIES from ");
        strBuilder.append(_tableName);
        strBuilder.append(" where (ACCESSORIES = *  not  ACCESSORIES is null)  order by PUBDATE desc limit ");
        strBuilder.append(nums);
        String strSQL = strBuilder.toString().toUpperCase();
        return strSQL;
    }

    /**
     * 查询方志动态列表记录信息总数
     */
    public static String GenerateGetDynamicCountQuery(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select count(*) as total from ");
        strBuilder.append(_tableName);
        strBuilder.append(" where status='");
        strBuilder.append(params.getString("state"));
        strBuilder.append("' and PUBDATE>'");
        strBuilder.append(Common.GetSearchDate(params.getString("day")));
        strBuilder.append("'");
        String strSQL = strBuilder.toString().toUpperCase();
        return strSQL;
    }

    /**
     * 构造查询方志动态列表内容信息语句
     */
    public static String GenerateGetDynamicListQuerySQL(JSONObject params, int count) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select ID,TITLE,SOURCE,PUBDATE,DIGEST,ACCESSORIES from ");
        strBuilder.append(_tableName);
        strBuilder.append(" where status='");
        strBuilder.append(params.getString("state"));
        strBuilder.append("' and PUBDATE>'");
        strBuilder.append(Common.GetSearchDate(params.getString("day")));
        strBuilder.append("'");

        int pageSize = 0;
        if (params.containsKey("pageSize")) {
            pageSize = params.getInteger("pageSize");
            if (count <= pageSize) {
                strBuilder.append(" limit 0,");
                strBuilder.append(count);
            } else {
                int page = 0;
                if (params.containsKey("page")) {
                    page = params.getInteger("page");
                    int start = (page - 1) * pageSize;
                    strBuilder.append(" limit ");
                    strBuilder.append(start);
                    strBuilder.append(",");
                    strBuilder.append(pageSize);
                }
            }
        } else {
            strBuilder.append(" limit 0,10");
        }
        return strBuilder.toString().toUpperCase();
    }


    /**
     * 构造查询方志动态详细内容 SQL语句
     */
    public static String GenerateGetDynamicContentQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select id,title,source,pubdate,url,content from ");
        strBuilder.append(_tableName);
        strBuilder.append(" where id='");
        strBuilder.append(params.getString("id"));
        strBuilder.append("'");
        String strSQL = strBuilder.toString().toUpperCase();
        return strSQL;
    }

    /**
     * 查询单本书籍的基本信息的SQL语句
     */
    public static String GenerateSingleBookInfoQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select ");
        strBuilder.append(EFieldInfo.GetFieldsByCode(params.getString("type")));
        strBuilder.append(" from ");
        strBuilder.append(EBookTableInfo.GetTableNameByCode(params.getString("type")));

        if (params.containsKey("id")) {
            strBuilder.append(" where SYS_FLD_DOI='");
            strBuilder.append(params.getString("id"));
            strBuilder.append("'");
        }
        return strBuilder.toString().toUpperCase();
    }

    /**
     * 构造查询年鉴列表数量 SQL语句
     */
    public static String GenerateGetYearBookListNumsQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select count(*) as num from DPM_YEARBOOKYEARINFO ");
        if (params.containsKey("year")) {
            String year = params.getString("year");
            if (!year.isEmpty()) {
                strBuilder.append("where name % '");
                strBuilder.append(year);
                strBuilder.append("'");
            }
        }
        return strBuilder.toString().toUpperCase();
    }

    public static String GenerateGetYearBookTimeAroundQuerySQL() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select NAME from DPM_YEARBOOKYEARINFO group by name order by name desc");
        return strBuilder.toString().toUpperCase();
    }

    public static String GenerateGetYearBookListQuerySQL(JSONObject params, int count) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select NAME,ISSUEDEP,ISSUEDATE,ISBN,SYS_FLD_DOI,SYS_FLD_CLASSFICATION,SYS_FLD_COVERPATH from DPM_YEARBOOKYEARINFO ");

        // 添加年度查询条件
        if (params.containsKey("year")) {
            String year = params.getString("year");
            if (!year.isEmpty()) {
                strBuilder.append("where name % '");
                strBuilder.append(year);
                strBuilder.append("'");
            }
        }

        // 排序
        strBuilder.append(" order by name desc");
        // 添加分页
        int pageSize = 0;
        if (params.containsKey("pageSize")) {
            pageSize = params.getInteger("pageSize");
            if (count >= 10) {
                int page = 0;
                if (params.containsKey("page")) {
                    page = params.getInteger("page");
                    int start = (page - 1) * pageSize;
                    strBuilder.append(" limit ");
                    strBuilder.append(start);
                    strBuilder.append(",");
                    strBuilder.append(pageSize);
                }
            } else {
                strBuilder.append(" limit 0,");
                strBuilder.append(count);
            }
        } else {
            strBuilder.append(" limit 0,");
            strBuilder.append(10);
        }

        return strBuilder.toString().toUpperCase();
    }

    /**
     * 构造查询期刊年份区间SQL语句
     */
    public static String GenerateJournalInfoNumsQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select count(*) as total from DPM_JOURNALINFO");
        if (params.containsKey("keyword")) {
            String keyword = params.getString("keyword");
            if (!keyword.isEmpty()) {
                strBuilder.append(" where CNAME % '");
                strBuilder.append(keyword);
                strBuilder.append("'");
            }
        }
        return strBuilder.toString().toUpperCase();
    }

    public static String GenerateJournalInfoQuerySQL(JSONObject params, int count) {
        StringBuilder strBuilder = new StringBuilder();
        String strField = "BASEID,CNAME,DESCRIPTION,ISSN,CN,FOUNDDATE,HOSTDEP,TYPE,SYS_FLD_COVERPATH";
        strBuilder.append("select ");
        strBuilder.append(strField);
        strBuilder.append(" from DPM_JOURNALINFO");
        // 添加关键词检索条件
        if (params.containsKey("keyword")) {
            String strKeyword = params.getString("keyword");
            if (!strKeyword.isEmpty()) {
                strBuilder.append(" where CNAME % '");
                strBuilder.append(strKeyword);
                strBuilder.append("'");
            }
        }
        // 添加分页
        if (params.containsKey("pageSize")) {
            int pageSize = params.getInteger("pageSize");
            if (pageSize > count) {
                strBuilder.append(" limit 0,");
                strBuilder.append(count);
            } else {
                if (params.containsKey("page")) {
                    int page = params.getInteger("page");
                    if (page <= 0) {
                        strBuilder.append(" limit 0,");
                        strBuilder.append(pageSize);
                    } else {
                        int start = (page - 1) * pageSize;
                        strBuilder.append(" limit ");
                        strBuilder.append(start);
                        strBuilder.append(",");
                        strBuilder.append(pageSize);
                    }
                }
            }
        } else {
            strBuilder.append(" limit 0,10");
        }
        return strBuilder.toString().toUpperCase();
    }

    /**
     * 查询期刊年期信息
     */
    public static String GenerateGetJournalYearInfoNumsQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select count(*) as total from DPM_JOURNALYEARINFO");
        // BaseID
        String strBaseId = "";
        if (params.containsKey("code")) {
            strBaseId = params.getString("code");
            if (!strBaseId.isEmpty()) {
                strBuilder.append(" where BASEID='");
                strBuilder.append(strBaseId);
                strBuilder.append("'");
            }
        }
        // year
        if (params.containsKey("year")) {
            String strYear = params.getString("year");
            if (!strYear.isEmpty() && !strBaseId.isEmpty()) {
                strBuilder.append(" and YEAR='");
                strBuilder.append(strYear);
                strBuilder.append("'");
            }
        }
        return strBuilder.toString().toUpperCase();
    }

    public static String GenerateGetJournalYearInfoTimeAroundQuerySQL(String id) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select year from DPM_JOURNALYEARINFO where BASEID='");
        strBuilder.append(id);
        strBuilder.append("' GROUP BY YEAR order by YEAR desc");
        return strBuilder.toString().toUpperCase();
    }


    public static String GenerateGetJournalYearInfoQuerySQL(JSONObject params, int count) {
        StringBuilder strBuilder = new StringBuilder();
        String strField = "BASEID,CNAME,YEAR,ISSUE,SYS_FLD_DOI,SYS_FLD_FILEPATH,SYS_FLD_COVERPATH";
        strBuilder.append("select ");
        strBuilder.append(strField);
        strBuilder.append(" from DPM_JOURNALYEARINFO ");
        // BaseID
        String strBaseId = "";
        if (params.containsKey("code")) {
            strBaseId = params.getString("code");
            if (!strBaseId.isEmpty()) {
                strBuilder.append(" where BASEID='");
                strBuilder.append(strBaseId);
                strBuilder.append("'");
            }
        }
        // year
        if (params.containsKey("year")) {
            String strYear = params.getString("year");
            if (!strYear.isEmpty() && !strBaseId.isEmpty()) {
                strBuilder.append(" and YEAR='");
                strBuilder.append(strYear);
                strBuilder.append("'");
            }
        }
        // 添加分页
        if (params.containsKey("pageSize")) {
            int pageSize = params.getInteger("pageSize");
            if (pageSize > count) {
                strBuilder.append(" limit 0,");
                strBuilder.append(count);
            } else {
                if (params.containsKey("page")) {
                    int page = params.getInteger("page");
                    if (page <= 0) {
                        strBuilder.append(" limit 0,");
                        strBuilder.append(pageSize);
                    } else {
                        int start = (page - 1) * pageSize;
                        strBuilder.append(" limit ");
                        strBuilder.append(start);
                        strBuilder.append(",");
                        strBuilder.append(pageSize);
                    }
                }
            }
        } else {
            strBuilder.append(" limit 0,10");
        }
        return strBuilder.toString().toUpperCase();
    }


    public static String GenerateJournalBaseInfoQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        String strFields = "BASEID,CNAME,YEAR,ISSUE,SYS_FLD_DOI,SYS_FLD_COVERPATH";
        strBuilder.append("select ");
        strBuilder.append(strFields);
        strBuilder.append(" from DPM_JOURNALYEARINFO where BASEID='");
        strBuilder.append(params.getString("code"));
        strBuilder.append("' and SYS_FLD_DOI='");
        strBuilder.append(params.getString("id"));
        strBuilder.append("'");
        return strBuilder.toString().toUpperCase();
    }

    public static String GenerateJournalBaseCatalogQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("SELECT SYS_FLD_CATALOG FROM DPM_JOURNALYEARINFO where BASEID='");
        strBuilder.append(params.getString("code"));
        strBuilder.append("' and SYS_FLD_DOI='");
        strBuilder.append(params.getString("id"));
        strBuilder.append("'");
        return strBuilder.toString().toUpperCase();
    }

    public static String GenerateJournalReadCatalogQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        String strFields = "NAME,SYS_FLD_DOI,BASEID,PARENTDOI";
        strBuilder.append("select ");
        strBuilder.append(strFields);
        strBuilder.append(" from DPM_JOURNALARTICLE where BASEID='");
        strBuilder.append(params.getString("code"));
        strBuilder.append("' and PARENTDOI='");
        strBuilder.append(params.getString("id"));
        strBuilder.append("'");
        return strBuilder.toString().toUpperCase();
    }


    public static String GenerateJournalFullTextQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        String strFields = "BASEID,SYS_FLD_DOI,PARENTDOI,SYS_FLD_PARAXML";
        strBuilder.append("select ");
        strBuilder.append(strFields);
        strBuilder.append(" from DPM_JOURNALARTICLE where BASEID='");
        strBuilder.append(params.getString("code"));
        strBuilder.append("' and SYS_FLD_DOI='");
        strBuilder.append(params.getString("id"));
        strBuilder.append("'");
        return strBuilder.toString().toUpperCase();
    }

    public static String GenerateBookChapterInfoGroupNumsQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select count(*) as count from ");
        strBuilder.append(ECatalogTableInfo.GetTableNameByCode(params.getString("type")));
        strBuilder.append(" where title % '");
        strBuilder.append(params.getString("keyword"));
        strBuilder.append("' GROUP by PARENTDOI");
        return strBuilder.toString().toUpperCase();
    }


    public static String GenerateBookChapterInfoGroupQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        String strFields = " TITLE,PARENTDOI,SYS_FLD_DOI,SYS_FLD_ORDERNUM";
        strBuilder.append("select ");
        strBuilder.append(strFields);
        strBuilder.append(" from ");
        strBuilder.append(ECatalogTableInfo.GetTableNameByCode(params.getString("type")));
        if (params.containsKey("keyword")) {
            String keyword = params.getString("keyword");
            if (!keyword.isEmpty()) {
                strBuilder.append(" where title % '");
                strBuilder.append(keyword);
                strBuilder.append("'");
            }
        }

        strBuilder.append(" order by PARENTDOI");
        return strBuilder.toString().toUpperCase();
    }

    public static String GenerateBookChapterInfoQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        String strFields = "title,PARENTDOI";
        strBuilder.append("select ");
        strBuilder.append(strFields);
        strBuilder.append(" from ");
        strBuilder.append(params.getString("type"));
        strBuilder.append(" where title % '");
        strBuilder.append(params.getString("keyword"));
        strBuilder.append("'");
        return strBuilder.toString().toUpperCase();
    }

    public static String GenerateBookListCatalogNumsQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select count(*) as total from ");
        strBuilder.append(ECatalogTableInfo.GetTableNameByCode(params.getString("type")));
        String strKeyword = "";
        if (params.containsKey("keyword")) {
            strKeyword = params.getString("keyword");
            if (!strKeyword.isEmpty()) {
                strBuilder.append(" where ");
                strBuilder.append(EFullTextField.GetFieldByCode(params.getString("type")));
                strBuilder.append(" % '");
                strBuilder.append(strKeyword);
                strBuilder.append("'");
            }
        }
        if (params.containsKey("id")) {
            String strGuid = params.getString("id");
            if (!strGuid.isEmpty()) {
                if (strKeyword.isEmpty()) {
                    strBuilder.append(" where ");
                } else {
                    strBuilder.append(" and ");
                }
                strBuilder.append("PARENTDOI='");
                strBuilder.append(params.getString("id"));
                strBuilder.append("'");
            }
        }
        return strBuilder.toString().toUpperCase();
    }

    /**
     * 生成查询单本书籍全文内容SQL语句
     */
    public static String GenerateBookListCatalogQuerySQL(JSONObject params, int count) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select ");
        strBuilder.append(EFullFields.GetFieldByCode(params.getString("type")));
        strBuilder.append(" from ");
        strBuilder.append(ECatalogTableInfo.GetTableNameByCode(params.getString("type")));
        String strKeyword = "";
        if (params.containsKey("keyword")) {
            strKeyword = params.getString("keyword");
            if (!strKeyword.isEmpty()) {
                strBuilder.append(" where ");
                strBuilder.append(EFullTextField.GetFieldByCode(params.getString("type")));
                strBuilder.append(" % '");
                strBuilder.append(strKeyword);
                strBuilder.append("'");
            }
        }
        if (params.containsKey("id")) {
            String strGuid = params.getString("id");
            if (!strGuid.isEmpty()) {
                if (strKeyword.isEmpty()) {
                    strBuilder.append(" where ");
                } else {
                    strBuilder.append(" and ");
                }
                strBuilder.append("PARENTDOI='");
                strBuilder.append(params.getString("id"));
                strBuilder.append("'");
            }
        }
        // 分页
        if (params.containsKey("pageSize")) {
            int pageSize = params.getInteger("pageSize");
            if (count <= pageSize) {
                strBuilder.append(" limit 0,");
                strBuilder.append(count);
            } else {
                if (params.containsKey("page")) {
                    int page = params.getInteger("page");
                    int start = (page - 1) * pageSize;
                    strBuilder.append(" limit ");
                    strBuilder.append(start);
                    strBuilder.append(",");
                    strBuilder.append(pageSize);
                }
            }
        } else {
            strBuilder.append(" limit 0,10");
        }
        return strBuilder.toString().toUpperCase();
    }


    public static String GenerateChronicleEventsNumsQuerySQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select count(*) as total from DPM_CHRONICLEVENTS where eventTime>'");
        strBuilder.append(params.getString("startTime"));
        strBuilder.append("' and eventTime<'");
        strBuilder.append(params.getString("endTime"));
        strBuilder.append("'");
        return strBuilder.toString().toUpperCase();
    }

    public static String GenerateChronicleEventsQuerySQL(JSONObject params, int count) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select ID,TITLE,EventDATE,EVENT from DPM_CHRONICLEVENTS where eventTime>'");
        strBuilder.append(params.getString("startTime"));
        strBuilder.append("' and eventTime<'");
        strBuilder.append(params.getString("endTime"));
        strBuilder.append("'");

        if (params.containsKey("pageSize")) {
            int pageSize = params.getInteger("pageSize");
            if (count <= pageSize) {
                strBuilder.append(" limit 0,");
                strBuilder.append(count);
            } else {
                if (params.containsKey("page")) {
                    int page = params.getInteger("page");
                    if (page >= 1) {
                        int start = (page - 1) * pageSize;
                        strBuilder.append(" limit ");
                        strBuilder.append(start);
                        strBuilder.append(",");
                        strBuilder.append(pageSize);
                    } else {
                        strBuilder.append(" limit 0,");
                        strBuilder.append(pageSize);
                    }
                }
            }
        } else {
            strBuilder.append(" limit 0,10");
        }
        return strBuilder.toString().toUpperCase();
    }


    public static String GenerateQueryFullTextNumsSQL(JSONObject params) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("select count(*) as total from ");
        strBuilder.append(ECatalogTableInfo.GetTableNameByCode(params.getString("type")));
        strBuilder.append(" where PARENTDOI='");
        strBuilder.append(params.getString("id"));
        strBuilder.append("'");
        if (params.containsKey("keyword")) {
            String strKeyword = params.getString("keyword");
            if (!strKeyword.isEmpty()) {
                strBuilder.append(" and ");
                strBuilder.append(EFullTextField.GetFieldByCode(params.getString("type")));
                strBuilder.append(" % '");
                strBuilder.append(strKeyword);
                strBuilder.append("'");
            }
        }
        return strBuilder.toString().toUpperCase();
    }

    public static String GenerateQueryFullTextSQL(JSONObject params, int count) {
        StringBuilder strBuilder = new StringBuilder();
        String strFields = Common.GetQueryFullTextFields(params.getString("type"));
        strBuilder.append("select ");
        strBuilder.append(strFields);
        strBuilder.append(" from ");
        strBuilder.append(ECatalogTableInfo.GetTableNameByCode(params.getString("type")));
        strBuilder.append(" where PARENTDOI='");
        strBuilder.append(params.getString("id"));
        strBuilder.append("'");
        if (params.containsKey("keyword")) {
            String strKeyword = params.getString("keyword");
            if (!strKeyword.isEmpty()) {
                strBuilder.append(" and ");
                strBuilder.append(EFullTextField.GetFieldByCode(params.getString("type")));
                strBuilder.append(" % '");
                strBuilder.append(strKeyword);
                strBuilder.append("'");
            }
        }
        // 分页
        if (params.containsKey("pageSize")) {
            int pageSize = params.getInteger("pageSize");
            if (pageSize >= count) {
                strBuilder.append(" limit 0,");
                strBuilder.append(count);
            } else {
                if (params.containsKey("page")) {
                    int page = params.getInteger("page");
                    int start = (page - 1) * pageSize;
                    strBuilder.append(" limit ");
                    strBuilder.append(start);
                    strBuilder.append(",");
                    strBuilder.append(pageSize);
                } else {
                    strBuilder.append(" limit 0,");
                    strBuilder.append(pageSize);
                }
            }

        } else {
            strBuilder.append(" limit 0,10");
        }
        return strBuilder.toString().toUpperCase();
    }
}
