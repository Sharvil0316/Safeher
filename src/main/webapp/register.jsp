<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register | SafeHer</title>
    <link rel="stylesheet" href="css/style.css">
    <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
</head>
<body>

    <nav class="navbar">
        <a href="index.jsp" class="nav-brand">
            <ion-icon name="shield-checkmark"></ion-icon> SafeHer
        </a>
        <div class="nav-links">
            <a href="login.jsp">Already have an account? <strong>Login</strong></a>
        </div>
    </nav>

    <div class="auth-wrapper">
        <div class="auth-container" style="max-width: 580px;">
            <h2>Create Your Account</h2>
            <p class="auth-subtitle">Register with your emergency contacts for instant safety.</p>

            <%
                String error = request.getParameter("error");
                if(error != null) {
            %>
                <div class="alert alert-error">
                    <ion-icon name="alert-circle-outline"></ion-icon> <%= error %>
                </div>
            <% } %>

            <form action="register" method="POST" id="registerForm">

                <!-- Personal Details -->
                <div class="form-group">
                    <label for="name">Full Name</label>
                    <input type="text" id="name" name="name" required placeholder="Enter your full name">
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="email">Email Address</label>
                        <input type="email" id="email" name="email" required placeholder="you@email.com">
                    </div>
                    <div class="form-group">
                        <label for="phoneNo">Phone Number</label>
                        <input type="tel" id="phoneNo" name="phoneNo" required placeholder="e.g. 9876543210"
                               pattern="[0-9]{10}" title="Enter a valid 10-digit phone number">
                    </div>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" required placeholder="Create a strong password" minlength="6">
                </div>

                <!-- Emergency Contacts Section -->
                <div class="contact-section">
                    <h3>
                        <ion-icon name="people-outline"></ion-icon> Emergency Contacts
                    </h3>
                    <p class="hint">Add 2 to 3 trusted contacts. They will be alerted during an SOS.</p>

                    <!-- Contact 1 (Required) -->
                    <div class="contact-block">
                        <span class="label-tag">Contact 1 (Required)</span>
                        <div class="form-row">
                            <div class="form-group">
                                <label>Name</label>
                                <input type="text" name="contactName" required placeholder="e.g. Mother">
                            </div>
                            <div class="form-group">
                                <label>Phone</label>
                                <input type="tel" name="contactNumber" required placeholder="e.g. 9876543210"
                                       pattern="[0-9]{10}" title="Enter a valid 10-digit phone number">
                            </div>
                        </div>
                    </div>

                    <!-- Contact 2 (Required) -->
                    <div class="contact-block">
                        <span class="label-tag">Contact 2 (Required)</span>
                        <div class="form-row">
                            <div class="form-group">
                                <label>Name</label>
                                <input type="text" name="contactName" required placeholder="e.g. Father">
                            </div>
                            <div class="form-group">
                                <label>Phone</label>
                                <input type="tel" name="contactNumber" required placeholder="e.g. 9876543210"
                                       pattern="[0-9]{10}" title="Enter a valid 10-digit phone number">
                            </div>
                        </div>
                    </div>

                    <!-- Contact 3 (Optional) -->
                    <div class="contact-block">
                        <span class="label-tag optional-tag">Contact 3 (Optional)</span>
                        <div class="form-row">
                            <div class="form-group">
                                <label>Name</label>
                                <input type="text" name="contactName" placeholder="e.g. Friend">
                            </div>
                            <div class="form-group">
                                <label>Phone</label>
                                <input type="tel" name="contactNumber" placeholder="e.g. 9876543210"
                                       pattern="[0-9]{10}" title="Enter a valid 10-digit phone number">
                            </div>
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary w-full mt-3" style="padding: 0.9rem; font-size: 1rem; justify-content: center;">
                    <ion-icon name="checkmark-circle-outline"></ion-icon> Register Account
                </button>
            </form>

            <p class="auth-link">
                Already registered? <a href="login.jsp">Login here</a>
            </p>
        </div>
    </div>

</body>
</html>
