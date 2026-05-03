package com.safety.servlet;

import com.safety.util.DBConnection;
import com.safety.util.EmailService;

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
import java.util.Random;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            response.sendRedirect("forgot_password.jsp?error=Please enter your email address.");
            return;
        }

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            response.sendRedirect("forgot_password.jsp?error=Database connection failed. Please try again.");
            return;
        }

        try {
            // Check if user exists with this email
            String checkSql = "SELECT user_id, name FROM users WHERE email = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setString(1, email);
            ResultSet rs = checkPs.executeQuery();

            if (!rs.next()) {
                response.sendRedirect("forgot_password.jsp?error=No account found with this email address.");
                return;
            }

            String userName = rs.getString("name");
            int userId = rs.getInt("user_id");

            // Generate a 6-digit reset code
            String resetCode = generateResetCode();

            // Delete any old reset tokens for this user
            String deleteSql = "DELETE FROM password_reset_tokens WHERE user_id = ?";
            PreparedStatement deletePs = conn.prepareStatement(deleteSql);
            deletePs.setInt(1, userId);
            deletePs.executeUpdate();

            // Insert new reset token (expires in 15 minutes)
            String insertSql = "INSERT INTO password_reset_tokens (user_id, token, expires_at) VALUES (?, ?, DATE_ADD(NOW(), INTERVAL 15 MINUTE))";
            PreparedStatement insertPs = conn.prepareStatement(insertSql);
            insertPs.setInt(1, userId);
            insertPs.setString(2, resetCode);
            insertPs.executeUpdate();

            // Send the reset code via email
            boolean emailSent = sendResetEmail(email, userName, resetCode);

            if (emailSent) {
                response.sendRedirect("reset_password.jsp?email=" + java.net.URLEncoder.encode(email, "UTF-8")
                        + "&success=A 6-digit reset code has been sent to your email.");
            } else {
                response.sendRedirect("forgot_password.jsp?error=Failed to send reset email. Please try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("forgot_password.jsp?error=An error occurred. Please try again.");
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * Generates a random 6-digit numeric code.
     */
    private String generateResetCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 to 999999
        return String.valueOf(code);
    }

    /**
     * Sends a password reset email with a nicely formatted message.
     */
    private boolean sendResetEmail(String recipientEmail, String userName, String resetCode) {
        String subject = "🔐 SafeHer - Password Reset Code";
        String message = "Hi " + userName + ",\n\n"
                + "You requested to reset your password for your SafeHer account.\n\n"
                + "Your Password Reset Code is:\n\n"
                + "    " + resetCode + "\n\n"
                + "This code will expire in 15 minutes.\n\n"
                + "If you did not request this, please ignore this email.\n\n"
                + "Stay safe,\n"
                + "SafeHer Security Team";

        return EmailService.sendEmail(recipientEmail, subject, message);
    }
}
