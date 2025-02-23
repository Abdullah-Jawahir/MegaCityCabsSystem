package com.system.model;

import java.time.LocalDateTime;

public class User {
    private int id;
    private String name;
    private String password;
    private String role;
    private String email;
    private String phone;
    private LocalDateTime lastLogin;

    public User(String name, String password, String role, String email, String phone, LocalDateTime lastLogin) {
        this.setName(name);
        this.setPassword(password);
        this.setRole(role);
        this.setEmail(email);
        this.setPhone(phone);
        this.setLastLogin(lastLogin);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    // Setters (Can include validation)
    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("ID must be greater than zero.");
        }
    }

    public void setName(String username) {
        if (username != null && !username.trim().isEmpty()) {
            this.name = username;
        } else {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
    }

    public void setPassword(String password) {
        if (password != null && password.length() >= 6) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
    }

    public void setRole(String role) {
        if (role != null && !role.trim().isEmpty()) {
            this.role = role;
        } else {
            throw new IllegalArgumentException("Role cannot be empty.");
        }
    }

    public void setEmail(String email) {
        if (email != null && email.contains("@")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    public void setPhone(String phone) {
        if (phone != null && !phone.trim().isEmpty()) {
            this.phone = phone;
        } else {
            throw new IllegalArgumentException("Phone number is required.");
        }
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
