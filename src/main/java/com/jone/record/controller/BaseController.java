package com.jone.record.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jone.record.dao.RedisDao;
import com.jone.record.entity.vo.BaseData;
import com.jone.record.entity.vo.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

public class BaseController {
    protected static Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 打印json格式
     *
     * @param object
     * @param response
     */
    public void printJson(BaseData object, HttpServletResponse response) {
        OutputStream out = null;
        try {
            response.setContentType("application/json" + ";charset=UTF-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            out = response.getOutputStream();
            String jsonStr = JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
            out.write(jsonStr.getBytes("UTF-8"));
        } catch (IOException e) {
            logger.error("IOException", e);
        } finally {
            if (null != out) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    logger.error("IOException", e);
                }
            }
        }
    }

    public UserInfo getRedisUser(HttpServletRequest request, RedisDao dao) {
        String key = request.getHeader("token") == null ? "" : request.getHeader("token");
        if (!StringUtils.isEmpty(key)) {
            String value = dao.getValue(key);
            if (StringUtils.isEmpty(value))
                return null;
            UserInfo userInfo = JSONObject.parseObject(value, UserInfo.class);
            return userInfo;
        } else {
            return null;
        }
    }

    public String clearAdcd(String adcd) {
        char[] strs = adcd.toCharArray();
        int index = 0;
        for (int i = 0; i < strs.length; i++) {
            if (i > 0 && strs[i] == '0' && strs[i - 1] != '0')
                index = i;
            else if (i > 0 && strs[i] != '0')
                index = -1;
        }
        if (index > 0)
            return adcd.substring(0, index);
        else
            return adcd;
    }

    public String getFileName(HttpServletRequest request,
                              HttpServletResponse response, String fileName) {
        String agent = request.getHeader("USER-AGENT");
        try {
            if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
                String newFileName = URLEncoder.encode(fileName, "UTF-8");
                newFileName = StringUtils.replace(newFileName, "+", "%20");
                if (newFileName.length() > 150) {
                    newFileName = new String(fileName.getBytes("GB2312"),
                            "ISO8859-1");
                    newFileName = StringUtils.replace(newFileName, " ", "%20");
                }
                return newFileName;
            }
            if ((agent != null) && (-1 != agent.indexOf("Mozilla")))
                return new String(fileName.getBytes(), "ISO8859-1");

            return fileName;
        } catch (Exception ex) {
            return fileName;
        }

    }
}
