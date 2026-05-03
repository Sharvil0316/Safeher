<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="SafeHer - A secure emergency SOS platform designed to empower women with rapid-response safety tools, emergency contacts, and incident reporting.">
    <title>SafeHer | Women Safety Application</title>
    <link rel="stylesheet" href="css/style.css">
    <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
    <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
</head>
<body>

    <nav class="navbar">
        <a href="index.jsp" class="nav-brand">
            <ion-icon name="shield-checkmark"></ion-icon> SafeHer
        </a>
        <div class="nav-links">
            <a href="login.jsp">Login</a>
            <a href="register.jsp" class="btn btn-primary">Get Started</a>
        </div>
    </nav>

    <div class="container hero">
        <div class="hero-text">
            <h1>Your Safety, <span>Our Priority.</span></h1>
            <p>Immediate help and fast communication can save lives. SafeHer is a secure, rapid-response platform providing women with a reliable emergency SOS system, trusted contact alerts, and a direct line to authorities.</p>
            <div class="hero-actions">
                <a href="register.jsp" class="btn btn-primary">
                    <ion-icon name="person-add-outline"></ion-icon> Join Now
                </a>
                <a href="#features" class="btn btn-outline">
                    <ion-icon name="information-circle-outline"></ion-icon> Learn More
                </a>
            </div>
        </div>
        <div class="hero-image">
            <img src="hero.png"
                 alt="Women empowerment and safety" style="max-width: 100%; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.1);">
        </div>
    </div>

    <div class="container" id="features">
        <h2 class="text-center mb-3" style="font-size: 2.2rem; font-weight: 800; letter-spacing: -0.02em;">Core Features</h2>
        <div class="feature-grid">
            <div class="feature-card">
                <div class="feature-icon" style="color: var(--sos);">
                    <ion-icon name="alert-circle"></ion-icon>
                </div>
                <h3>One-Click SOS</h3>
                <p>Send an immediate alert with your live location to all your registered emergency contacts instantly.</p>
            </div>
            <div class="feature-card">
                <div class="feature-icon" style="color: var(--primary);">
                    <ion-icon name="document-text"></ion-icon>
                </div>
                <h3>Incident Reports</h3>
                <p>Securely file incident complaints from anywhere and track their progress through the admin system.</p>
            </div>
            <div class="feature-card">
                <div class="feature-icon" style="color: var(--success);">
                    <ion-icon name="people"></ion-icon>
                </div>
                <h3>Emergency Contacts</h3>
                <p>Register up to 3 trusted contacts who are automatically alerted whenever you trigger an SOS.</p>
            </div>
        </div>
    </div>

    <div class="footer">
        <p>&copy; 2026 <a href="index.jsp">SafeHer</a>. Built for safety, powered by trust.</p>
    </div>

</body>
</html>
