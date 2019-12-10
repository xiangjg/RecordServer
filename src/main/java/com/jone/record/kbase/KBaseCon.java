
/**
 * create by dugg 20191102
 * KBase链接类，执行KBase链接
 */

package com.jone.record.kbase;

import com.jone.record.kbase.entity.KBaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class KBaseCon {

    private static final Logger loger = LoggerFactory.getLogger (KBaseCon.class);
    private static Connection _con = null;
    private static int _index = 0;
    private static String strLog = "";

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
}
