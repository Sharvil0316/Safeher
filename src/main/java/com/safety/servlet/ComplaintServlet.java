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

@WebServlet("/fileComplaint")
public class ComplaintServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String complaintType = request.getParameter("complaintType");
        String description = request.getParameter("description");
        String location = request.getParameter("location");

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                String sql = "INSERT INTO complaints (user_id, complaint_type, description, location, status) VALUES (?, ?, ?, ?, 'Pending')";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, user.getUserId());
                ps.setString(2, complaintType);
                ps.setString(3, description);
                ps.setString(4, location);

                int result = ps.executeUpdate();
                if (result > 0) {
                    response.sendRedirect("dashboard.jsp?msg=Complaint filed successfully.");
                } else {
                    response.sendRedirect("dashboard.jsp?error=Failed to file complaint.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("dashboard.jsp?error=Error: " + e.getMessage());
            } finally {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        } else {
            response.sendRedirect("dashboard.jsp?error=Database connection failed.");
        }
    }
}
