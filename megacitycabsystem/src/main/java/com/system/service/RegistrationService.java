package com.system.service;


import com.system.model.Customer;
import com.system.model.Driver;
import com.system.model.User;
import com.system.utils.DBConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;

public class RegistrationService {

    private final UserService userService;
    private final DriverService driverService;
    private final CustomerService customerService;

    public RegistrationService() {
        this.userService = new UserService();
        this.driverService = new DriverService();
        this.customerService = new CustomerService();
    }
    
    /**
     * Registers a new customer. Both the user and customer records are inserted within a single transaction.
     * 
     * @param user The User object containing common user details.
     * @param registrationNumber The unique registration number for the customer.
     * @param address The customer's address.
     * @param nic The customer's NIC (National Identity Card) number.
     * @return true if registration is successful, false otherwise.
     */
    public boolean registerCustomer(User user, String registrationNumber, String address, String nic) {
        Connection connection = null;
        try {
            connection = DBConnectionFactory.getConnection();
            // Begin transaction
            connection.setAutoCommit(false);
            
            // Insert User using the connection-aware method
            int userId = userService.addUser(connection, user);
            if (userId <= 0) {
                connection.rollback();
                return false;
            }
            user.setId(userId);
            
            // Create and insert Customer using the connection-aware method
            Customer customer = new Customer(0, user, registrationNumber, address, nic);
            boolean customerInserted = customerService.addCustomer(connection, customer);
            if (!customerInserted) {
                connection.rollback();
                return false;
            }
            
            // Commit the transaction if both inserts succeed
            connection.commit();
            return true;
        } catch (Exception e) {
            // Roll back in case of any error during the transaction
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            // Ensure auto-commit is reset and the connection is closed
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Registers a new driver. Both the user and driver records are inserted within a single transaction.
     * 
     * @param user The User object containing common user details.
     * @param licenseNumber The driver's license number.
     * @param status The status of the driver (e.g., "Available").
     * @return true if registration is successful, false otherwise.
     */
    public boolean registerDriver(User user, String licenseNumber, String status) {
        Connection connection = null;
        try {
            connection = DBConnectionFactory.getConnection();
            // Begin transaction
            connection.setAutoCommit(false);
            
            // Insert User using the connection-aware method
            int userId = userService.addUser(connection, user);
            if (userId <= 0) {
                connection.rollback();
                return false;
            }
            user.setId(userId);
            
            // Create and insert Driver using the connection-aware method
            Driver driver = new Driver(0, user, licenseNumber, status);
            boolean driverInserted = driverService.addDriver(connection, driver);
            if (!driverInserted) {
                connection.rollback();
                return false;
            }
            
            // Commit the transaction if both inserts succeed
            connection.commit();
            return true;
        } catch (Exception e) {
            // Roll back in case of any error during the transaction
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            // Ensure auto-commit is reset and the connection is closed
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
