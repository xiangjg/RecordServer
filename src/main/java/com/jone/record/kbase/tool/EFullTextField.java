package com.jone.record.kbase.tool;

import com.alibaba.fastjson.JSONObject;

/**
 * 单本书籍全文检索内容字段信息
 *
 * */

public enum EFullTextField {
    kbase_History_Books("1", "CONTENT"),                     // 志书
    kbase_Year_Books("2", "FULLTEXT"),            // 年鉴
    kbase_Journal_books("3", "FULLTEXT"),                // 期刊
    kbase_Situation_Books("4", "CONTENT"),              // 地情资料
    kbase_Multi_Media("5", "CONTENT"),                          // 多媒体，只包含图片
    kbase_Local_History_Books("6", "CONTENT"),       // 地方史
    ;

    EFullTextField(String code, String field) {
        this.code = code;
        this.field = field;
    }

    private String code;
    private String field;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.clear();
        try {
            jsonObject.put("code", code);
            jsonObject.put("field", field);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String GetFieldByCode(String code) {
        String strName = "";
        //通过enum.values()获取所有的枚举值
        for (EFullTextField fieldInfo : EFullTextField.values()) {
            //通过enum.get获取字段值
            if (fieldInfo.getCode().equals(code)) {
                strName = fieldInfo.field;
                break;
            }
        }
        return strName;
    }

    public static EFullTextField GetEFieldInfoByCode(String code) {
        EFullTextField fieldInfo = null;
        for (EFullTextField info : EFullTextField.values()) {
            if (info.getCode().equals(code)) {
                fieldInfo = info;
                break;
            }
        }
        return fieldInfo;
    }
}

