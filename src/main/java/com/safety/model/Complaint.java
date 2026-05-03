package com.safety.model;

import java.sql.Timestamp;

public class Complaint {
    private int complaintId;
    private int userId;
    private String complaintType;
    private String description;
    private String location;
    private String status;
    private Timestamp createdAt;

    public Complaint() {}

    public Complaint(int complaintId, int userId, String complaintType, String description, String location, String status, Timestamp createdAt) {
        this.complaintId = complaintId;
        this.userId = userId;
        this.complaintType = complaintType;
        this.description = description;
        this.location = location;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getComplaintId() { return complaintId; }
    public void setComplaintId(int complaintId) { this.complaintId = complaintId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getComplaintType() { return complaintType; }
    public void setComplaintType(String complaintType) { this.complaintType = complaintType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
