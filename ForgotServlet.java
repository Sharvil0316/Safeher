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
import java.util.UUID;

@WebServlet("/forgotPassword")
public class ForgotServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            response.sendRedirect("forgot_password.jsp?error=Please enter your email address.");
            return;
        }

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                // Check if user exists
                String checkSql = "SELECT name FROM users WHERE email = ?";
                PreparedStatement checkPs = conn.prepareStatement(checkSql);
                checkPs.setString(1, email);
                ResultSet rs = checkPs.executeQuery();

                if (rs.next()) {
                    String userName = rs.getString("name");
                    
                    // Generate a temporary 8-character password
                    String tempPassword = UUID.randomUUID().toString().substring(0, 8);

                    // Update the password in the database
                    String updateSql = "UPDATE users SET password = ? WHERE email = ?";
                    PreparedStatement updatePs = conn.prepareStatement(updateSql);
                    updatePs.setString(1, tempPassword);
                    updatePs.setString(2, email);
                    
                    int rowsAffected = updatePs.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        // Send the email using our existing EmailService
                        String subject = "SafeHer: Password Reset";
                        String message = "Hi " + userName + ",\n\n"
                                + "You requested a password reset for your SafeHer account.\n\n"
                                + "Your temporary password is: " + tempPassword + "\n\n"
                                + "Please login using this temporary password, and then immediately go to the Settings page to change it to something secure.\n\n"
                                + "Stay safe,\nThe SafeHer Team";

                        boolean emailSent = EmailService.sendEmail(email, subject, message);
                        
                        if (emailSent) {
                            response.sendRedirect("login.jsp?success=A temporary password has been sent to your email!");
                        } else {
                            response.sendRedirect("forgot_password.jsp?error=Failed to send email. Please check server configuration.");
                        }
                    } else {
                        response.sendRedirect("forgot_password.jsp?error=Failed to update password. Try again.");
                    }
                } else {
                    response.sendRedirect("forgot_password.jsp?error=No account found with that email address.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("forgot_password.jsp?error=Database error occurred.");
            } finally {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        } else {
            response.sendRedirect("forgot_password.jsp?error=Database connection failed.");
        }
    }
}
