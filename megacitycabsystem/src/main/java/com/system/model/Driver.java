package com.system.model;

public class Driver {
    private int driverId;
    private User user;  // Changed from userId to full User object
    private String licenseNumber;

    public enum DriverStatus {
        Available,
        Busy,
        Inactive,
        Assigned;
        
        @Override
        public String toString() {
            return name();
        }
    }

    private DriverStatus status;

    // Constructor
    public Driver(int driverId, User user, String licenseNumber, String status) {
        this.driverId = driverId;
        this.user = user;
        this.licenseNumber = licenseNumber;
        this.status = DriverStatus.valueOf(status);
    }

    public int getDriverId() {
        return driverId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    // FIXED: Use enum type instead of String
    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    // Optional: If you need to set status from a string
    public void setStatusFromString(String status) {
        this.status = DriverStatus.valueOf(status);
    }
}
