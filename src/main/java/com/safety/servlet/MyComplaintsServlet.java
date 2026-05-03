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

/**
 * Returns JSON list of the logged-in user's own complaints.
 */
@WebServlet("/myComplaints")
public class MyComplaintsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Map<String, String>> complaints = new ArrayList<>();
        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            try {
                String sql = "SELECT complaint_id, complaint_type, location, status, created_at "
                        + "FROM complaints WHERE user_id = ? ORDER BY created_at DESC";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, user.getUserId());
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Map<String, String> comp = new HashMap<>();
                    comp.put("complaintId", String.valueOf(rs.getInt("complaint_id")));
                    comp.put("type", rs.getString("complaint_type"));
                    comp.put("location", rs.getString("location"));
                    comp.put("status", rs.getString("status"));
                    comp.put("time", rs.getString("created_at"));
                    complaints.add(comp);
                }

                response.getWriter().write(new Gson().toJson(complaints));

            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } finally {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}
