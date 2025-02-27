package com.system.dao;

import com.system.model.Customer;
import com.system.model.User;
import com.system.utils.DBConnectionFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerDAO {
    private static final Logger logger = Logger.getLogger(CustomerDAO.class.getName());

    public boolean addCustomer(Connection connection, Customer customer) {
        String sql = "INSERT INTO customer (user_id, registration_number, address, nic) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, customer.getUser().getId());  // Get the userId from the User object
            statement.setString(2, customer.getRegistrationNumber());
            statement.setString(3, customer.getAddress());
            statement.setString(4, customer.getNic());

            return statement.executeUpdate() > 0; // Returns true if insertion is successful
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding customer", e);
            return false;
        } finally {
            // Only close the statement, since the connection is managed externally
            closeResources(null, statement, null);
        }
    }

    // Retrieve a customer by registration number
    public Customer getCustomerByRegistrationNumber(String registrationNumber) {
        String sql = "SELECT * FROM customer WHERE registration_number = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, registrationNumber);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Create the User object from the result set
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

                return new Customer(
                        resultSet.getInt("customer_id"),
                        user,  // Set the full User object
                        resultSet.getString("registration_number"),
                        resultSet.getString("address"),
                        resultSet.getString("nic")
                );
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving customer by registration number", e);
        } finally {
            closeResources(connection, statement, resultSet);
        }
        return null;
    }

    // Retrieve a customer by customer ID
    public Customer getCustomerById(int customerId) throws SQLException {
    	Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnectionFactory.getConnection();
            String sql = "SELECT customer.*, user.name, user.username, user.password, user.role, " +
                    "user.email, user.phone, user.last_login " +
                    "FROM customer " +
                    "INNER JOIN user ON customer.user_id = user.user_id " +
                    "WHERE customer.customer_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, customerId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Create the User object from the result set
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

                // Return the Customer object with the full User object
                return new Customer(
                        resultSet.getInt("customer_id"),
                        user,  // Pass the fully populated User object
                        resultSet.getString("registration_number"),
                        resultSet.getString("address"),
                        resultSet.getString("nic")
                );
            }
            return null;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving customer by ID", e);
            throw e;
        } finally {
            closeResources(connection, statement, resultSet);
        }
        
    }

    // Retrieve all customers
    public List<Customer> getAllCustomers() throws SQLException {
    	Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Customer> customers = new ArrayList<>();
        

        try {
            connection = DBConnectionFactory.getConnection();
            String sql = "SELECT customer.*, user.name, user.username, user.password, user.role, " +
                    "user.email, user.phone, user.last_login " +
                    "FROM customer " +
                    "INNER JOIN user ON customer.user_id = user.user_id";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Create the User object from the result set
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

                // Add the Customer object to the list
                customers.add(new Customer(
                        resultSet.getInt("customer_id"),
                        user,  // Set the full User object
                        resultSet.getString("registration_number"),
                        resultSet.getString("address"),
                        resultSet.getString("nic")
                ));
            }
            
            return customers;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving all customers", e);
            throw e;
        } finally {
            closeResources(connection, statement, resultSet);
        }
        
    }
    
 // Retrieve a customer by the user ID from the users table
    public Customer getCustomerByUserId(int userId) throws SQLException {
        String sql = "SELECT customer.*, user.name, user.username, user.password, user.role, " +
                     "user.email, user.phone, user.last_login " +
                     "FROM customer " +
                     "INNER JOIN user ON customer.user_id = user.user_id " +
                     "WHERE customer.user_id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DBConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                // Create the User object from the result set
                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("role"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getTimestamp("last_login") != null 
                            ? resultSet.getTimestamp("last_login").toLocalDateTime() 
                            : null
                );
                user.setId(resultSet.getInt("user_id"));
                
                // Return the Customer object with the full User object
                return new Customer(
                        resultSet.getInt("customer_id"),
                        user,
                        resultSet.getString("registration_number"),
                        resultSet.getString("address"),
                        resultSet.getString("nic")
                );
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving customer by user ID: " + userId, e);
            throw e;
        } finally {
            closeResources(connection, statement, resultSet);
        }
        
        return null;
    }



    // Update customer details
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customer SET registration_number = ?, address = ?, nic = ? WHERE customer_id = ?";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setString(1, customer.getRegistrationNumber());
            statement.setString(2, customer.getAddress());
            statement.setString(3, customer.getNic());
            statement.setInt(4, customer.getCustomerId()); // Update by customer ID

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating customer", e);
        } finally {
            closeResources(connection, statement, null);
        }
        return false;
    }

    // Delete a customer
    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, customerId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting customer", e);
        } finally {
            closeResources(connection, statement, null);
        }
        return false;
    }
    
    
    public String getLastRegistrationNumber() {
        String sql = "SELECT registration_number FROM customer ORDER BY customer_id DESC LIMIT 1";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnectionFactory.getConnection(); 
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
 
            if (resultSet.next()) {
                return resultSet.getString("registration_number");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving last registration number", e);
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return null; 
    }
    
    public int getTotalCustomers() {
        String query = "SELECT COUNT(*) FROM customer";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnectionFactory.getConnection(); 
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving total customers count", e);
        } finally {
            closeResources(connection, statement, resultSet);
        }
        
        return 0;
    }

    
    public int getCustomersCountBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        String query = "SELECT COUNT(*) FROM customer c " +
                       "JOIN user u ON c.user_id = u.user_id " +
                       "WHERE u.last_login BETWEEN ? AND ?";

        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setTimestamp(1, Timestamp.valueOf(startDate));
            statement.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    
    // Helper method to close resources
    private void closeResources(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) resultSet.close();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error closing ResultSet", e);
        }

        try {
            if (statement != null) statement.close();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error closing Statement", e);
        }

        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error closing Connection", e);
        }
    }
    
    

}