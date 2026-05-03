package com.safety.model;

public class EmergencyContact {
    private int contactId;
    private int userId;
    private String contactName;
    private String contactNumber;

    public EmergencyContact() {}

    public EmergencyContact(int contactId, int userId, String contactName, String contactNumber) {
        this.contactId = contactId;
        this.userId = userId;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    public int getContactId() { return contactId; }
    public void setContactId(int contactId) { this.contactId = contactId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
}
