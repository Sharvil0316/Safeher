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
    <title>Dashboard | SafeHer</title>
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
            <span class="nav-user" style="margin-right: 15px;">
                <ion-icon name="person-circle-outline" style="font-size: 1.2rem; vertical-align: middle;"></ion-icon>
                Hello, <strong><%= user.getName() %></strong>
            </span>
            <a href="settings.jsp" style="color: var(--text-dark); margin-right: 15px; font-weight: 500;">
                <ion-icon name="settings-outline" style="vertical-align: middle;"></ion-icon> Settings
            </a>
            <a href="logout" style="color: var(--sos); font-weight: 600;">
                <ion-icon name="log-out-outline" style="vertical-align: middle;"></ion-icon> Logout
            </a>
        </div>
    </nav>

    <div class="container">

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

        <div class="dashboard-layout">

            <!-- ======== LEFT SIDEBAR ======== -->
            <div class="sidebar-col">

                <!-- SOS Widget -->
                <div class="sos-widget card">
                    <p style="font-size: 0.8rem; color: var(--text-muted); margin-bottom: 1rem; font-weight: 600; text-transform: uppercase; letter-spacing: 0.08em;">Emergency SOS</p>
                    <button id="sosBtn" class="btn-sos">
                        <ion-icon name="alert" style="vertical-align: middle; margin-right: 6px; font-size: 1.3rem;"></ion-icon> SOS
                    </button>
                    <p style="margin-top: 0.8rem;">Press to alert all your emergency contacts</p>
                    <div id="sosStatus"></div>
                </div>

                <!-- Emergency Contacts Card -->
                <div class="card">
                    <h3>
                        <ion-icon name="people-outline" style="color: var(--primary);"></ion-icon>
                        My Emergency Contacts
                    </h3>
                    <div id="contactsList">
                        <p class="text-muted text-center" style="padding: 1rem; font-size: 0.9rem;">Loading contacts...</p>
                    </div>

                    <!-- Add Contact Form (shown when fewer than 3) -->
                    <div id="addContactSection" style="display: none; margin-top: 1rem; padding-top: 1rem; border-top: 1px dashed var(--border);">
                        <form action="contacts" method="POST">
                            <div class="form-row" style="margin-bottom: 0.5rem;">
                                <div class="form-group" style="margin-bottom: 0.5rem;">
                                    <input type="text" name="contactName" required placeholder="Contact Name" style="font-size: 0.85rem; padding: 0.6rem;">
                                </div>
                                <div class="form-group" style="margin-bottom: 0.5rem;">
                                    <input type="email" name="contactNumber" required placeholder="Email Address" style="font-size: 0.85rem; padding: 0.6rem;">
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary btn-sm w-full" style="justify-content: center;">
                                <ion-icon name="add-circle-outline"></ion-icon> Add Contact
                            </button>
                        </form>
                    </div>
                </div>

                <!-- Quick Helplines -->
                <div class="card">
                    <h3>
                        <ion-icon name="call-outline" style="color: var(--success);"></ion-icon>
                        Quick Helplines
                    </h3>
                    <div class="helpline-item">
                        <div class="helpline-icon red">
                            <ion-icon name="woman-outline"></ion-icon>
                        </div>
                        <div class="helpline-text">
                            <span class="label">Women Helpline</span>
                            <span class="number">181</span>
                        </div>
                    </div>
                    <div class="helpline-item">
                        <div class="helpline-icon blue">
                            <ion-icon name="shield-outline"></ion-icon>
                        </div>
                        <div class="helpline-text">
                            <span class="label">Police / Emergency</span>
                            <span class="number">112</span>
                        </div>
                    </div>
                    <div class="helpline-item">
                        <div class="helpline-icon green">
                            <ion-icon name="medical-outline"></ion-icon>
                        </div>
                        <div class="helpline-text">
                            <span class="label">Ambulance</span>
                            <span class="number">108</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- ======== MAIN CONTENT ======== -->
            <div class="main-col">

                <!-- File Complaint Card -->
                <div class="card">
                    <h3>
                        <ion-icon name="document-text-outline" style="color: var(--primary);"></ion-icon>
                        File an Incident Report
                    </h3>
                    <p class="card-muted">Report an incident securely. Authorities will review your case promptly.</p>

                    <form action="fileComplaint" method="POST">
                        <div class="form-group">
                            <label>Type of Incident</label>
                            <select name="complaintType" required>
                                <option value="">Select incident type...</option>
                                <option value="Harassment">Harassment</option>
                                <option value="Stalking">Stalking</option>
                                <option value="Domestic Violence">Domestic Violence</option>
                                <option value="Eve Teasing">Eve Teasing</option>
                                <option value="Cyberbullying">Cyberbullying</option>
                                <option value="Workplace Harassment">Workplace Harassment</option>
                                <option value="Other">Other</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>Location</label>
                            <input type="text" name="location" required placeholder="Area, City or Landmark where this happened">
                        </div>

                        <div class="form-group">
                            <label>Description</label>
                            <textarea name="description" rows="5" required placeholder="Describe the incident in detail. Include time, people involved, and any witnesses..."></textarea>
                        </div>

                        <button type="submit" class="btn btn-primary" style="padding: 0.8rem 2rem;">
                            <ion-icon name="paper-plane-outline"></ion-icon> Submit Report
                        </button>
                    </form>
                </div>

                <!-- My Complaints History -->
                <div class="card mt-3">
                    <h3>
                        <ion-icon name="albums-outline" style="color: var(--warning-text);"></ion-icon>
                        My Complaint History
                    </h3>
                    <div id="myComplaints">
                        <p class="text-muted text-center" style="padding: 1.5rem; font-size: 0.9rem;">Loading your complaints...</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="footer">
        <p>&copy; 2026 <a href="index.jsp">SafeHer</a>. Stay Safe, Stay Strong.</p>
    </div>

    <script>
        // ===========================
        // SOS BUTTON HANDLER
        // ===========================
        document.getElementById('sosBtn').addEventListener('click', function() {
            const statusDiv = document.getElementById('sosStatus');
            const btn = this;

            // Confirm before sending
            if (!confirm('Are you sure you want to trigger an SOS alert? This will notify ALL your emergency contacts.')) {
                return;
            }

            btn.disabled = true;
            btn.style.opacity = '0.7';
            statusDiv.innerHTML = "<span style='color: var(--warning-text);'><ion-icon name='locate-outline'></ion-icon> Getting your location...</span>";

            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(
                    function(position) {
                        sendSOSData(position.coords.latitude, position.coords.longitude);
                    },
                    function(err) {
                        console.error("Error getting location: ", err);
                        alert("Note: GPS signal weak or denied. Sending SOS with limited location info!");
                        sendSOSData("Unknown", "Unknown");
                    },
                    { enableHighAccuracy: true, timeout: 15000, maximumAge: 0 }
                );
            } else {
                sendSOSData("Unknown", "Unknown");
            }
        });

        function sendSOSData(lat, lng) {
            const statusDiv = document.getElementById('sosStatus');
            const btn = document.getElementById('sosBtn');
            statusDiv.innerHTML = "<span style='color: var(--warning-text);'><ion-icon name='radio-outline'></ion-icon> Sending alerts to your contacts...</span>";

            fetch('sos', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'latitude=' + encodeURIComponent(lat) + '&longitude=' + encodeURIComponent(lng)
            })
            .then(res => res.text())
            .then(data => {
                btn.disabled = false;
                btn.style.opacity = '1';
                if(data.startsWith('success')) {
                    const count = data.split(':')[1] || '0';
                    statusDiv.innerHTML = "<span style='color: var(--success);'><ion-icon name='checkmark-circle'></ion-icon> SOS sent to " + count + " contact(s)!</span>";
                } else {
                    statusDiv.innerHTML = "<span style='color: var(--sos);'><ion-icon name='warning'></ion-icon> Failed! Please dial 112 immediately.</span>";
                }
            })
            .catch(err => {
                btn.disabled = false;
                btn.style.opacity = '1';
                statusDiv.innerHTML = "<span style='color: var(--sos);'><ion-icon name='warning'></ion-icon> Network error. Dial 112 immediately!</span>";
            });
        }

        // ===========================
        // LOAD EMERGENCY CONTACTS
        // ===========================
        function loadContacts() {
            fetch('contacts')
            .then(res => res.json())
            .then(contacts => {
                const container = document.getElementById('contactsList');
                const addSection = document.getElementById('addContactSection');

                if (contacts.length === 0) {
                    container.innerHTML = '<p class="text-muted text-center" style="padding: 1rem; font-size: 0.85rem;">No contacts found. Add contacts below.</p>';
                    addSection.style.display = 'block';
                    return;
                }

                let html = '<ul class="contact-list">';
                contacts.forEach(c => {
                    html += '<li class="contact-item">'
                        + '<div class="contact-info">'
                        + '  <span class="name">' + escapeHtml(c.contactName) + '</span>'
                        + '  <span class="phone"><ion-icon name="mail-outline" style="font-size: 0.75rem;"></ion-icon> ' + escapeHtml(c.contactNumber) + '</span>'
                        + '</div>'
                        + '<form action="contacts" method="POST" style="margin:0;" onsubmit="return confirm(\'Remove this contact?\')">'
                        + '  <input type="hidden" name="action" value="delete">'
                        + '  <input type="hidden" name="contactId" value="' + c.contactId + '">'
                        + '  <button type="submit" class="btn btn-ghost btn-sm" title="Remove"><ion-icon name="trash-outline"></ion-icon></button>'
                        + '</form>'
                        + '</li>';
                });
                html += '</ul>';
                container.innerHTML = html;

                // Show add form only if less than 3 contacts
                addSection.style.display = contacts.length < 3 ? 'block' : 'none';
            })
            .catch(err => {
                document.getElementById('contactsList').innerHTML = '<p class="text-muted text-center" style="padding: 1rem;">Unable to load contacts.</p>';
            });
        }

        // ===========================
        // LOAD USER'S COMPLAINTS
        // ===========================
        function loadMyComplaints() {
            fetch('myComplaints')
            .then(res => res.json())
            .then(complaints => {
                const container = document.getElementById('myComplaints');
                if (complaints.length === 0) {
                    container.innerHTML = '<p class="text-muted text-center" style="padding: 1.5rem;">No complaints filed yet.</p>';
                    return;
                }

                let html = '<div class="table-wrapper"><table><tr>'
                    + '<th>ID</th><th>Type</th><th>Location</th><th>Date</th><th>Status</th>'
                    + '</tr>';

                complaints.forEach(c => {
                    let badgeClass = 'badge-pending';
                    if (c.status === 'Resolved') badgeClass = 'badge-resolved';
                    else if (c.status === 'In Progress') badgeClass = 'badge-progress';

                    html += '<tr>'
                        + '<td>#' + c.complaintId + '</td>'
                        + '<td>' + escapeHtml(c.type) + '</td>'
                        + '<td>' + escapeHtml(c.location) + '</td>'
                        + '<td>' + c.time + '</td>'
                        + '<td><span class="badge ' + badgeClass + '">' + c.status + '</span></td>'
                        + '</tr>';
                });
                html += '</table></div>';
                container.innerHTML = html;
            })
            .catch(err => {
                document.getElementById('myComplaints').innerHTML = '<p class="text-muted text-center" style="padding: 1.5rem;">Unable to load complaints.</p>';
            });
        }

        function escapeHtml(text) {
            const div = document.createElement('div');
            div.appendChild(document.createTextNode(text || ''));
            return div.innerHTML;
        }

        // Load data on page ready
        loadContacts();
        loadMyComplaints();
    </script>

</body>
</html>
