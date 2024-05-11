package model.filtererr.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtil {
    private static Connection conn = null;

    public  Connection getConn(String ip,String port, String database, String username, String password) {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://" + ip + ":" + port + "/" + database +
                "?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&";
        try {
            //获取连接对象Connection
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            close();
        }
        return conn;
    }

    public  void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean testConn(String ip,String port, String database, String username, String password) {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://" + ip + ":" + port + "/" + database +
                "?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&";
        try {
            //获取连接对象Connection
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
