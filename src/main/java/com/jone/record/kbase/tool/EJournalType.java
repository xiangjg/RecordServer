package com.jone.record.kbase.tool;

import com.alibaba.fastjson.JSONObject;

public enum EJournalType {
    Journal_weekly("0","周刊"),
    Journal_Journal("1","旬刊"),
    Journal_Half_Moon("2","半月刊"),
    Journal_monthly("3","月刊"),
    Journal_Bimonthly("4","双月刊"),
    Journal_quarterly("5","季刊"),
    Journal_semiannual("6","半年刊"),
    Journal_Annual("7","年刊");

    private String code;
    private String name;

    EJournalType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject ();
        jsonObject.clear ();
        try {
            jsonObject.put ("code", code);
            jsonObject.put ("name", name);
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return jsonObject.toString ();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String GetNameByCode(String code) {
        String strName = "";
        //通过enum.values()获取所有的枚举值
        for (EJournalType journalType : EJournalType.values ()) {
            //通过enum.get获取字段值
            if (journalType.getCode ().equals (code)) {
                strName = journalType.name;
                break;
            }
        }
        return strName;
    }

    public static EJournalType GetEJournalTypeByCode(String code) {
        EJournalType journalType = null;
        for (EJournalType info : EJournalType.values ()) {
            if (info.getCode ().equals (code)) {
                journalType = info;
                break;
            }
        }
        return journalType;
    }
}
