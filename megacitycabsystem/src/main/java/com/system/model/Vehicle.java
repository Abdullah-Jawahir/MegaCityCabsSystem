package com.system.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Vehicle {
    private int vehicleId;
    private String plateNumber;
    private String model;
    private String status;
    private Integer driverId;
    private LocalDateTime createdAt;  // New field for created_at
    private float ratePerKm;

    // Constructor for existing vehicles (when vehicleId is known)
    public Vehicle(int vehicleId, String plateNumber, String model, String status, Integer driverId, LocalDateTime createdAt, float ratePerKm) {
        this.vehicleId = vehicleId;
        this.plateNumber = plateNumber;
        this.model = model;
        this.status = status;
        this.driverId = driverId;
        this.createdAt = createdAt;
        this.ratePerKm = ratePerKm;
    }

    // Constructor for new vehicles (without vehicleId)
    public Vehicle(String plateNumber, String model, String status, Integer driverId, LocalDateTime createdAt, float ratePerKm) {
        this.plateNumber = plateNumber;
        this.model = model;
        this.status = status;
        this.driverId = driverId;
        this.createdAt = createdAt;
        this.ratePerKm = ratePerKm;
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

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

	public float getRatePerKm() {
		return ratePerKm;
	}

	public void setRatePerKm(float ratePerKm) {
		this.ratePerKm = ratePerKm;
	}
}
