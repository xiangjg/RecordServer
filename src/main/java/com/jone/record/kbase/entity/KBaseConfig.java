package com.jone.record.kbase.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class KBaseConfig {
    private static String driverClass;
    private static String url;
    private static String userName;
    private static String password;
    private static String coverPath;

    @Value("${kbase.driverclass}")
    public void setDriverClass(String driverClass) {
        KBaseConfig.driverClass = driverClass;
    }

    @Value("${kbase.url}")
    public void setUrl(String url) {
        KBaseConfig.url = url;
    }

    @Value("${kbase.username}")
    public void setUserName(String userName) {
        KBaseConfig.userName = userName;
    }

    @Value("${kbase.password}")
    public void setPassword(String password) {
        KBaseConfig.password = password;
    }

    @Value("${kbase.coverpath}")
    public void setCoverPath(String coverPath) {
        KBaseConfig.coverPath = coverPath;
    }


    public static String getDriverClass() {
        return driverClass;
    }

    public static String getUrl() {
        return url;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public static String getCoverPath() {
        return coverPath;
    }
}
