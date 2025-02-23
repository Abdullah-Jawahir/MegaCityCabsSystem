package com.system.service;

import com.system.dao.BookingDAO;
import com.system.model.Booking;

import java.sql.SQLException;
import java.util.List;

public class BookingService {

    private BookingDAO bookingDAO;

    public BookingService() {
        this.bookingDAO = new BookingDAO();
    }

    // Create a new booking
    public boolean createBooking(Booking booking) {
        try {
            return bookingDAO.createBooking(booking);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get a booking by its ID
    public Booking getBookingById(String bookingId) {
        try {
			return bookingDAO.getBookingById(bookingId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        try {
			return bookingDAO.getAllBookings();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    // Update the status of a booking
    public boolean updateBookingStatus(String bookingId, String status) {
        try {
            return bookingDAO.updateBookingStatus(bookingId, status);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a booking by its ID
    public boolean deleteBooking(String bookingId) {
        try {
            return bookingDAO.deleteBooking(bookingId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
 // Update booking details
    public boolean updateBookingDetails(Booking booking) {
        try {
            // Update the booking details in the database
            return bookingDAO.updateBookingDetails(booking);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
