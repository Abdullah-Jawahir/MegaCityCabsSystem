package com.system.dao;

import com.system.model.Vehicle;
import com.system.utils.DBConnectionFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VehicleDAO {

    private static final Logger logger = Logger.getLogger(VehicleDAO.class.getName());

    // Method to create a new vehicle in the database
    public boolean createVehicle(Vehicle vehicle) {
        String query = "INSERT INTO vehicle (plate_number, model, status, driver_id, created_at, rate_per_km) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnectionFactory.getConnection();
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, vehicle.getPlateNumber());
            stmt.setString(2, vehicle.getModel());
            stmt.setString(3, vehicle.getStatus());
            stmt.setInt(4, vehicle.getDriverId());
            stmt.setTimestamp(5, Timestamp.valueOf(vehicle.getCreatedAt()));
            stmt.setFloat(6, vehicle.getRatePerKm());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // You can fetch the generated vehicleId if needed
                }
                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating vehicle: " + vehicle.getPlateNumber(), e);
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);  // Close resources in the finally block
        }
        return false;
    }

    // Method to get a vehicle by its ID
    public Vehicle getVehicleById(int vehicleId) throws SQLException  {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        try {
            conn = DBConnectionFactory.getConnection();
            String query = "SELECT * FROM vehicle WHERE vehicle_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, vehicleId);
            resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String plateNumber = resultSet.getString("plate_number");
                String model = resultSet.getString("model");
                String status = resultSet.getString("status");
                int driverId = resultSet.getInt("driver_id");
                LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                float ratePerKm = resultSet.getFloat("rate_per_km");
                return new Vehicle(vehicleId, plateNumber, model, status, driverId, createdAt, ratePerKm);
            }
            return null;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving vehicle with ID: " + vehicleId, e);
            throw e;
        } finally {
            closeResources(conn, stmt, resultSet);
        }
    }


    // Method to get all vehicles
    public List<Vehicle> getAllVehicles() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        List<Vehicle> vehicles = new ArrayList<>();

        try {
            conn = DBConnectionFactory.getConnection();
            String query = "SELECT vehicle_id, plate_number, model, status, driver_id, created_at, rate_per_km FROM vehicle";
            stmt = conn.prepareStatement(query);
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int vehicleId = resultSet.getInt("vehicle_id"); // Retrieve vehicle ID
                String plateNumber = resultSet.getString("plate_number");
                String model = resultSet.getString("model");
                String status = resultSet.getString("status");
                int driverId = resultSet.getInt("driver_id");
                LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                float ratePerKm = resultSet.getFloat("rate_per_km");

                // Use the updated constructor
                vehicles.add(new Vehicle(vehicleId, plateNumber, model, status, driverId, createdAt, ratePerKm));
            }
            return vehicles;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving all vehicles", e);
            throw e;
        } finally {
            closeResources(conn, stmt, resultSet);
        }
    }

    // Method to update the vehicle details (plate number, model, status, driver ID, rate per KM)
    public boolean updateVehicle(int vehicleId, String plateNumber, String model, String status, int driverId, float ratePerKm) {
        String query = "UPDATE vehicle SET plate_number = ?, model = ?, status = ?, driver_id = ?, rate_per_km = ? WHERE vehicle_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnectionFactory.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, plateNumber);
            stmt.setString(2, model);
            stmt.setString(3, status);
            stmt.setInt(4, driverId);
            stmt.setFloat(5, ratePerKm);
            stmt.setInt(6, vehicleId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating vehicle with ID: " + vehicleId, e);
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }
        return false;
    }

    /**
     * Retrieves a list of vehicles by their status.
     *
     * @param status The status to filter vehicles by (e.g., "Active").
     * @return List of vehicles matching the given status.
     * @throws SQLException if any database error occurs.
     */
    public List<Vehicle> getVehiclesByStatus(String status) throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT vehicle_id, plate_number, model, status, driver_id, created_at, rate_per_km FROM vehicle WHERE status = ?"; // Added rate_per_km
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionFactory.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, status);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int vehicleId = rs.getInt("vehicle_id");
                String plateNumber = rs.getString("plate_number");
                String model = rs.getString("model");
                String vehicleStatus = rs.getString("status");
                int driverId = rs.getInt("driver_id");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                float ratePerKm = rs.getFloat("rate_per_km");

                vehicles.add(new Vehicle(vehicleId, plateNumber, model, vehicleStatus, driverId, createdAt, ratePerKm));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving vehicles by status: " + status, e);
            throw new SQLException("Error retrieving vehicles by status: " + status, e);
        } finally {
            closeResources(conn, stmt, rs);
        }
        return vehicles;
    }

    // Method to update the status of a vehicle
    public boolean updateVehicleStatus(int vehicleId, String status) {
        String query = "UPDATE vehicle SET status = ? WHERE vehicle_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnectionFactory.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, status);
            stmt.setInt(2, vehicleId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating vehicle status with ID: " + vehicleId, e);
        } finally {
            closeResources(conn, stmt, null);
        }
        return false;
    }

    // Method to delete a vehicle by its ID
    public boolean deleteVehicle(int vehicleId) {
        String query = "DELETE FROM vehicle WHERE vehicle_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnectionFactory.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, vehicleId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting vehicle with ID: " + vehicleId, e);
        } finally {
            closeResources(conn, stmt, null);
        }
        return false;
    }

    // Method to get count of available vehicles
    public int getVehiclesCountByStatus(String status) {
        String query = "SELECT COUNT(*) FROM vehicle WHERE status = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            conn = DBConnectionFactory.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, status);
            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching vehicle count by status: " + status, e);
        } finally {
            closeResources(conn, stmt, resultSet);
        }

        return 0;
    }

    // Method to get count of new vehicles within a date range
    public int getNewVehiclesCountBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        String query = "SELECT COUNT(*) FROM vehicle WHERE created_at BETWEEN ? AND ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            conn = DBConnectionFactory.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching new vehicle count between dates", e);
        } finally {
            closeResources(conn, stmt, resultSet);
        }

        return 0;
    }

    // Helper method to close resources explicitly
    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error closing ResultSet", e);
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error closing PreparedStatement", e);
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error closing Connection", e);
        }
    }
}