<%@ page import="com.safety.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("user");
    if(user == null || !"ADMIN".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard | SafeHer</title>
    <link rel="stylesheet" href="css/style.css">
    <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
    <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
</head>
<body>

    <nav class="navbar admin-nav">
        <a href="admin_dashboard.jsp" class="nav-brand">
            <ion-icon name="shield-checkmark"></ion-icon> SafeHer Admin
        </a>
        <div class="nav-links">
            <span style="font-weight: 600; color: rgba(255,255,255,0.6); font-size: 0.85rem;">
                <ion-icon name="settings-outline" style="vertical-align: middle;"></ion-icon> Control Panel
            </span>
            <a href="logout" style="font-weight: 600;">
                <ion-icon name="log-out-outline" style="vertical-align: middle;"></ion-icon> Logout
            </a>
        </div>
    </nav>

    <div class="container">

        <!-- Header -->
        <div class="section-header">
            <h2><ion-icon name="grid-outline" style="vertical-align: middle; margin-right: 0.3rem;"></ion-icon> Command Center</h2>
            <button class="btn btn-outline" onclick="fetchData()" id="refreshBtn">
                <ion-icon name="refresh-outline"></ion-icon> Refresh
            </button>
        </div>

        <!-- Stats Cards -->
        <div class="stats-grid" id="statsGrid">
            <div class="stat-card danger">
                <div class="stat-label">Active SOS Alerts</div>
                <div class="stat-value" id="statAlerts">-</div>
            </div>
            <div class="stat-card warning">
                <div class="stat-label">Pending Complaints</div>
                <div class="stat-value" id="statPending">-</div>
            </div>
            <div class="stat-card success">
                <div class="stat-label">Resolved Cases</div>
                <div class="stat-value" id="statResolved">-</div>
            </div>
            <div class="stat-card info">
                <div class="stat-label">Registered Users</div>
                <div class="stat-value" id="statUsers">-</div>
            </div>
        </div>

        <!-- SOS Alerts Section -->
        <div class="card mb-3">
            <h3 class="section-title danger">
                <ion-icon name="warning-outline"></ion-icon> SOS Alerts
            </h3>
            <div id="alertsContainer">
                <p class="text-muted text-center" style="padding: 2rem;">Loading alerts...</p>
            </div>
        </div>

        <!-- Complaints Section -->
        <div class="card mb-3">
            <h3 class="section-title">
                <ion-icon name="albums-outline" style="color: var(--primary);"></ion-icon> Complaint Reports
            </h3>
            <div id="complaintsContainer">
                <p class="text-muted text-center" style="padding: 2rem;">Loading complaints...</p>
            </div>
        </div>

    </div>

    <div class="footer">
        <p>&copy; 2026 <a href="index.jsp">SafeHer</a>. Admin Panel &mdash; Authorized Personnel Only.</p>
    </div>

    <script>
        // ===========================
        // FETCH ADMIN DATA
        // ===========================
        function fetchData() {
            const btn = document.getElementById('refreshBtn');
            btn.disabled = true;
            btn.innerHTML = '<ion-icon name="sync-outline" class="spin"></ion-icon> Loading...';

            fetch('api/admin/')
            .then(response => response.json())
            .then(data => {
                // Update stats
                if (data.stats) {
                    document.getElementById('statAlerts').textContent = data.stats.activeAlerts || 0;
                    document.getElementById('statPending').textContent = data.stats.pendingComplaints || 0;
                    document.getElementById('statResolved').textContent = data.stats.resolvedComplaints || 0;
                    document.getElementById('statUsers').textContent = data.stats.totalUsers || 0;
                }

                renderAlerts(data.alerts || []);
                renderComplaints(data.complaints || []);

                btn.disabled = false;
                btn.innerHTML = '<ion-icon name="refresh-outline"></ion-icon> Refresh';
            })
            .catch(err => {
                console.error('Error fetching admin data:', err);
                btn.disabled = false;
                btn.innerHTML = '<ion-icon name="refresh-outline"></ion-icon> Refresh';
            });
        }

        // ===========================
        // RENDER ALERTS TABLE
        // ===========================
        function renderAlerts(alerts) {
            const container = document.getElementById('alertsContainer');

            if (alerts.length === 0) {
                container.innerHTML = '<p class="text-center" style="padding: 2rem; color: var(--success);"><ion-icon name="checkmark-circle"></ion-icon> No SOS alerts found.</p>';
                return;
            }

            let html = '<div class="table-wrapper"><table>'
                + '<tr><th>ID</th><th>User</th><th>Phone</th><th>Location</th><th>Time</th><th>Status</th><th>Action</th></tr>';

            alerts.forEach(a => {
                let locationLink = (a.lat && a.lat !== 'Unknown' && a.lat !== 'null')
                    ? '<a href="https://www.google.com/maps?q=' + a.lat + ',' + a.lng + '" target="_blank" style="color: var(--primary); font-weight: 600;"><ion-icon name="location-outline"></ion-icon> View Map</a>'
                    : '<span class="text-muted">N/A</span>';

                let statusBadge = a.status === 'Active'
                    ? '<span class="badge badge-active blink">' + a.status + '</span>'
                    : '<span class="badge badge-resolved">' + a.status + '</span>';

                let actionBtn = a.status === 'Active'
                    ? '<button class="btn btn-success btn-sm" onclick="resolveAlert(' + a.alertId + ')"><ion-icon name="checkmark-outline"></ion-icon> Resolve</button>'
                    : '<span class="text-muted" style="font-size:0.8rem;">Done</span>';

                html += '<tr>'
                    + '<td><strong>#' + a.alertId + '</strong></td>'
                    + '<td>' + escapeHtml(a.userName) + '</td>'
                    + '<td>' + escapeHtml(a.userPhone) + '</td>'
                    + '<td>' + locationLink + '</td>'
                    + '<td style="font-size:0.82rem;">' + a.time + '</td>'
                    + '<td>' + statusBadge + '</td>'
                    + '<td>' + actionBtn + '</td>'
                    + '</tr>';
            });

            html += '</table></div>';
            container.innerHTML = html;
        }

        // ===========================
        // RENDER COMPLAINTS TABLE
        // ===========================
        function renderComplaints(complaints) {
            const container = document.getElementById('complaintsContainer');

            if (complaints.length === 0) {
                container.innerHTML = '<p class="text-muted text-center" style="padding: 2rem;">No complaints filed yet.</p>';
                return;
            }

            let html = '<div class="table-wrapper"><table>'
                + '<tr><th>ID</th><th>User</th><th>Type</th><th>Location</th><th>Description</th><th>Time</th><th>Status</th><th>Action</th></tr>';

            complaints.forEach(c => {
                let badgeClass = 'badge-pending';
                if (c.status === 'Resolved') badgeClass = 'badge-resolved';
                else if (c.status === 'In Progress') badgeClass = 'badge-progress';

                // Truncate long descriptions
                let desc = c.desc || '';
                let shortDesc = desc.length > 60 ? desc.substring(0, 60) + '...' : desc;

                html += '<tr>'
                    + '<td><strong>#' + c.complaintId + '</strong></td>'
                    + '<td>' + escapeHtml(c.userName) + '</td>'
                    + '<td>' + escapeHtml(c.type) + '</td>'
                    + '<td>' + escapeHtml(c.location) + '</td>'
                    + '<td style="font-size: 0.82rem; max-width: 200px;" title="' + escapeHtml(desc) + '">' + escapeHtml(shortDesc) + '</td>'
                    + '<td style="font-size: 0.82rem;">' + c.time + '</td>'
                    + '<td><span class="badge ' + badgeClass + '">' + c.status + '</span></td>'
                    + '<td>'
                    + '  <select class="status-select" onchange="updateComplaintStatus(' + c.complaintId + ', this.value)">'
                    + '    <option value="">Change...</option>'
                    + '    <option value="Pending"' + (c.status === 'Pending' ? ' selected' : '') + '>Pending</option>'
                    + '    <option value="In Progress"' + (c.status === 'In Progress' ? ' selected' : '') + '>In Progress</option>'
                    + '    <option value="Resolved"' + (c.status === 'Resolved' ? ' selected' : '') + '>Resolved</option>'
                    + '  </select>'
                    + '</td>'
                    + '</tr>';
            });

            html += '</table></div>';
            container.innerHTML = html;
        }

        // ===========================
        // ADMIN ACTIONS
        // ===========================
        function resolveAlert(alertId) {
            if (!confirm('Mark this SOS alert as resolved?')) return;

            fetch('api/admin/', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'action=resolveAlert&alertId=' + alertId
            })
            .then(res => res.json())
            .then(data => {
                if (data.status === 'success') {
                    fetchData(); // Refresh all data
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(err => alert('Network error updating alert.'));
        }

        function updateComplaintStatus(complaintId, newStatus) {
            if (!newStatus) return;

            fetch('api/admin/', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'action=updateComplaintStatus&complaintId=' + complaintId + '&status=' + encodeURIComponent(newStatus)
            })
            .then(res => res.json())
            .then(data => {
                if (data.status === 'success') {
                    fetchData(); // Refresh all data
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(err => alert('Network error updating complaint status.'));
        }

        function escapeHtml(text) {
            const div = document.createElement('div');
            div.appendChild(document.createTextNode(text || ''));
            return div.innerHTML;
        }

        // Initial load and auto-refresh every 15 seconds
        fetchData();
        setInterval(fetchData, 15000);
    </script>

</body>
</html>
