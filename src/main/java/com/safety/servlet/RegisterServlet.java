package com.safety.servlet;

import com.safety.util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phoneNo = request.getParameter("phoneNo");

        // Emergency contact fields (2 mandatory + 1 optional)
        String[] contactNames = request.getParameterValues("contactName");
        String[] contactNumbers = request.getParameterValues("contactNumber");

        // Server-side validation: at least 2 emergency contacts required
        int validContactCount = 0;
        if (contactNames != null && contactNumbers != null) {
            for (int i = 0; i < contactNames.length; i++) {
                if (contactNames[i] != null && !contactNames[i].trim().isEmpty()
                        && contactNumbers[i] != null && !contactNumbers[i].trim().isEmpty()) {
                    validContactCount++;
                }
            }
        }

        if (validContactCount < 2) {
            response.sendRedirect("register.jsp?error=At least 2 emergency contacts are required.");
            return;
        }

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            response.sendRedirect("register.jsp?error=Database connection failed.");
            return;
        }

        try {
            conn.setAutoCommit(false); // Begin transaction

            // 1. Insert user
            String userSql = "INSERT INTO users (name, email, password, phone_no, role) VALUES (?, ?, ?, ?, 'USER')";
            PreparedStatement userPs = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            userPs.setString(1, name);
            userPs.setString(2, email);
            userPs.setString(3, password);
            userPs.setString(4, phoneNo);

            int userResult = userPs.executeUpdate();
            if (userResult <= 0) {
                conn.rollback();
                response.sendRedirect("register.jsp?error=Registration failed.");
                return;
            }

            // Get auto-generated user_id
            ResultSet generatedKeys = userPs.getGeneratedKeys();
            int userId = -1;
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            }

            // 2. Insert emergency contacts linked to this user_id
            String contactSql = "INSERT INTO emergency_contacts (user_id, contact_name, contact_number) VALUES (?, ?, ?)";
            PreparedStatement contactPs = conn.prepareStatement(contactSql);

            for (int i = 0; i < contactNames.length; i++) {
                if (contactNames[i] != null && !contactNames[i].trim().isEmpty()
                        && contactNumbers[i] != null && !contactNumbers[i].trim().isEmpty()) {
                    contactPs.setInt(1, userId);
                    contactPs.setString(2, contactNames[i].trim());
                    contactPs.setString(3, contactNumbers[i].trim());
                    contactPs.addBatch();
                }
            }
            contactPs.executeBatch();

            conn.commit(); // Commit transaction
            response.sendRedirect("login.jsp?success=Registration successful! Please login.");

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            if (e.getMessage().contains("Duplicate")) {
                response.sendRedirect("register.jsp?error=An account with this email already exists.");
            } else {
                response.sendRedirect("register.jsp?error=Registration error: " + e.getMessage());
            }
        } finally {
            try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
