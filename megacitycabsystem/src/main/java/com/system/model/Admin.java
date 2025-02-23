package com.system.model;

public class Admin {
    private int adminId;  // Auto-incremented in DB
    private int userId;   // Foreign key linking to User table

    // Constructor
    public Admin(int adminId, int userId) {
        this.adminId = adminId;  // Auto-incremented in DB
        this.userId = userId;    // Links to User table
    }

    // Getter and Setter for adminId
    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    // Getter and Setter for userId
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
