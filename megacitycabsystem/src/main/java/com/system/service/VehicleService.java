package com.system.service;

import com.system.dao.VehicleDAO;
import com.system.model.Vehicle;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class VehicleService {

    private VehicleDAO vehicleDAO;

    public VehicleService() {
        this.vehicleDAO = new VehicleDAO();
    }

    // Create a new vehicle
    public boolean createVehicle(Vehicle vehicle) {
        try {
            return vehicleDAO.createVehicle(vehicle);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get a vehicle by its ID
    public Vehicle getVehicleById(int vehicleId) {
        try {
			return vehicleDAO.getVehicleById(vehicleId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    // Get all vehicles
    public List<Vehicle> getAllVehicles() {
        try {
			return vehicleDAO.getAllVehicles();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    // Update the status of a vehicle
    public boolean updateVehicleStatus(int vehicleId, String status) {
        try {
            return vehicleDAO.updateVehicleStatus(vehicleId, status);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a vehicle by its ID
    public boolean deleteVehicle(int vehicleId) {
        try {
            return vehicleDAO.deleteVehicle(vehicleId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to update vehicle details (plate number, model, status, driver ID)
    public boolean updateVehicle(int vehicleId, String plateNumber, String model, String status, int driverId) {
        try {
            return vehicleDAO.updateVehicle(vehicleId, plateNumber, model, status, driverId);
        } catch (Exception e) {
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
                if (vehicle.getDriverId() != 0) {  // 0 indicates no driver is assigned
                    return vehicle;  // Return the first available vehicle
                }
            }

            // If no available vehicle is found, return null
            return null;

        } catch (SQLException e) {
            // Log the error and return null
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
            // Log the error
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates vehicle status and returns updated vehicle object
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
            e.printStackTrace();
            return null;
        }
    }
    
    public int getAvailableVehiclesCount() {
        return vehicleDAO.getVehiclesCountByStatus("Active");
    }
    
    public int getNewVehiclesCount(String period) {
        LocalDateTime startDate = getStartDateForPeriod(period);
        LocalDateTime endDate = LocalDateTime.now();
        return vehicleDAO.getNewVehiclesCountBetweenDates(startDate, endDate);
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
