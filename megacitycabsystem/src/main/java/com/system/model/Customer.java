package com.system.model;

public class Customer {
    private int customerId;
    private User user;
    private String registrationNumber;
    private String address;
    private String nic;

    // Constructor to initialize the customer 
    public Customer(int customerId, User user, String registrationNumber, String address, String nic) {
        this.setCustomerId(customerId); 
        this.setUser(user); 
        this.setRegistrationNumber(registrationNumber);  
        this.setAddress(address); 
        this.setNic(nic);  
    }

    // Getters
    public int getCustomerId() {
        return customerId;
    }

    public User getUser() {
        return user;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getNic() {
        return nic;
    }

    // Setters with validation
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
        } else {
            throw new IllegalArgumentException("User cannot be null.");
        }
    }

    public void setRegistrationNumber(String registrationNumber) {
        if (registrationNumber != null && !registrationNumber.trim().isEmpty()) {
            this.registrationNumber = registrationNumber;
        } else {
            throw new IllegalArgumentException("Registration number cannot be empty.");
        }
    }

    public void setAddress(String address) {
        if (address != null && !address.trim().isEmpty()) {
            this.address = address;
        } else {
            throw new IllegalArgumentException("Address cannot be empty.");
        }
    }

    public void setNic(String nic) {
        if (nic != null && !nic.trim().isEmpty()) {
            this.nic = nic;
        } else {
            throw new IllegalArgumentException("NIC is required.");
        }
    }
}
