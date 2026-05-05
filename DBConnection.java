package com.safety.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Read from Railway cloud environment variables
            String host = System.getenv("MYSQLHOST");
            String port = System.getenv("MYSQLPORT");
            String dbName = System.getenv("MYSQLDATABASE");
            
            String url;
            if (host != null && !host.trim().isEmpty()) {
                // We are inside Railway cloud
                url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
            } else {
                // We are on localhost, fallback to Railway Public External URL
                url = "jdbc:mysql://tramway.proxy.rlwy.net:36182/railway";
            }
            
            String user = System.getenv("MYSQLUSER");
            if (user == null || user.trim().isEmpty()) {
                user = "root";
            }
            
            String password = System.getenv("MYSQLPASSWORD");
            if (password == null || password.trim().isEmpty()) {
                password = "QaVJxEBxZtJjdnUvcGbtAYLTjdjostKX";
            }

            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database Connection Error: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
}
