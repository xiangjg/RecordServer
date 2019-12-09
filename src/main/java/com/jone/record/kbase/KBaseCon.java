
/**
 * create by dugg 20191102
 * KBase链接类，执行KBase链接
 */

package com.jone.record.kbase;

import com.jone.record.entity.KBaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * application.properties 配置文件信息
 * # KBase config info
 * kbase.driverclass=com.kbase.jdbc.Driver
 * kbase.url=jdbc:kbase://127.0.0.1:4567
 * kbase.username=DBOWN
 * kbase.password=
 * <p>
 * # port
 * server.port=8221
 */


public class KBaseCon {

    private static final Logger loger = LoggerFactory.getLogger (KBaseCon.class);
    private static Connection _con = null;
    private static String _driverClass = "";        // "com.kbase.jdbc.Driver";
    private static String _url = "";                // "jdbc:kbase://127.0.0.1:4567";
    private static String _userName = "";
    private static String _password = "";
    private static int _index = 0;
    private static String strLog = "";

//    static {
//        try {
//            _index = 0;
//            readConfig ();
//            Class.forName (_driverClass);
//            if (null == _con) {
//                _index++;
//                _con = DriverManager.getConnection (_url, _userName, _password);
//                strLog = String.format ("KBase数据库初始化连接成功,连接了%d次！", _index);
//                loger.info (strLog);
//            }
//        } catch (Exception e) {
//            loger.error ("数据库初始化连接失败！");
//            e.printStackTrace ();
//        }
//    }

    private static void readConfig() {
        try {
            Properties pros = new Properties ();
            File file = new File ("src/main/resources/application.properties");
            InputStream istream = new FileInputStream (file);
            pros.load (istream);
            _driverClass = pros.getProperty ("kbase.driverclass");
            _url = pros.getProperty ("kbase.url");
            _userName = pros.getProperty ("kbase.username");
            _password = pros.getProperty ("kbase.password");
            loger.info ("读取数据库配置参数成功！");
        } catch (IOException e) {
            loger.error ("读取配置文件登录信息失败！");
            e.printStackTrace();
        }
    }

    public static Connection GetInitConnect() {
        if (null == _con) {
            try {
                _index++;
                Class.forName(KBaseConfig.getDriverClass());
                _con = DriverManager.getConnection (KBaseConfig.getUrl (), KBaseConfig.getUserName (), KBaseConfig.getPassword ());
                strLog = String.format ("KBase数据库初始化连接成功,连接了%d次！", _index);
                loger.info (strLog);
            } catch (Exception e) {
                loger.error ("数据库连接失败！");
                e.printStackTrace();
            }
        }
        return _con;
    }

    public static void Close(Connection con) {
        if (null != con) {
            try {
                con.close ();
            } catch (SQLException e) {
                loger.error ("关闭数据库连接失败！");
                e.printStackTrace ();
            }
        }
    }

    public static void Close(Statement state) {
        if (null != state) {
            try {
                state.close ();
            } catch (SQLException e) {
                loger.error ("关闭Statement连接失败！");
                e.printStackTrace ();
            }
        }
    }

    public static void Close(ResultSet rst) {
        if (null != rst) {
            try {
                rst.close ();
            } catch (SQLException e) {
                loger.error ("关闭ResultSet失败！");
                e.printStackTrace ();
            }
        }
    }

}
