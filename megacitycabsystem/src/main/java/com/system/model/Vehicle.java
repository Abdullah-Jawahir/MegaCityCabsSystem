package com.system.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Vehicle {
    private int vehicleId;
    private String plateNumber;
    private String model;
    private String status;
    private int driverId;
    private LocalDateTime createdAt;  // New field for created_at

    // Constructor for existing vehicles (when vehicleId is known)
    public Vehicle(int vehicleId, String plateNumber, String model, String status, int driverId, LocalDateTime createdAt) {
        this.vehicleId = vehicleId;
        this.plateNumber = plateNumber;
        this.model = model;
        this.status = status;
        this.driverId = driverId;
        this.createdAt = createdAt;  // Initialize new field
    }

    // Constructor for new vehicles (without vehicleId)
    public Vehicle(String plateNumber, String model, String status, int driverId, LocalDateTime createdAt) {
        this.plateNumber = plateNumber;
        this.model = model;
        this.status = status;
        this.driverId = driverId;
        this.createdAt = createdAt;  // Initialize new field
    }

    // Getters and setters
    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
