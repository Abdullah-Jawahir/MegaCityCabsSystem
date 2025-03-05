package com.system.dao;

import com.system.model.Driver;
import com.system.model.User;
import com.system.utils.DBConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriverDAO {
    private static final Logger logger = Logger.getLogger(DriverDAO.class.getName());

    public boolean addDriver(Connection connection, Driver driver) throws SQLException {
        String query = "INSERT INTO driver (user_id, license_number, status) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, driver.getUser().getId());
            statement.setString(2, driver.getLicenseNumber());
            statement.setString(3, driver.getStatus().toString()); // Convert enum to String
            return statement.executeUpdate() > 0;
        }
    }

    public Driver getDriverById(int driverId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnectionFactory.getConnection();
            String query = "SELECT driver.*, user.name, user.username, user.password, user.role, " +
                           "user.email, user.phone, user.last_login " +
                           "FROM driver " +
                           "INNER JOIN user ON driver.user_id = user.user_id " +
                           "WHERE driver.driver_id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, driverId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User(
                    resultSet.getString("name"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("role"),
                    resultSet.getString("email"),
                    resultSet.getString("phone"),
                    resultSet.getTimestamp("last_login") != null ? resultSet.getTimestamp("last_login").toLocalDateTime() : null
                );
                user.setId(resultSet.getInt("user_id"));

                return new Driver(
                    resultSet.getInt("driver_id"),
                    user,
                    resultSet.getString("license_number"),
                    resultSet.getString("status")
                );
            }
            return null;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting driver by ID: " + driverId, e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    public List<Driver> getAllDrivers() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Driver> drivers = new ArrayList<>();

        try {
            connection = DBConnectionFactory.getConnection();
            String query = "SELECT driver.*, user.name, user.username, user.password, user.role, " +
                           "user.email, user.phone, user.last_login " +
                           "FROM driver " +
                           "INNER JOIN user ON driver.user_id = user.user_id";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User(
                    resultSet.getString("name"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("role"),
                    resultSet.getString("email"),
                    resultSet.getString("phone"),
                    resultSet.getTimestamp("last_login") != null ? resultSet.getTimestamp("last_login").toLocalDateTime() : null
                );
                user.setId(resultSet.getInt("user_id"));

                drivers.add(new Driver(
                    resultSet.getInt("driver_id"),
                    user,
                    resultSet.getString("license_number"),
                    resultSet.getString("status")
                ));
            }
            return drivers;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting all drivers", e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }
    
    public Driver getDriverByVehicleId(int vehicleId) throws SQLException {
        String query = "SELECT d.driver_id, d.license_number, d.status, d.user_id, " +
                       "u.name, u.username, u.password, u.role, u.email, u.phone, u.last_login " +
                       "FROM driver d " +
                       "INNER JOIN vehicle v ON v.driver_id = d.driver_id " +
                       "INNER JOIN user u ON d.user_id = u.user_id " +
                       "WHERE v.vehicle_id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DBConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, vehicleId);
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                // Build the User object
                User user = new User(
                    resultSet.getString("name"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("role"),
                    resultSet.getString("email"),
                    resultSet.getString("phone"),
                    resultSet.getTimestamp("last_login") != null ? 
                        resultSet.getTimestamp("last_login").toLocalDateTime() : null
                );
                user.setId(resultSet.getInt("user_id"));
                
                // Build and return the Driver object
                return new Driver(
                    resultSet.getInt("driver_id"),
                    user,
                    resultSet.getString("license_number"),
                    resultSet.getString("status")
                );
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving driver by vehicle ID: " + vehicleId, e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
        
        return null;
    }
    
    public List<Driver> getAvailableDrivers() throws SQLException {
        List<Driver> drivers = new ArrayList<>();
        String query = "SELECT d.driver_id, d.license_number, d.status, d.user_id, " +
                       "u.name, u.username, u.password, u.role, u.email, u.phone, u.last_login " +
                       "FROM driver d " +
                       "INNER JOIN user u ON d.user_id = u.user_id " +
                       "WHERE d.status = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = DBConnectionFactory.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setString(1, "Available"); 
            rs = stmt.executeQuery();

            while (rs.next()) {

                User user = new User(
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getTimestamp("last_login") != null ? 
                        rs.getTimestamp("last_login").toLocalDateTime() : null
                );
                user.setId(rs.getInt("user_id"));

                Driver driver = new Driver(
                    rs.getInt("driver_id"),
                    user,
                    rs.getString("license_number"),
                    rs.getString("status")
                );
                drivers.add(driver);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving available drivers", e);
            throw e;
        } finally {
            closeResources(rs, stmt, connection);
        }
        return drivers;
    }

    public boolean updateDriver(Driver driver) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnectionFactory.getConnection();
            String query = "UPDATE driver SET license_number = ?, status = ? WHERE driver_id = ?";
            statement = connection.prepareStatement(query);

            statement.setString(1, driver.getLicenseNumber());
            statement.setString(2, driver.getStatus().toString());
            statement.setInt(3, driver.getDriverId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating driver: " + driver.getDriverId(), e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    public boolean deleteDriver(int driverId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnectionFactory.getConnection();
            String query = "DELETE FROM driver WHERE driver_id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, driverId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting driver: " + driverId, e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }

    public boolean updateDriverStatus(int driverId, String status) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnectionFactory.getConnection();
            String query = "UPDATE driver SET status = ? WHERE driver_id = ?";
            statement = connection.prepareStatement(query);

            statement.setString(1, status);
            statement.setInt(2, driverId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating driver status: " + driverId, e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }
    
    /**
     * Reassigns all bookings from an old driver to a new driver.
     *
     * @param oldDriverId the driver ID to be replaced
     * @param newDriverId the new driver ID to assign bookings
     * @return true if the update was successful, false otherwise
     */
    public boolean reassignBookings(int oldDriverId, int newDriverId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBConnectionFactory.getConnection();
            String sql = "UPDATE booking SET driver_id = ? WHERE driver_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, newDriverId);
            statement.setInt(2, oldDriverId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error reassigning bookings from driver " + oldDriverId + " to " + newDriverId, e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }
    
    /**
     * Reassigns all vehicles from an old driver to a new driver.
     *
     * @param oldDriverId the driver ID to be replaced
     * @param newDriverId the new driver ID to assign vehicles
     * @return true if the update was successful, false otherwise
     */
    public boolean reassignVehicles(int oldDriverId, int newDriverId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBConnectionFactory.getConnection();
            String sql = "UPDATE vehicle SET driver_id = ? WHERE driver_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, newDriverId);
            statement.setInt(2, oldDriverId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error reassigning vehicles from driver " + oldDriverId + " to " + newDriverId, e);
            throw e;
        } finally {
            closeResources(null, statement, connection);
        }
    }
    
    public List<Map<String, Object>> getTopPerformingDrivers(int limit) {
        List<Map<String, Object>> topDrivers = new ArrayList<>();
        String query = "SELECT d.driver_id, u.name, COUNT(b.booking_id) AS total_bookings " +
                       "FROM driver d " +
                       "INNER JOIN user u ON d.user_id = u.user_id " +
                       "LEFT JOIN booking b ON d.driver_id = b.driver_id " +
                       "GROUP BY d.driver_id, u.name " +
                       "ORDER BY total_bookings DESC " +
                       "LIMIT ?";

        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> driverData = new HashMap<>();
                    driverData.put("driver_id", resultSet.getInt("driver_id"));
                    driverData.put("name", resultSet.getString("name"));
                    driverData.put("total_bookings", resultSet.getInt("total_bookings"));

                    // Hardcoded rating value
                    double avgRating = 4.5;
                    driverData.put("avg_rating", avgRating);
                    // Calculate full stars using Math.floor and store as an integer
                    driverData.put("fullStars", (int) Math.floor(avgRating));

                    topDrivers.add(driverData);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching top-performing drivers", e);
        }

        return topDrivers;
    }



    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error closing ResultSet", e);
        }

        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error closing Statement", e);
        }

        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error closing Connection", e);
        }
    }
}