package com.system.service;

import com.system.dao.VehicleDAO;
import com.system.model.Vehicle;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VehicleService {

    private VehicleDAO vehicleDAO;
    private static final Logger logger = Logger.getLogger(VehicleService.class.getName()); //Correct Logger declaration

    public VehicleService() {
        this.vehicleDAO = new VehicleDAO();
    }

    // Create a new vehicle
    public boolean createVehicle(Vehicle vehicle) {
        try {
            return vehicleDAO.createVehicle(vehicle);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating vehicle (service): ", e);
            e.printStackTrace();
            return false;
        }
    }

    // Get a vehicle by its ID
    public Vehicle getVehicleById(int vehicleId) {
        try {
            return vehicleDAO.getVehicleById(vehicleId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting vehicle by ID: " + vehicleId, e);
            e.printStackTrace();
            return null;
        }
    }

    // Get all vehicles
    public List<Vehicle> getAllVehicles() {
        try {
            return vehicleDAO.getAllVehicles();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting all vehicles: ", e);
            e.printStackTrace();
            return null;
        }
    }

    // Update the status of a vehicle
    public boolean updateVehicleStatus(int vehicleId, String status) {
        try {
            return vehicleDAO.updateVehicleStatus(vehicleId, status);
        } catch (Exception e) {
             logger.log(Level.SEVERE, "Error updating vehicle status: " + vehicleId, e);
            e.printStackTrace();
            return false;
        }
    }

    // Delete a vehicle by its ID
    public boolean deleteVehicle(int vehicleId) {
        try {
            return vehicleDAO.deleteVehicle(vehicleId);
        } catch (Exception e) {
             logger.log(Level.SEVERE, "Error deleting vehicle: " + vehicleId, e);
            e.printStackTrace();
            return false;
        }
    }

    // Method to update vehicle details (plate number, model, status, driver ID, ratePerKm)
    public boolean updateVehicle(int vehicleId, String plateNumber, String model, String status, Integer driverId, float ratePerKm) { //Changed to Integer driverID
        try {
            return vehicleDAO.updateVehicle(vehicleId, plateNumber, model, status, driverId, ratePerKm);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating vehicle (service): " + vehicleId, e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets all the available vehicles for new bookings.
     * A vehicle is considered available if:
     * 1. Its status is 'Active'
     * 2. It has an assigned driver
     *
     * @return Vehicle object if available, null if no vehicles are available
     */
    public Vehicle getAvailableVehicles() {
        try {
            // Get all active vehicles from the database
            List<Vehicle> availableVehicles = vehicleDAO.getVehiclesByStatus("Active");

            // Iterate through the list to find a vehicle that has an assigned driver
            for (Vehicle vehicle : availableVehicles) {
                // Check if the vehicle has a driver assigned
                if (vehicle.getDriverId() != null) {  // Check if the vehicle has a driver assigned, was (vehicle.getDriverId() != 0)
                    return vehicle;  // Return the first available vehicle
                }
            }

            // If no available vehicle is found, return null
            return null;

        } catch (SQLException e) {
             logger.log(Level.SEVERE, "Error getting available vehicles: ", e);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Gets the first available vehicle for new bookings.
     * A vehicle is considered available if:
     * 1. Its status is 'Active'
     * 2. It has an assigned driver
     * 3. It is not currently part of any active booking
     *
     * @return Vehicle object if available, null if no vehicles are available
     */
    public Vehicle getAvailableVehicle() {
        try {
            List<Vehicle> availableVehicles = vehicleDAO.getVehiclesByStatus("Active");

            // Return the first available vehicle or null if none found
            return availableVehicles != null && !availableVehicles.isEmpty()
                    ? availableVehicles.get(0)
                    : null;

        } catch (SQLException e) {
             logger.log(Level.SEVERE, "Error getting available vehicle: ", e);
            e.printStackTrace();
            return null;
        }
    }

    public List<Vehicle> getAllAvailableVehicles() {
        try {
            // Get all active vehicles from the database
            List<Vehicle> availableVehicles = vehicleDAO.getVehiclesByStatus("Active");
            List<Vehicle> result = new ArrayList<>();

            // Iterate through the list to find a vehicle that has an assigned driver
            for (Vehicle vehicle : availableVehicles) {
                // Check if the vehicle has a driver assigned
                if (vehicle.getDriverId() != null) {  // Check if the vehicle has a driver assigned
                    result.add(vehicle); // Add to the result
                }
            }
            return result;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting all available vehicles: ", e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates vehicle status and returns updated vehicle object
     *
     * @param vehicleId the ID of the vehicle to update
     * @param newStatus the new status to set
     * @return updated Vehicle object or null if update fails
     */
    public Vehicle updateAndGetVehicle(int vehicleId, String newStatus) {
        try {
            // First update the status
            boolean updated = updateVehicleStatus(vehicleId, newStatus);
            if (updated) {
                // If update successful, return the updated vehicle
                return getVehicleById(vehicleId);
            }
            return null;
        } catch (Exception e) {
              logger.log(Level.SEVERE, "Error update and get vehicle: " + vehicleId, e);
            e.printStackTrace();
            return null;
        }
    }

    public int getAvailableVehiclesCount() {
        try{
            return vehicleDAO.getVehiclesCountByStatus("Active");
        }catch(Exception e){
            logger.log(Level.SEVERE, "Error getting available vehicle count: ", e);
            e.printStackTrace();
            return 0;
        }

    }

    public int getNewVehiclesCount(String period) {
        LocalDateTime startDate = getStartDateForPeriod(period);
        LocalDateTime endDate = LocalDateTime.now();
        try{
            return vehicleDAO.getNewVehiclesCountBetweenDates(startDate, endDate);
        }catch(Exception e){
             logger.log(Level.SEVERE, "Error getting new vehicle count: ", e);
            e.printStackTrace();
            return 0;
        }

    }

    private LocalDateTime getStartDateForPeriod(String period) {
        LocalDateTime now = LocalDateTime.now();

        switch (period) {
            case "last7days":
                return now.minusDays(7);
            case "previous7days":
                return now.minusDays(14);
            case "last30days":
                return now.minusDays(30);
            case "previous30days":
                return now.minusDays(60);
            case "thisMonth":
                return now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            case "lastMonth":
                return now.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            case "thisYear":
                return now.withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
            case "lastYear":
                return now.minusYears(1).withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
            default:
                return now.minusDays(7);
        }
    }
}