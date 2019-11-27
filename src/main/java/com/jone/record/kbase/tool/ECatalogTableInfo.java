/**
 * create by dugg 20191104
 * 根据传入的资源类型，返回数据库表名
 */

package com.jone.record.kbase.tool;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.LinkedMap;

public enum ECatalogTableInfo {

    kbase_History_Books("1", "DPM_CHAPTER"),                  // 志书
    kbase_Year_Books("2", "DPM_YEARBOOKARTICLE"),             // 年鉴
    kbase_Local_History_Books("3", "DPM_CHAPTER"),            // 地方史
    kbase_Situation_Books("4", "DPM_CHAPTER"),                // 地情资料
    ;

    private String code;
    private String tableName;

    public String getCode() {
        return code;
    }

    public String getTableName() {
        return tableName;
    }


    ECatalogTableInfo(String code, String tableName) {
        this.code = code;
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        String strContent = "";
        JSONObject jsonObject = new JSONObject(new LinkedMap());
        jsonObject.clear();
        try {
            jsonObject.put("code", code);
            jsonObject.put("tableName", tableName);
            strContent = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strContent;
    }

    /**
     * 根据type获取表名tableName
     *
     * @param code
     * @return
     */
    public static String GetTableNameByCode(String code) {
        String tableInfo = null;
        //通过enum.values()获取所有的枚举值
        for (ECatalogTableInfo eCatalogTableInfo : ECatalogTableInfo.values()) {
            //通过enum.get获取字段值
            if (eCatalogTableInfo.getCode().equals(code)) {
                tableInfo = eCatalogTableInfo.tableName;
            }
        }
        return tableInfo;
    }

    /**
     * 根据code获取ErrorInfo
     *
     * @param code
     * @return
     */
    public static ECatalogTableInfo GetTableNameInfoByCode(String code) {
        ECatalogTableInfo tableInfo = null;
        for (ECatalogTableInfo eCatalogTableInfo : ECatalogTableInfo.values()) {
            if (eCatalogTableInfo.getCode().equals(code)) {
                tableInfo = eCatalogTableInfo;
            }
        }
        return tableInfo;
    }

}
