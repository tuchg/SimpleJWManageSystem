package cn.wchihc.jwc.dao.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    public static final String DATABASE = "JWC";

    static {
        //全局注册驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn() {
        Connection conn = null;
        try {
            // 加载mysql驱动
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + DATABASE + "?useUnicode=true&characterEncoding=UTF8",
                    "root",
                    "a123456");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
