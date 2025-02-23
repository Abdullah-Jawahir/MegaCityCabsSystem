package com.system.model;

public class Vehicle {
    private int vehicleId;
    private String plateNumber;
    private String model;
    private String status;
    private int driverId;

    // Constructor for existing vehicles (when vehicleId is known)
    public Vehicle(int vehicleId, String plateNumber, String model, String status, int driverId) {
        this.vehicleId = vehicleId;
        this.plateNumber = plateNumber;
        this.model = model;
        this.status = status;
        this.driverId = driverId;
    }

    // Constructor for new vehicles (without vehicleId)
    public Vehicle(String plateNumber, String model, String status, int driverId) {
        this.plateNumber = plateNumber;
        this.model = model;
        this.status = status;
        this.driverId = driverId;
    }

    // Getters and setters (if needed)
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
}
