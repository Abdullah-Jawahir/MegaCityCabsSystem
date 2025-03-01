package com.system.dao;

import com.system.model.Bill;
import com.system.model.Booking;
import com.system.model.User;
import com.system.utils.DBConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BillDAO {
	
	private static final Logger logger = Logger.getLogger(BillDAO.class.getName());

	 // Method to create a new bill in the database
    public boolean createBill(Bill bill) {
        String query = "INSERT INTO Bills (bill_id, booking_id, base_amount, tax_amount, discount_amount, total_amount, status, generated_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, bill.getBillId());
            statement.setString(2, bill.getBooking().getBookingId());
            statement.setFloat(3, bill.getBaseAmount());
            statement.setFloat(4, bill.getTaxAmount());
            statement.setFloat(5, bill.getDiscountAmount());
            statement.setFloat(6, bill.getTotalAmount());
            statement.setString(7, bill.getStatus());
            statement.setInt(8, bill.getGeneratedBy().getId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating bill: " + e.getMessage(), e);
            e.printStackTrace();
        }
        return false;
    }

    // Method to get a bill by its ID
    public Bill getBillById(String billId) {
        String query = "SELECT * FROM Bills WHERE bill_id = ?";

        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, billId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Fetch all fields from the resultSet first
                String billIdResult = resultSet.getString("bill_id");
                String bookingId = resultSet.getString("booking_id");
                float baseAmount = resultSet.getFloat("base_amount");
                float taxAmount = resultSet.getFloat("tax_amount");
                float discountAmount = resultSet.getFloat("discount_amount");
                float totalAmount = resultSet.getFloat("total_amount");
                String status = resultSet.getString("status");
                int generatedById = resultSet.getInt("generated_by");

                // Now that we have all necessary data from resultSet, fetch related entities
                Booking booking = new BookingDAO().getBookingById(bookingId);
                User generatedBy = new UserDAO().getUserById(generatedById);

                // Return the fully populated Bill object
                return new Bill(billIdResult, booking, baseAmount, taxAmount, discountAmount, totalAmount, status, generatedBy);
            }
        } catch (SQLException e) {
            // Log the error with the billId to help with debugging
            logger.log(Level.SEVERE, "Error retrieving bill by ID: " + billId, e);
        }
        return null;  // Return null if no bill is found or an error occurs
    }


    // Method to get all bills
    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM Bills";

        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String billId = resultSet.getString("bill_id");
                String bookingId = resultSet.getString("booking_id");
                float baseAmount = resultSet.getFloat("base_amount");
                float taxAmount = resultSet.getFloat("tax_amount");
                float discountAmount = resultSet.getFloat("discount_amount");
                float totalAmount = resultSet.getFloat("total_amount");
                String status = resultSet.getString("status");

                Booking booking = new BookingDAO().getBookingById(bookingId);
                User generatedBy = new UserDAO().getUserById(resultSet.getInt("generated_by"));

                Bill bill = new Bill(billId, booking, baseAmount, taxAmount, discountAmount, totalAmount, status, generatedBy);
                bills.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    // Method to update the status of a bill
    public boolean updateBillStatus(String billId, String status) {
        String query = "UPDATE Bills SET status = ? WHERE bill_id = ?";

        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, status);
            statement.setString(2, billId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Method to get a bill by booking ID
    public Bill getBillByBookingId(String bookingId) {
        String query = "SELECT * FROM Bills WHERE booking_id = ?";

        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, bookingId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String billId = resultSet.getString("bill_id");
                float baseAmount = resultSet.getFloat("base_amount");
                float taxAmount = resultSet.getFloat("tax_amount");
                float totalAmount = resultSet.getFloat("total_amount");
                float discountAmount = resultSet.getFloat("discount_amount");
                String status = resultSet.getString("status");
                int generatedById = resultSet.getInt("generated_by");

                Booking booking = new BookingDAO().getBookingById(bookingId);
                User generatedBy = new UserDAO().getUserById(generatedById);

                return new Bill(billId, booking, baseAmount, taxAmount, discountAmount, totalAmount, status, generatedBy);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving bill by booking ID: " + bookingId, e);
        }
        return null;
    }
    
    // Method to update a bill
    public boolean updateBill(Bill bill) {
        String query = "UPDATE Bills SET base_amount = ?, tax_amount = ?, discount_amount = ?, total_amount = ?, status = ?, generated_by = ? WHERE bill_id = ?";

        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setFloat(1, bill.getBaseAmount());
            statement.setFloat(2, bill.getTaxAmount());
            statement.setFloat(3, bill.getDiscountAmount());
            statement.setFloat(4, bill.getTotalAmount());
            statement.setString(5, bill.getStatus());
            statement.setInt(6, bill.getGeneratedBy().getId());
            statement.setString(7, bill.getBillId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating bill: " + bill.getBillId(), e);
             e.printStackTrace();
        }
        return false;
    }

    // Method to delete a bill by its ID
    public boolean deleteBill(String billId) {
        String query = "DELETE FROM Bills WHERE bill_id = ?";

        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, billId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
