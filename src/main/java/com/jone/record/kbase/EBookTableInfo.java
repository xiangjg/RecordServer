/**
 * create by dugg 20191104
 * 根据传入的资源类型，返回数据库表名
 */

package com.jone.record.kbase;

import com.alibaba.fastjson.JSONObject;

public enum EBookTableInfo {
    kbase_History_Books ("1", "DPM_BOOK"),                     // 志书
    kbase_Year_Books ("2", "DPM_YEARBOOKYEARINFO"),            // 年鉴
    kbase_Local_History_Books ("3", "DPM_LOCALHISTORY"),       // 地方史
    kbase_Situation_Books ("4", "DPM_SITUATION"),              // 地情资料
    ;

    private String code;
    private String tableName;

    public String getCode() {
        return code;
    }

    public String getTableName() {
        return tableName;
    }

    EBookTableInfo(String code, String tableName) {
        this.code = code;
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        String strContent = "";
        JSONObject jsonObject = new JSONObject ();
        jsonObject.hashCode ();
        try {
            jsonObject.put ("code", code);
            jsonObject.put ("tableName", tableName);
            strContent = jsonObject.toString ();
        } catch (Exception e) {
            e.printStackTrace ();
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
        String strTableName = "";
        //通过enum.values()获取所有的枚举值
        for (EBookTableInfo Info : EBookTableInfo.values ()) {
            //通过enum.get获取字段值
            if (Info.getCode ().equals (code)) {
                strTableName = Info.tableName;
                break;
            }
        }
        return strTableName;
    }

    /**
     * 根据code获取ErrorInfo
     *
     * @param code
     * @return
     */
    public static EBookTableInfo GetTableNameInfoByCode(String code) {
        EBookTableInfo ETableInfo = null;
        for (EBookTableInfo info : EBookTableInfo.values ()) {
            if (info.getCode ().equals (code)) {
                ETableInfo = info;
                break;
            }
        }
        return ETableInfo;
    }

}
