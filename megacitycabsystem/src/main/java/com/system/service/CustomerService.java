package com.system.service;

import com.system.dao.CustomerDAO;
import com.system.model.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CustomerService {

    private CustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    // Add a new customer
    public boolean addCustomer(Connection connection, Customer customer) throws SQLException {
        boolean isAdded = customerDAO.addCustomer(connection, customer);
        if (!isAdded) {
            throw new SQLException("Failed to add customer.");
        }
        return true;
    }

    // Get a customer by registration number
    public Customer getCustomerByRegistrationNumber(String registrationNumber) {
        try {
            return customerDAO.getCustomerByRegistrationNumber(registrationNumber);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
        }
        return null;
    }

    // Get a customer by customer ID
    public Customer getCustomerById(int customerId) {
        try {
            return customerDAO.getCustomerById(customerId);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
        }
        return null;
    }

    // Get all customers
    public List<Customer> getAllCustomers() {
        try {
            return customerDAO.getAllCustomers();
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception
        }
        return null;
    }

    // Update customer details
    public boolean updateCustomer(Customer customer) {
        try {
            boolean isUpdated = customerDAO.updateCustomer(customer);
            if (!isUpdated) {
                throw new Exception("Failed to update customer.");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
        }
        return false;
    }

    // Delete a customer
    public boolean deleteCustomer(int customerId) {
        try {
            boolean isDeleted = customerDAO.deleteCustomer(customerId);
            if (!isDeleted) {
                throw new Exception("Failed to delete customer.");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
        }
        return false;
    }
    
    public String generateNextRegistrationNumber() {
        String lastRegNumber = customerDAO.getLastRegistrationNumber(); // Fetch last register number from DB
        int nextNumber = 1; // Default if no customers exist

        if (lastRegNumber != null && lastRegNumber.startsWith("CUST")) {
            String numberPart = lastRegNumber.substring(4); // Extract numeric part
            nextNumber = Integer.parseInt(numberPart) + 1;  // Increment the number
        }

        return String.format("CUST%03d", nextNumber); // Format as CUST001, CUST002, etc.
    }
    
    /**
     * Retrieves customer details based on the user ID from the users table.
     * This method is used to get customer information for the currently logged-in user.
     *
     * @param userId The ID of the user from the users table
     * @return Customer object if found, null otherwise
     */
    public Customer getCustomerByUserId(int userId) {
        try {
            return customerDAO.getCustomerByUserId(userId);
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception
            return null;
        }
    }


}
