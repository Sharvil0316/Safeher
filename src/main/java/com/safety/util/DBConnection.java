package com.safety.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Read from environment variables, fallback to local for development
            String url = System.getenv("DB_URL");
            if (url == null || url.trim().isEmpty()) {
                url = "jdbc:mysql://localhost:3308/women_safety_db";
            }
            
            String user = System.getenv("DB_USER");
            if (user == null || user.trim().isEmpty()) {
                user = "root";
            }
            
            String password = System.getenv("DB_PASSWORD");
            if (password == null || password.trim().isEmpty()) {
                password = "SSpatil";
            }

            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database Connection Error: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
}
