package com.safety.model;

import java.sql.Timestamp;

public class SOSAlert {
    private int alertId;
    private int userId;
    private String locationLat;
    private String locationLng;
    private String status;
    private Timestamp createdAt;

    public SOSAlert() {}

    public SOSAlert(int alertId, int userId, String locationLat, String locationLng, String status, Timestamp createdAt) {
        this.alertId = alertId;
        this.userId = userId;
        this.locationLat = locationLat;
        this.locationLng = locationLng;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getAlertId() { return alertId; }
    public void setAlertId(int alertId) { this.alertId = alertId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getLocationLat() { return locationLat; }
    public void setLocationLat(String locationLat) { this.locationLat = locationLat; }
    public String getLocationLng() { return locationLng; }
    public void setLocationLng(String locationLng) { this.locationLng = locationLng; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
