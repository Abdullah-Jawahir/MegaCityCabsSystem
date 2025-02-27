package com.system.model;

/**
 * A wrapper class to combine Vehicle and Driver information for view rendering
 */
public class VehicleDriverWrapper {
    private Vehicle vehicle;
    private Driver assignedDriver;

    public VehicleDriverWrapper(Vehicle vehicle, Driver assignedDriver) {
        this.vehicle = vehicle;
        this.assignedDriver = assignedDriver;
    }

    // Delegate methods to match the JSP's expected property paths
    public int getVehicleId() {
        return vehicle.getVehicleId();
    }

    public String getModel() {
        return vehicle.getModel();
    }
    
    public String getPlateNumber() {
        return vehicle.getPlateNumber();
    }

    public Driver getAssignedDriver() {
        return assignedDriver;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}