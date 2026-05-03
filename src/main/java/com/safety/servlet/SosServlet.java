package com.safety.servlet;

import com.safety.model.User;
import com.safety.util.DBConnection;
import com.safety.util.EmailService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/sos")
public class SosServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("unauthorized");
            return;
        }

        String lat = request.getParameter("latitude");
        String lng = request.getParameter("longitude");

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            response.getWriter().write("error");
            return;
        }

        try {
            conn.setAutoCommit(false);

            // 1. Insert the SOS alert record
            String alertSql = "INSERT INTO sos_alerts (user_id, location_lat, location_lng, status) VALUES (?, ?, ?, 'Active')";
            PreparedStatement alertPs = conn.prepareStatement(alertSql, Statement.RETURN_GENERATED_KEYS);
            alertPs.setInt(1, user.getUserId());
            alertPs.setString(2, lat);
            alertPs.setString(3, lng);

            int result = alertPs.executeUpdate();
            if (result <= 0) {
                conn.rollback();
                response.getWriter().write("error");
                return;
            }

            ResultSet generatedKeys = alertPs.getGeneratedKeys();
            int alertId = -1;
            if (generatedKeys.next()) {
                alertId = generatedKeys.getInt(1);
            }

            // 2. Fetch all emergency contacts for this user
            String contactSql = "SELECT contact_id, contact_name, contact_number FROM emergency_contacts WHERE user_id = ?";
            PreparedStatement contactPs = conn.prepareStatement(contactSql);
            contactPs.setInt(1, user.getUserId());
            ResultSet contactRs = contactPs.executeQuery();

            // 3. Loop through each contact and send an alert
            String recipientSql = "INSERT INTO sos_alert_recipients (alert_id, contact_id, contact_name, contact_number, delivery_status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement recipientPs = conn.prepareStatement(recipientSql);

            int contactsNotified = 0;
            while (contactRs.next()) {
                int contactId = contactRs.getInt("contact_id");
                String contactName = contactRs.getString("contact_name");
                String contactEmailInfo = contactRs.getString("contact_number");

                // Send emergency alert via Email service
                boolean sent = EmailService.sendSOSAlert(
                        contactName, contactEmailInfo,
                        user.getName(), user.getPhoneNo(),
                        lat, lng
                );

                // Record the recipient and delivery status
                recipientPs.setInt(1, alertId);
                recipientPs.setInt(2, contactId);
                recipientPs.setString(3, contactName);
                recipientPs.setString(4, contactEmailInfo);
                recipientPs.setString(5, sent ? "SENT" : "FAILED");
                recipientPs.addBatch();

                if (sent) contactsNotified++;
            }
            recipientPs.executeBatch();

            conn.commit();

            // Return a success response with the count of notified contacts
            response.setContentType("text/plain");
            response.getWriter().write("success:" + contactsNotified);

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            response.getWriter().write("error");
        } finally {
            try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
