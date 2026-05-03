<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password | SafeHer</title>
    <link rel="stylesheet" href="css/style.css">
    <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
</head>
<body>

    <nav class="navbar">
        <a href="index.jsp" class="nav-brand">
            <ion-icon name="shield-checkmark"></ion-icon> SafeHer
        </a>
        <div class="nav-links">
            <a href="login.jsp">Back to <strong>Login</strong></a>
        </div>
    </nav>

    <div class="auth-wrapper">
        <div class="auth-container">
            <div class="forgot-icon-wrapper">
                <div class="forgot-icon-circle">
                    <ion-icon name="lock-open-outline"></ion-icon>
                </div>
            </div>
            <h2>Forgot Password?</h2>
            <p class="auth-subtitle">No worries! Enter your registered email and we'll send you a reset code.</p>

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

            <form action="forgot-password" method="POST" id="forgotForm">
                <div class="form-group">
                    <label for="email">Email Address</label>
                    <input type="email" id="email" name="email" required placeholder="Enter your registered email">
                </div>

                <button type="submit" class="btn btn-primary w-full mt-2" id="submitBtn" style="padding: 0.9rem; font-size: 1rem; justify-content: center;">
                    <ion-icon name="mail-outline"></ion-icon> Send Reset Code
                </button>
            </form>

            <p class="auth-link" style="margin-top: 2rem;">
                Remember your password? <a href="login.jsp">Sign In</a>
            </p>
        </div>
    </div>

    <script>
        document.getElementById('forgotForm').addEventListener('submit', function() {
            var btn = document.getElementById('submitBtn');
            btn.innerHTML = '<ion-icon name="hourglass-outline"></ion-icon> Sending...';
            btn.disabled = true;
            btn.style.opacity = '0.7';
        });
    </script>

</body>
</html>
