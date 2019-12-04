package com.jone.record.kbase.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class LoadConfig {
    private static final Logger loger = LoggerFactory.getLogger(Common.class);

    private String strServerFilePath = null;

    public LoadConfig() {
        super();
        try {
            Properties pros = new Properties();
            File file = new File("src/main/resources/application.properties");
            InputStream istream = new FileInputStream(file);
            pros.load(istream);
            this.strServerFilePath = pros.getProperty("kbase.server.filepath");
        } catch (Exception e) {
            loger.error("读取配置文件登录信息失败！");
        }
    }

    public String getStrServerFilePath() {
        return strServerFilePath;
    }

    public void setStrServerFilePath(String strServerFilePath) {
        this.strServerFilePath = strServerFilePath;
    }
}
