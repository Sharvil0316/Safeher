<%@ page import="com.safety.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("user");
    if(user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Settings | SafeHer</title>
    <link rel="stylesheet" href="css/style.css">
    <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
    <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
</head>
<body>

    <nav class="navbar">
        <a href="dashboard.jsp" class="nav-brand">
            <ion-icon name="shield-checkmark"></ion-icon> SafeHer
        </a>
        <div class="nav-links">
            <a href="dashboard.jsp" style="color: var(--text-dark); margin-right: 15px; font-weight: 500;">
                <ion-icon name="arrow-back-outline" style="vertical-align: middle;"></ion-icon> Back to Dashboard
            </a>
            <span class="nav-user">
                <ion-icon name="person-circle-outline" style="font-size: 1.2rem; vertical-align: middle;"></ion-icon>
                Hello, <strong><%= user.getName() %></strong>
            </span>
            <a href="logout" style="color: var(--sos); font-weight: 600;">
                <ion-icon name="log-out-outline" style="vertical-align: middle;"></ion-icon> Logout
            </a>
        </div>
    </nav>

    <div class="container" style="max-width: 600px; margin-top: 3rem;">
        
        <%
            String error = request.getParameter("error");
            String msg = request.getParameter("msg");
            if(error != null) {
        %>
            <div class="alert alert-error">
                <ion-icon name="alert-circle-outline"></ion-icon> <%= error %>
            </div>
        <% }
            if(msg != null) {
        %>
            <div class="alert alert-success">
                <ion-icon name="checkmark-circle-outline"></ion-icon> <%= msg %>
            </div>
        <% } %>

        <div class="card glass-effect">
            <h2 class="section-title" style="margin-bottom: 0.5rem;">Account Settings</h2>
            <p style="color: var(--text-light); margin-bottom: 2rem; font-size: 0.9rem;">
                Update your personal information below. Your emergency contacts will stay safe.
            </p>

            <form action="updateProfile" method="POST">
                <div class="form-group">
                    <label>Full Name</label>
                    <input type="text" name="name" value="<%= user.getName() %>" required style="padding: 0.8rem;">
                </div>
                
                <div class="form-group">
                    <label>Email Address</label>
                    <input type="email" name="email" value="<%= user.getEmail() %>" required style="padding: 0.8rem;">
                </div>
                
                <div class="form-group">
                    <label>Phone Number</label>
                    <input type="tel" name="phone_no" value="<%= user.getPhoneNo() %>" required pattern="[0-9]+" style="padding: 0.8rem;">
                </div>
                
                <div class="form-group" style="margin-bottom: 2rem;">
                    <label>New Password (Optional)</label>
                    <input type="password" name="password" placeholder="Leave blank to keep your current password" style="padding: 0.8rem;">
                </div>
                
                <button type="submit" class="btn btn-primary w-full" style="justify-content: center; padding: 0.9rem; font-size: 1rem;">
                    Update Profile
                </button>
            </form>
        </div>
    </div>

</body>
</html>
