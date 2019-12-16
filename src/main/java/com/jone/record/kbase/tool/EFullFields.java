package com.jone.record.kbase.tool;

import com.alibaba.fastjson.JSONObject;

/**
 * 单本书籍全文检索内容字段信息
 */
public enum EFullFields {

    kbase_History_Books("1", "TITLE,PARENTDOI,SYS_FLD_DOI,SYS_FLD_ORDERNUM"),                     // 志书
    kbase_Year_Books("2", "TITLE,PARENTDOI,SYS_FLD_DOI,SYS_FLD_ORDERNUM"),            // 年鉴
    kbase_Journal_books("3", "NAME,PARENTDOI,SYS_FLD_DOI"),                // 期刊
    kbase_Situation_Books("4", "NAME,PARENTDOI,SYS_FLD_DOI,SYS_FLD_ORDERNUM"),              // 地情资料
    kbase_Multi_Media("5", "NAME,PARENTDOI,SYS_FLD_DOI"),                          // 多媒体，只包含图片
    kbase_Local_History_Books("6", "NAME,PARENTDOI,SYS_FLD_DOI,SYS_FLD_ORDERNUM"),       // 地方史
    ;

    EFullFields(String code, String field) {
        this.code = code;
        this.fields = field;
    }

    private String code;
    private String fields;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getField() {
        return fields;
    }

    public void setField(String field) {
        this.fields = field;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.clear();
        try {
            jsonObject.put("code", code);
            jsonObject.put("field", fields);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String GetFieldByCode(String code) {
        String strName = "";
        //通过enum.values()获取所有的枚举值
        for (EFullFields fieldInfo : EFullFields.values()) {
            //通过enum.get获取字段值
            if (fieldInfo.getCode().equals(code)) {
                strName = fieldInfo.fields;
                break;
            }
        }
        return strName;
    }

    public static EFullFields GetEFieldInfoByCode(String code) {
        EFullFields fieldInfo = null;
        for (EFullFields info : EFullFields.values()) {
            if (info.getCode().equals(code)) {
                fieldInfo = info;
                break;
            }
        }
        return fieldInfo;
    }
}