CREATE DATABASE IF NOT EXISTS women_safety_db;
USE women_safety_db;

-- 1. Users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_no VARCHAR(15) NOT NULL,
    role VARCHAR(10) DEFAULT 'USER' -- 'USER' or 'ADMIN'
);

-- 2. Emergency Contacts (2-3 per user, enforced at application level)
CREATE TABLE IF NOT EXISTS emergency_contacts (
    contact_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    contact_name VARCHAR(100) NOT NULL,
    contact_number VARCHAR(15) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 3. Complaints / Incident Reports
CREATE TABLE IF NOT EXISTS complaints (
    complaint_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    complaint_type VARCHAR(100) NOT NULL,
    description TEXT,
    location VARCHAR(255),
    status VARCHAR(20) DEFAULT 'Pending', -- Pending, In Progress, Resolved
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 4. SOS Alerts
CREATE TABLE IF NOT EXISTS sos_alerts (
    alert_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    location_lat VARCHAR(50),
    location_lng VARCHAR(50),
    status VARCHAR(20) DEFAULT 'Active', -- Active, Resolved
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 5. SOS Alert Recipients - tracks which contacts were notified for each alert
CREATE TABLE IF NOT EXISTS sos_alert_recipients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    alert_id INT NOT NULL,
    contact_id INT NOT NULL,
    contact_name VARCHAR(100),
    contact_number VARCHAR(15),
    delivery_status VARCHAR(20) DEFAULT 'SENT', -- SENT, DELIVERED, FAILED
    FOREIGN KEY (alert_id) REFERENCES sos_alerts(alert_id) ON DELETE CASCADE,
    FOREIGN KEY (contact_id) REFERENCES emergency_contacts(contact_id) ON DELETE CASCADE
);

-- Default Admin Account (password: admin123)
INSERT IGNORE INTO users (name, email, password, phone_no, role)
VALUES ('Admin', 'admin@safetyapp.com', 'admin123', '9999999999', 'ADMIN');
