<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | SafeHer</title>
    <link rel="stylesheet" href="css/style.css">
    <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
</head>
<body>

    <nav class="navbar">
        <a href="index.jsp" class="nav-brand">
            <ion-icon name="shield-checkmark"></ion-icon> SafeHer
        </a>
        <div class="nav-links">
            <a href="register.jsp">Don't have an account? <strong>Register</strong></a>
        </div>
    </nav>

    <div class="auth-wrapper">
        <div class="auth-container">
            <h2>Welcome Back</h2>
            <p class="auth-subtitle">Sign in to access your safety dashboard.</p>

            <%
                String error = request.getParameter("error");
                String success = request.getParameter("success");
                if(error != null) {
            %>
                <div class="alert alert-error">
                    <ion-icon name="alert-circle-outline"></ion-icon> <%= error %>
                </div>
            <% }
                if(success != null) {
            %>
                <div class="alert alert-success">
                    <ion-icon name="checkmark-circle-outline"></ion-icon> <%= success %>
                </div>
            <% } %>

            <form action="login" method="POST">
                <div class="form-group">
                    <label for="email">Email Address</label>
                    <input type="email" id="email" name="email" required placeholder="Enter your email">
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" required placeholder="Enter your password">
                </div>

                <button type="submit" class="btn btn-primary w-full mt-2" style="padding: 0.9rem; font-size: 1rem; justify-content: center;">
                    <ion-icon name="log-in-outline"></ion-icon> Sign In
                </button>
            </form>

            <p class="auth-link forgot-link">
                <a href="forgot_password.jsp"><ion-icon name="key-outline"></ion-icon> Forgot Password?</a>
            </p>

            <p class="auth-link">
                New here? <a href="register.jsp">Create an account</a>
            </p>

            <div style="margin-top: 1.5rem; padding-top: 1rem; border-top: 1px solid var(--border-light); text-align: center;">
                <p style="font-size: 0.8rem; color: var(--text-muted);">
                    <strong>Admin Login:</strong> admin@safetyapp.com / admin123
                </p>
            </div>
        </div>
    </div>

</body>
</html>
