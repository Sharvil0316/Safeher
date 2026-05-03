package com.safety.servlet;

import com.safety.util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String resetCode = request.getParameter("resetCode");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validation
        if (email == null || email.trim().isEmpty()) {
            response.sendRedirect("forgot_password.jsp?error=Session expired. Please request a new reset code.");
            return;
        }

        if (resetCode == null || resetCode.trim().isEmpty()) {
            response.sendRedirect("reset_password.jsp?email=" + encode(email) + "&error=Please enter the reset code.");
            return;
        }

        if (newPassword == null || newPassword.length() < 6) {
            response.sendRedirect("reset_password.jsp?email=" + encode(email) + "&error=Password must be at least 6 characters long.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            response.sendRedirect("reset_password.jsp?email=" + encode(email) + "&error=Passwords do not match.");
            return;
        }

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            response.sendRedirect("reset_password.jsp?email=" + encode(email) + "&error=Database connection failed.");
            return;
        }

        try {
            // Verify the reset code and check expiry
            String verifySql = "SELECT prt.user_id FROM password_reset_tokens prt "
                    + "JOIN users u ON prt.user_id = u.user_id "
                    + "WHERE u.email = ? AND prt.token = ? AND prt.expires_at > NOW()";
            PreparedStatement verifyPs = conn.prepareStatement(verifySql);
            verifyPs.setString(1, email);
            verifyPs.setString(2, resetCode);
            ResultSet rs = verifyPs.executeQuery();

            if (!rs.next()) {
                response.sendRedirect("reset_password.jsp?email=" + encode(email)
                        + "&error=Invalid or expired reset code. Please request a new one.");
                return;
            }

            int userId = rs.getInt("user_id");

            // Update the user's password
            String updateSql = "UPDATE users SET password = ? WHERE user_id = ?";
            PreparedStatement updatePs = conn.prepareStatement(updateSql);
            updatePs.setString(1, newPassword);
            updatePs.setInt(2, userId);
            int rowsUpdated = updatePs.executeUpdate();

            if (rowsUpdated > 0) {
                // Delete all reset tokens for this user (clean up)
                String deleteSql = "DELETE FROM password_reset_tokens WHERE user_id = ?";
                PreparedStatement deletePs = conn.prepareStatement(deleteSql);
                deletePs.setInt(1, userId);
                deletePs.executeUpdate();

                response.sendRedirect("login.jsp?success=Password reset successful! You can now login with your new password.");
            } else {
                response.sendRedirect("reset_password.jsp?email=" + encode(email)
                        + "&error=Failed to update password. Please try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("reset_password.jsp?email=" + encode(email)
                    + "&error=An error occurred. Please try again.");
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private String encode(String value) {
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }
}
