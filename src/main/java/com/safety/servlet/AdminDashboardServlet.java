package com.safety.servlet;

import com.safety.model.User;
import com.safety.util.DBConnection;
import com.google.gson.Gson;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/admin/*")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> data = new HashMap<>();
        List<Map<String, String>> alerts = new ArrayList<>();
        List<Map<String, String>> complaints = new ArrayList<>();
        Map<String, Integer> stats = new HashMap<>();

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                // Fetch active SOS Alerts with user info
                String alertSql = "SELECT s.*, u.name, u.phone_no FROM sos_alerts s "
                        + "JOIN users u ON s.user_id = u.user_id ORDER BY s.created_at DESC";
                PreparedStatement ps1 = conn.prepareStatement(alertSql);
                ResultSet rs1 = ps1.executeQuery();
                int activeAlerts = 0;
                while (rs1.next()) {
                    Map<String, String> alert = new HashMap<>();
                    alert.put("alertId", String.valueOf(rs1.getInt("alert_id")));
                    alert.put("userName", rs1.getString("name"));
                    alert.put("userPhone", rs1.getString("phone_no"));
                    alert.put("lat", rs1.getString("location_lat"));
                    alert.put("lng", rs1.getString("location_lng"));
                    alert.put("status", rs1.getString("status"));
                    alert.put("time", rs1.getString("created_at"));
                    alerts.add(alert);
                    if ("Active".equals(rs1.getString("status"))) activeAlerts++;
                }

                // Fetch all Complaints with user info
                String compSql = "SELECT c.*, u.name, u.phone_no FROM complaints c "
                        + "JOIN users u ON c.user_id = u.user_id ORDER BY c.created_at DESC";
                PreparedStatement ps2 = conn.prepareStatement(compSql);
                ResultSet rs2 = ps2.executeQuery();
                int pendingComplaints = 0;
                int resolvedComplaints = 0;
                while (rs2.next()) {
                    Map<String, String> comp = new HashMap<>();
                    comp.put("complaintId", String.valueOf(rs2.getInt("complaint_id")));
                    comp.put("userName", rs2.getString("name"));
                    comp.put("type", rs2.getString("complaint_type"));
                    comp.put("desc", rs2.getString("description"));
                    comp.put("location", rs2.getString("location"));
                    comp.put("status", rs2.getString("status"));
                    comp.put("time", rs2.getString("created_at"));
                    complaints.add(comp);
                    if ("Pending".equals(rs2.getString("status"))) pendingComplaints++;
                    if ("Resolved".equals(rs2.getString("status"))) resolvedComplaints++;
                }

                // Get total registered users count
                String userCountSql = "SELECT COUNT(*) FROM users WHERE role = 'USER'";
                PreparedStatement ps3 = conn.prepareStatement(userCountSql);
                ResultSet rs3 = ps3.executeQuery();
                rs3.next();
                int totalUsers = rs3.getInt(1);

                stats.put("activeAlerts", activeAlerts);
                stats.put("pendingComplaints", pendingComplaints);
                stats.put("resolvedComplaints", resolvedComplaints);
                stats.put("totalUsers", totalUsers);

                data.put("alerts", alerts);
                data.put("complaints", complaints);
                data.put("stats", stats);

                response.getWriter().write(new Gson().toJson(data));

            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } finally {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String action = request.getParameter("action");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Database connection failed\"}");
            return;
        }

        try {
            if ("updateComplaintStatus".equals(action)) {
                String complaintId = request.getParameter("complaintId");
                String status = request.getParameter("status");

                String sql = "UPDATE complaints SET status = ? WHERE complaint_id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, status);
                ps.setInt(2, Integer.parseInt(complaintId));
                ps.executeUpdate();

                response.getWriter().write("{\"status\":\"success\",\"message\":\"Complaint status updated to " + status + "\"}");

            } else if ("resolveAlert".equals(action)) {
                String alertId = request.getParameter("alertId");

                String sql = "UPDATE sos_alerts SET status = 'Resolved' WHERE alert_id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(alertId));
                ps.executeUpdate();

                response.getWriter().write("{\"status\":\"success\",\"message\":\"Alert marked as resolved\"}");
            } else {
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid action\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
