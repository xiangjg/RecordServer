package com.jone.record.kbase;


import com.alibaba.fastjson.JSONObject;

public enum EFieldInfo {

    kbase_History_Books ("1", "NAME,AUTHOR,ISBN,ISSUEDEP,ISSUEDATE,SYS_FLD_CLASSFICATION,SYS_FLD_DOI,SYS_FLD_FILEPATH,SYS_FLD_COVERPATH,SYS_FLD_ABSTRACT"),                     // 志书
    kbase_Year_Books ("2", "NAME,AUTHOR,ISBN,ISSUEDEP,ISSUEDATE,SYS_FLD_CLASSFICATION,SYS_FLD_DOI,SYS_FLD_FILEPATH,SYS_FLD_COVERPATH,SYS_FLD_ABSTRACT"),            // 年鉴
    kbase_Local_History_Books ("3", "NAME,AUTHOR,ISBN,ISSUEDEP,ISSUEDATE,SYS_FLD_CLASSFICATION,SYS_FLD_DOI,SYS_FLD_FILEPATH,SYS_FLD_COVERPATH,SYS_FLD_ABSTRACT"),       // 地方史
    kbase_Situation_Books ("4", "NAME,AUTHOR,ISBN,ISSUEDEP,ISSUEDATE,SYS_FLD_CLASSFICATION,SYS_FLD_DOI,SYS_FLD_FILEPATH,SYS_FLD_COVERPATH,SYS_FLD_ABSTRACT");              // 地情资料

    private String code;
    private String strFields;

    public String getCode() {
        return code;
    }

    public String getStrFields() {
        return strFields;
    }

    EFieldInfo(String code, String strFields) {
        this.code = code;
        this.strFields = strFields;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject ();
        jsonObject.clear ();
        try {
            jsonObject.put ("code", code);
            jsonObject.put ("tableName", strFields);
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return jsonObject.toString ();
    }

    /**
     * 根据查询的code获取获取字段信息
     *
     * @param code
     * @return
     */
    public static String GetFieldsByCode(String code) {
        String strFields = "";
        //通过enum.values()获取所有的枚举值
        for (EFieldInfo errInfo : EFieldInfo.values ()) {
            //通过enum.get获取字段值
            if (errInfo.getCode ().equals (code)) {
                strFields = errInfo.strFields;
                break;
            }
        }
        return strFields;
    }

    /**
     * 根据查询code获取FieldInfo信息
     *
     * @param code
     * @return
     */
    public static EFieldInfo GetFieldInfoByCode(String code) {
        EFieldInfo EFieldInfo = null;
        for (EFieldInfo info : EFieldInfo.values ()) {
            if (info.getCode ().equals (code)) {
                EFieldInfo = info;
                break;
            }
        }
        return EFieldInfo;
    }

}
