package com.system.service;

import com.system.dao.DriverDAO;
import com.system.model.Driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverService {

    private DriverDAO driverDAO;

    public DriverService() {
        this.driverDAO = new DriverDAO();
    }

    // Add a new driver and return generated ID
    public boolean addDriver(Connection connection, Driver driver) throws SQLException {
        // Delegate to the DAO method using the provided connection
        boolean isAdded = driverDAO.addDriver(connection, driver);
        if (!isAdded) {
            throw new SQLException("Failed to add driver.");
        }
        return true;
    }

    // Get a driver by their ID
    public Driver getDriverById(int driverId) {
        try {
            return driverDAO.getDriverById(driverId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving driver: " + e.getMessage());
        }
    }
    
    public List<Driver> getAllDrivers() {
        try {
            return driverDAO.getAllDrivers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Driver getDriverByVehicleId(int vehicleId) {
        try {
            return driverDAO.getDriverByVehicleId(vehicleId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving driver for vehicle ID: " + vehicleId + " - " + e.getMessage());
        }
    }

    /**
     * Gets all available drivers for new bookings.
     * A driver is considered available if their status is set to "Available".
     *
     * @return List of available drivers; an empty list if none found.
     */
    public List<Driver> getAvailableDrivers() {
        try {
            List<Driver> availableDrivers = driverDAO.getAvailableDrivers();
            return availableDrivers != null ? availableDrivers : new ArrayList<>();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Returns an available driver excluding the one specified by excludeDriverId.
     * This method is used to find a replacement driver when deleting a driver.
     *
     * @param excludeDriverId the driver ID to exclude
     * @return an available Driver object or null if none found
     */
    public Driver getAvailableDriverExcluding(int excludeDriverId) {
        try {
            List<Driver> availableDrivers = driverDAO.getAvailableDrivers();
            for (Driver driver : availableDrivers) {
                if (driver.getDriverId() != excludeDriverId) {
                    return driver;
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update driver details
    public boolean updateDriver(Driver driver) {
        try {
            boolean isUpdated = driverDAO.updateDriver(driver);
            if (!isUpdated) {
                throw new Exception("Failed to update driver.");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a driver by their ID
    public boolean deleteDriver(int driverId) {
        try {
            return driverDAO.deleteDriver(driverId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting driver: " + e.getMessage());
        }
    }

    // Update driver status
    public boolean updateDriverStatus(int driverId, String status) {
        try {
            return driverDAO.updateDriverStatus(driverId, status);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating driver status: " + e.getMessage());
        }
    }
    
    /**
     * Reassigns all bookings currently assigned to the old driver (oldDriverId)
     * to a new driver (newDriverId).
     *
     * @param oldDriverId the driver to be replaced
     * @param newDriverId the replacement driver
     * @return true if the update was successful, false otherwise
     */
    public boolean reassignBookings(int oldDriverId, int newDriverId) {
        try {
            return driverDAO.reassignBookings(oldDriverId, newDriverId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Reassigns all vehicles currently assigned to the old driver (oldDriverId)
     * to a new driver (newDriverId).
     *
     * @param oldDriverId the driver to be replaced
     * @param newDriverId the replacement driver
     * @return true if the update was successful, false otherwise
     */
    public boolean reassignVehicles(int oldDriverId, int newDriverId) {
        try {
            return driverDAO.reassignVehicles(oldDriverId, newDriverId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
