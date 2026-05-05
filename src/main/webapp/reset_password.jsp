<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password | SafeHer</title>
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
                <div class="forgot-icon-circle success-icon">
                    <ion-icon name="key-outline"></ion-icon>
                </div>
            </div>
            <h2>Reset Password</h2>
            <p class="auth-subtitle">Enter the 6-digit code sent to your email and choose a new password.</p>

            <%
                String error = request.getParameter("error");
                String success = request.getParameter("success");
                String email = request.getParameter("email");
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

            <form action="reset-password" method="POST" id="resetForm">
                <input type="hidden" name="email" value="<%= email != null ? email : "" %>">

                <div class="form-group">
                    <label for="resetCode">6-Digit Reset Code</label>
                    <input type="text" id="resetCode" name="resetCode" required
                           placeholder="Enter the code from your email"
                           maxlength="6" pattern="[0-9]{6}"
                           style="text-align: center; font-size: 1.4rem; font-weight: 700; letter-spacing: 0.5em;"
                           autocomplete="off">
                </div>

                <div class="form-group">
                    <label for="newPassword">New Password</label>
                    <input type="password" id="newPassword" name="newPassword" required
                           placeholder="Enter new password" minlength="6">
                </div>

                <div class="form-group">
                    <label for="confirmPassword">Confirm New Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required
                           placeholder="Confirm new password" minlength="6">
                </div>

                <div id="passwordError" class="alert alert-error" style="display: none;">
                    <ion-icon name="alert-circle-outline"></ion-icon> Passwords do not match!
                </div>

                <button type="submit" class="btn btn-primary w-full mt-2" id="resetBtn" style="padding: 0.9rem; font-size: 1rem; justify-content: center;">
                    <ion-icon name="checkmark-circle-outline"></ion-icon> Reset Password
                </button>
            </form>

            <p class="auth-link" style="margin-top: 1.5rem;">
                <a href="forgot_password.jsp"><ion-icon name="refresh-outline"></ion-icon> Resend Code</a>
            </p>

            <p class="auth-link">
                Remember your password? <a href="login.jsp">Sign In</a>
            </p>
        </div>
    </div>

    <script>
        document.getElementById('resetForm').addEventListener('submit', function(e) {
            var pw = document.getElementById('newPassword').value;
            var cpw = document.getElementById('confirmPassword').value;
            var errDiv = document.getElementById('passwordError');

            if (pw !== cpw) {
                e.preventDefault();
                errDiv.style.display = 'flex';
                document.getElementById('confirmPassword').focus();
                return false;
            }
            errDiv.style.display = 'none';

            var btn = document.getElementById('resetBtn');
            btn.innerHTML = '<ion-icon name="hourglass-outline"></ion-icon> Resetting...';
            btn.disabled = true;
            btn.style.opacity = '0.7';
        });

        // Auto-hide password error on typing
        document.getElementById('confirmPassword').addEventListener('input', function() {
            document.getElementById('passwordError').style.display = 'none';
        });

        // Only allow digits in reset code
        document.getElementById('resetCode').addEventListener('input', function() {
            this.value = this.value.replace(/[^0-9]/g, '');
        });
    </script>

</body>
</html>
