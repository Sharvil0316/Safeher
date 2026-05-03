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
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, email);
                ps.setString(2, password);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_no"),
                        rs.getString("role")
                    );

                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);

                    if ("ADMIN".equals(user.getRole())) {
                        response.sendRedirect("admin_dashboard.jsp");
                    } else {
                        response.sendRedirect("dashboard.jsp");
                    }
                } else {
                    response.sendRedirect("login.jsp?error=Invalid email or password.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("login.jsp?error=Database error occurred.");
            } finally {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        } else {
            response.sendRedirect("login.jsp?error=Database connection failed.");
        }
    }
}
