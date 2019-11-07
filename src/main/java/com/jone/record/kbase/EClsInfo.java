/**
 * create by dugg 20191105
 * 根据传入参数获取分类代码
 */


package com.jone.record.kbase;

import com.alibaba.fastjson.JSONObject;

public enum EClsInfo {

    kbase_all ("0", "3.*"),
    kbase_Province ("1", "3.1.*"),
    kbase_city ("2", "3.2.*"),
    kbase_county ("3", "3.3.*"),
    kbase_villages ("4", "3.4.*");

    private String code;
    private String clsCode;

    EClsInfo(String code, String clsCode) {
        this.code = code;
        this.clsCode = clsCode;
    }

    public String getCode() {
        return code;
    }

    public String getClsCode() {
        return clsCode;
    }

    @Override
    public String toString() {
        String strContent = "";
        JSONObject jsonObject = new JSONObject ();
        jsonObject.clear ();
        try {
            jsonObject.put ("code", code);
            jsonObject.put ("clsCode", clsCode);
            strContent = jsonObject.toString ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return strContent;
    }


    /**
     * 根据查询的code获取获分类代码
     *
     * @param code
     * @return 分类代码
     */
    public static String GetClsCodeByCode(String code) {
        String clsCode = "";
        //通过enum.values()获取所有的枚举值
        for (EClsInfo errInfo : EClsInfo.values ()) {
            //通过enum.get获取字段值
            if (errInfo.getCode ().equals (code)) {
                clsCode = errInfo.clsCode;
                break;
            }
        }
        return clsCode;
    }

    /**
     * 根据查询code获取FieldInfo信息
     *
     * @param code
     * @return
     */
    public static EClsInfo GetClsInfoByCode(String code) {
        EClsInfo EClsInfo = null;
        for (EClsInfo info : EClsInfo.values ()) {
            if (info.getCode ().equals (code)) {
                EClsInfo = info;
                break;
            }
        }
        return EClsInfo;
    }


}
