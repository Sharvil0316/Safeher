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
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles CRUD operations for emergency contacts.
 * GET  /contacts - returns JSON list of the user's emergency contacts
 * POST /contacts - adds or updates an emergency contact
 */
@WebServlet("/contacts")
public class ContactServlet extends HttpServlet {

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

        List<Map<String, String>> contacts = new ArrayList<>();
        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            try {
                String sql = "SELECT contact_id, contact_name, contact_number FROM emergency_contacts WHERE user_id = ? ORDER BY contact_id";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, user.getUserId());
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Map<String, String> contact = new HashMap<>();
                    contact.put("contactId", String.valueOf(rs.getInt("contact_id")));
                    contact.put("contactName", rs.getString("contact_name"));
                    contact.put("contactNumber", rs.getString("contact_number"));
                    contacts.add(contact);
                }

                response.getWriter().write(new Gson().toJson(contacts));

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

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            deleteContact(request, response, user);
        } else if ("update".equals(action)) {
            updateContact(request, response, user);
        } else {
            addContact(request, response, user);
        }
    }

    private void addContact(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {
        String name = request.getParameter("contactName");
        String number = request.getParameter("contactNumber");

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                // Check if user already has 3 contacts
                String countSql = "SELECT COUNT(*) FROM emergency_contacts WHERE user_id = ?";
                PreparedStatement countPs = conn.prepareStatement(countSql);
                countPs.setInt(1, user.getUserId());
                ResultSet countRs = countPs.executeQuery();
                countRs.next();
                int count = countRs.getInt(1);

                if (count >= 3) {
                    response.sendRedirect("dashboard.jsp?error=Maximum 3 emergency contacts allowed.");
                    return;
                }

                String sql = "INSERT INTO emergency_contacts (user_id, contact_name, contact_number) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, user.getUserId());
                ps.setString(2, name);
                ps.setString(3, number);
                ps.executeUpdate();

                response.sendRedirect("dashboard.jsp?msg=Contact added successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("dashboard.jsp?error=Failed to add contact.");
            } finally {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    private void updateContact(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {
        String contactId = request.getParameter("contactId");
        String name = request.getParameter("contactName");
        String number = request.getParameter("contactNumber");

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                String sql = "UPDATE emergency_contacts SET contact_name = ?, contact_number = ? WHERE contact_id = ? AND user_id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, number);
                ps.setInt(3, Integer.parseInt(contactId));
                ps.setInt(4, user.getUserId());
                ps.executeUpdate();

                response.sendRedirect("dashboard.jsp?msg=Contact updated.");
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("dashboard.jsp?error=Failed to update contact.");
            } finally {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    private void deleteContact(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {
        String contactId = request.getParameter("contactId");

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                // Prevent deletion if user has only 2 contacts (minimum)
                String countSql = "SELECT COUNT(*) FROM emergency_contacts WHERE user_id = ?";
                PreparedStatement countPs = conn.prepareStatement(countSql);
                countPs.setInt(1, user.getUserId());
                ResultSet countRs = countPs.executeQuery();
                countRs.next();
                int count = countRs.getInt(1);

                if (count <= 2) {
                    response.sendRedirect("dashboard.jsp?error=Minimum 2 emergency contacts required. Cannot delete.");
                    return;
                }

                String sql = "DELETE FROM emergency_contacts WHERE contact_id = ? AND user_id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(contactId));
                ps.setInt(2, user.getUserId());
                ps.executeUpdate();

                response.sendRedirect("dashboard.jsp?msg=Contact removed.");
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("dashboard.jsp?error=Failed to remove contact.");
            } finally {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}
