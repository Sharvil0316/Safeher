package com.safety.servlet;

import com.safety.model.User;
import com.safety.util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/updateProfile")
public class SettingsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phoneNo = request.getParameter("phone_no");
        String password = request.getParameter("password"); // Optional update

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                String sql;
                PreparedStatement ps;

                // Only update password if a new one is provided
                if (password != null && !password.trim().isEmpty()) {
                    sql = "UPDATE users SET name = ?, email = ?, phone_no = ?, password = ? WHERE user_id = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, name);
                    ps.setString(2, email);
                    ps.setString(3, phoneNo);
                    ps.setString(4, password);
                    ps.setInt(5, currentUser.getUserId());
                } else {
                    sql = "UPDATE users SET name = ?, email = ?, phone_no = ? WHERE user_id = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, name);
                    ps.setString(2, email);
                    ps.setString(3, phoneNo);
                    ps.setInt(4, currentUser.getUserId());
                }

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    // Update session object seamlessly
                    User updatedUser = new User(
                            currentUser.getUserId(),
                            name,
                            email,
                            password != null && !password.trim().isEmpty() ? password : currentUser.getPassword(),
                            phoneNo,
                            currentUser.getRole()
                    );
                    session.setAttribute("user", updatedUser);
                    response.sendRedirect("settings.jsp?msg=Profile updated successfully!");
                } else {
                    response.sendRedirect("settings.jsp?error=Failed to update profile.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("settings.jsp?error=Database error: email may already exist.");
            } finally {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        } else {
            response.sendRedirect("settings.jsp?error=Database connection failed.");
        }
    }
}
