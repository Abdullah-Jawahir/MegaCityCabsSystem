package com.system.service;

import com.system.dao.BookingDAO;
import com.system.model.Booking;

import java.sql.SQLException;
import java.time.LocalDateTime;
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
    
    public int getActiveBookings() {
        return bookingDAO.getBookingsCountByStatus("assigned");
    }
    
    public int getTotalBookingsForPeriod(String period) {
        LocalDateTime startDate = getStartDateForPeriod(period);
        LocalDateTime endDate = LocalDateTime.now();
        try {
			return bookingDAO.getBookingsCountBetweenDates(startDate, endDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
    }
    
    public double getTotalRevenueForPeriod(String period) {
        LocalDateTime startDate = getStartDateForPeriod(period);
        LocalDateTime endDate = LocalDateTime.now();
        try {
			return bookingDAO.getTotalRevenueBetweenDates(startDate, endDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
    }
    
    public List<Booking> getRecentBookings(int limit) {
        try {
			return bookingDAO.getRecentBookings(limit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
 // Get bookings by customer ID (Service Layer)
    public List<Booking> getBookingsByCustomerId(int customerId) {
        try {
            return bookingDAO.getBookingsByCustomerId(customerId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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
