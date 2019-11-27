package com.jone.record.kbase.tool;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.LinkedMap;

public enum EResourceType {

    kbase_book(1, "book"),
    kbase_Year_Book(2, "yearbook"),
    kbase_Local_History(3, "LocalHistory"),
    kbase_Situation_Book(4, "Situation_Book");

    EResourceType(int code, String file) {
        this.code = code;
        this.file = file;
    }

    private int code;
    private String file;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {

        JSONObject jsonObject = new JSONObject(new LinkedMap());
        jsonObject.clear();
        try {
            jsonObject.put("code", code);
            jsonObject.put("tableName", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();

    }

    public static String GetFieldsByCode(int code) {
        String strFilePath = "";
        //通过enum.values()获取所有的枚举值
        for (EResourceType resType : EResourceType.values()) {
            //通过enum.get获取字段值
            if (resType.getCode() == code) {
                strFilePath = resType.file;
                break;
            }
        }
        return strFilePath;
    }

    public static EResourceType GetFieldInfoByCode(int code) {
        EResourceType EFieldInfo = null;
        for (EResourceType info : EResourceType.values()) {
            if (info.getCode() == code) {
                EFieldInfo = info;
                break;
            }
        }
        return EFieldInfo;
    }


}
