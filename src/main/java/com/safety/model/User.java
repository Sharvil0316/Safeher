package com.safety.model;

public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private String phoneNo;
    private String role;

    public User() {}

    public User(int userId, String name, String email, String password, String phoneNo, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.role = role;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
