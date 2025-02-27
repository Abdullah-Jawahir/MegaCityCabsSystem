package com.system.dao;

import com.system.model.Booking;
import com.system.model.Driver;
import com.system.model.User;
import com.system.model.Customer;
import com.system.model.Vehicle;
import com.system.utils.DBConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookingDAO {

    private static final Logger logger = Logger.getLogger(BookingDAO.class.getName());

    // Method to create a new booking in the database
    public boolean createBooking(Booking booking) {
        String query = "INSERT INTO booking (booking_id, booking_time, pickup_location, destination, distance, status, driver_id, vehicle_id, customer_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.setString(1, booking.getBookingId());
            statement.setTimestamp(2, Timestamp.valueOf(booking.getBookingTime()));
            statement.setString(3, booking.getPickupLocation());
            statement.setString(4, booking.getDestination());
            statement.setFloat(5, booking.getDistance());
            statement.setString(6, booking.getStatus());
            statement.setInt(7, booking.getAssignedDriver().getDriverId());
            statement.setInt(8, booking.getAssignedVehicle().getVehicleId());
            statement.setInt(9, booking.getCustomer().getCustomerId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating booking: " + booking.getBookingId(), e);
        } finally {
            closeResources(connection, statement, null);
        }
        return false;
    }

    // Method to get a booking by its ID
    public Booking getBookingById(String bookingId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnectionFactory.getConnection();
            String query = "SELECT * FROM booking WHERE booking_id = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, bookingId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String bookingIdResult = resultSet.getString("booking_id");
                LocalDateTime bookingTime = resultSet.getTimestamp("booking_time").toLocalDateTime();
                String pickupLocation = resultSet.getString("pickup_location");
                String destination = resultSet.getString("destination");
                float distance = resultSet.getFloat("distance");
                String status = resultSet.getString("status");

                // Fetch related data while the ResultSet is open
                int driverId = resultSet.getInt("driver_id");
                int vehicleId = resultSet.getInt("vehicle_id");
                int customerId = resultSet.getInt("customer_id");

                // Fetch related data after ResultSet is still open
                Driver driver = new DriverDAO().getDriverById(driverId);
                Vehicle vehicle = new VehicleDAO().getVehicleById(vehicleId);
                Customer customer = new CustomerDAO().getCustomerById(customerId);

                return new Booking(bookingIdResult, bookingTime, pickupLocation, destination, distance, status, driver, vehicle, customer);
            }

            return null;  // Return null if no result found
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving booking by ID: " + bookingId, e);
            throw e;  // Rethrow exception to handle it outside
        } finally {
            closeResources(connection, statement, resultSet);  // Always close resources at the end
        }
    }


    public List<Booking> getAllBookings() throws SQLException {
    	String query = "SELECT booking.*, customer.*, driver.*, vehicle.*, " +
                "booking.status AS booking_status, " +  // Alias for booking status
                "driver.status AS driver_status, " +    // Alias for driver status
                "vehicle.status AS vehicle_status, " +  // Alias for vehicle status
                "user_driver.name AS driver_name, user_driver.username AS driver_username, user_driver.password AS driver_password, " +
                "user_driver.role AS driver_role, user_driver.email AS driver_email, " +
                "user_driver.phone AS driver_phone, user_driver.last_login AS driver_last_login, " +
                "user_customer.name AS customer_name, user_customer.username AS customer_username, user_customer.password AS customer_password, " +
                "user_customer.role AS customer_role, user_customer.email AS customer_email, " +
                "user_customer.phone AS customer_phone, user_customer.last_login AS customer_last_login " +
                "FROM booking " +
                "INNER JOIN customer ON booking.customer_id = customer.customer_id " +
                "INNER JOIN driver ON booking.driver_id = driver.driver_id " +
                "INNER JOIN vehicle ON booking.vehicle_id = vehicle.vehicle_id " +
                "INNER JOIN user AS user_driver ON driver.user_id = user_driver.user_id " +
                "INNER JOIN user AS user_customer ON customer.user_id = user_customer.user_id";


        List<Booking> bookings = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Fetch values from resultSet
                String bookingId = resultSet.getString("booking_id");
                LocalDateTime bookingTime = resultSet.getTimestamp("booking_time").toLocalDateTime();
                String pickupLocation = resultSet.getString("pickup_location");
                String destination = resultSet.getString("destination");
                float distance = resultSet.getFloat("distance");
                String status = resultSet.getString("booking_status");

                // Fetch related driver, vehicle, and customer data
                Driver driver = new Driver(
                    resultSet.getInt("driver_id"),
                    new User(
                        resultSet.getString("driver_name"),
                        resultSet.getString("driver_username"),
                        resultSet.getString("driver_password"),
                        resultSet.getString("driver_role"),
                        resultSet.getString("driver_email"),
                        resultSet.getString("driver_phone"),
                        resultSet.getTimestamp("driver_last_login") != null ?
                            resultSet.getTimestamp("driver_last_login").toLocalDateTime() : null
                    ),
                    resultSet.getString("license_number"),
                    resultSet.getString("driver_status")
                );

                
                Vehicle vehicle = new Vehicle(
                	    resultSet.getInt("vehicle_id"),
                	    resultSet.getString("plate_number"),
                	    resultSet.getString("model"),
                	    resultSet.getString("vehicle_status"),
                	    resultSet.getInt("driver_id"), 
                	    resultSet.getTimestamp("created_at") != null ? resultSet.getTimestamp("created_at").toLocalDateTime() : null
                	);

                Customer customer = new Customer(
                    resultSet.getInt("customer_id"),
                    new User(
                        resultSet.getString("customer_name"),
                        resultSet.getString("customer_username"),
                        resultSet.getString("customer_password"),
                        resultSet.getString("customer_role"),
                        resultSet.getString("customer_email"),
                        resultSet.getString("customer_phone"),
                        resultSet.getTimestamp("customer_last_login") != null ?
                            resultSet.getTimestamp("customer_last_login").toLocalDateTime() : null
                    ),
                    resultSet.getString("registration_number"),
                    resultSet.getString("address"),
                    resultSet.getString("nic")
                );

                // Create the Booking object and add it to the list
                Booking booking = new Booking(bookingId, bookingTime, pickupLocation, destination, distance, status, driver, vehicle, customer);
                bookings.add(booking);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving all bookings", e);
            throw e;  // Rethrow exception to handle it outside
        } finally {
            closeResources(connection, statement, resultSet);
        }
        return bookings;
    }



    // Update the booking details
    public boolean updateBookingDetails(Booking booking) {
        String updateQuery = "UPDATE booking SET pickup_location = ?, destination = ?, distance = ?, status = ?, driver_id = ?, vehicle_id = ?, customer_id = ? WHERE booking_id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(updateQuery);

            preparedStatement.setString(1, booking.getPickupLocation());
            preparedStatement.setString(2, booking.getDestination());
            preparedStatement.setFloat(3, booking.getDistance());
            preparedStatement.setString(4, booking.getStatus());
            preparedStatement.setInt(5, booking.getAssignedDriver().getDriverId());
            preparedStatement.setInt(6, booking.getAssignedVehicle().getVehicleId());
            preparedStatement.setInt(7, booking.getCustomer().getCustomerId());
            preparedStatement.setString(8, booking.getBookingId());

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating booking details for ID: " + booking.getBookingId(), e);
        } finally {
            closeResources(connection, preparedStatement, null);
        }
        return false;
    }

    // Method to update the status of a booking
    public boolean updateBookingStatus(String bookingId, String status) {
        String query = "UPDATE booking SET status = ? WHERE booking_id = ?";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, status);
            statement.setString(2, bookingId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating booking status for ID: " + bookingId, e);
        } finally {
            closeResources(connection, statement, null);
        }
        return false;
    }

    // Method to delete a booking by its ID
    public boolean deleteBooking(String bookingId) {
        String query = "DELETE FROM booking WHERE booking_id = ?";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, bookingId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting booking with ID: " + bookingId, e);
        } finally {
            closeResources(connection, statement, null);
        }
        return false;
    }
    
    public int getBookingsCountByStatus(String status) {
        String query = "SELECT COUNT(*) FROM booking WHERE status = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int count = 0;

        try {
            connection = DBConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, status);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving booking count for status: " + status, e);
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return count;
    }

    
    public int getBookingsCountBetweenDates(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        String query = "SELECT COUNT(*) FROM booking WHERE booking_time BETWEEN ? AND ?";
        int count = 0;

        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setTimestamp(1, Timestamp.valueOf(startDate));
            statement.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving booking count between dates", e);
            throw e;  // Rethrow for proper handling
        }

        return count;
    }

    
    public double getTotalRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        String query = "SELECT SUM(b.total_amount) FROM bills b " +
                       "JOIN booking bk ON b.booking_id = bk.booking_id " +
                       "WHERE bk.booking_time BETWEEN ? AND ? AND b.status = 'Paid'";
        double totalRevenue = 0.0;

        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setTimestamp(1, Timestamp.valueOf(startDate));
            statement.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next() && resultSet.getObject(1) != null) {
                    totalRevenue = resultSet.getDouble(1);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving total revenue between dates", e);
            throw e;
        }

        return totalRevenue;
    }

    
    public List<Booking> getRecentBookings(int limit) throws SQLException {
        String query = "SELECT booking.*, customer.*, driver.*, vehicle.*, " +
                "booking.status AS booking_status, " +  // Alias for booking status
                "driver.status AS driver_status, " +    // Alias for driver status
                "vehicle.status AS vehicle_status, " +  // Alias for vehicle status
                "user_driver.name AS driver_name, user_driver.username AS driver_username, user_driver.password AS driver_password, " +
                "user_driver.role AS driver_role, user_driver.email AS driver_email, " +
                "user_driver.phone AS driver_phone, user_driver.last_login AS driver_last_login, " +
                "user_customer.name AS customer_name,  user_customer.username AS customer_username, user_customer.password AS customer_password, " +
                "user_customer.role AS customer_role, user_customer.email AS customer_email, " +
                "user_customer.phone AS customer_phone, user_customer.last_login AS customer_last_login " +
                "FROM booking " +
                "INNER JOIN customer ON booking.customer_id = customer.customer_id " +
                "INNER JOIN driver ON booking.driver_id = driver.driver_id " +
                "INNER JOIN vehicle ON booking.vehicle_id = vehicle.vehicle_id " +
                "INNER JOIN user AS user_driver ON driver.user_id = user_driver.user_id " +
                "INNER JOIN user AS user_customer ON customer.user_id = user_customer.user_id " +
                "ORDER BY booking.booking_time DESC " +
                "LIMIT ?";

        List<Booking> bookings = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, limit);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Fetch values from resultSet
                String bookingId = resultSet.getString("booking_id");
                LocalDateTime bookingTime = resultSet.getTimestamp("booking_time").toLocalDateTime();
                String pickupLocation = resultSet.getString("pickup_location");
                String destination = resultSet.getString("destination");
                float distance = resultSet.getFloat("distance");
                String status = resultSet.getString("booking_status");

                // Fetch related driver, vehicle, and customer data
                Driver driver = new Driver(
                    resultSet.getInt("driver_id"),
                    new User(
                        resultSet.getString("driver_name"),
                        resultSet.getString("driver_username"),
                        resultSet.getString("driver_password"),
                        resultSet.getString("driver_role"),
                        resultSet.getString("driver_email"),
                        resultSet.getString("driver_phone"),
                        resultSet.getTimestamp("driver_last_login") != null ?
                            resultSet.getTimestamp("driver_last_login").toLocalDateTime() : null
                    ),
                    resultSet.getString("license_number"),
                    resultSet.getString("driver_status")
                );

                Vehicle vehicle = new Vehicle(
            	    resultSet.getInt("vehicle_id"),
            	    resultSet.getString("plate_number"),
            	    resultSet.getString("model"),
            	    resultSet.getString("vehicle_status"),
            	    resultSet.getInt("driver_id"),
            	    resultSet.getTimestamp("created_at") != null ? resultSet.getTimestamp("created_at").toLocalDateTime() : null
            	);

                Customer customer = new Customer(
                    resultSet.getInt("customer_id"),
                    new User(
                        resultSet.getString("customer_name"),
                        resultSet.getString("customer_username"),
                        resultSet.getString("customer_password"),
                        resultSet.getString("customer_role"),
                        resultSet.getString("customer_email"),
                        resultSet.getString("customer_phone"),
                        resultSet.getTimestamp("customer_last_login") != null ?
                            resultSet.getTimestamp("customer_last_login").toLocalDateTime() : null
                    ),
                    resultSet.getString("registration_number"),
                    resultSet.getString("address"),
                    resultSet.getString("nic")
                );

                // Create the Booking object and add it to the list
                Booking booking = new Booking(bookingId, bookingTime, pickupLocation, destination, distance, status, driver, vehicle, customer);
                bookings.add(booking);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving recent bookings", e);
            throw e;  // Rethrow exception to handle it outside
        } finally {
            closeResources(connection, statement, resultSet);
        }
        return bookings;
    }


    // Helper method to close resources
    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error closing resources", e);
        }
    }
}