package com.system.controller;

import com.system.model.*;
import com.system.service.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BillingController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookingService bookingService;
    private BillService billService;
    private VehicleService vehicleService;
    private DriverService driverService;
    private CustomerService customerService;

    private static final Logger logger = Logger.getLogger(BillingController.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();
        bookingService = new BookingService();
        billService = new BillService();
        vehicleService = new VehicleService();
        driverService = new DriverService();
        customerService = new CustomerService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            User user = (session != null) ? (User) session.getAttribute("user") : null;

            if (user == null) {
                response.sendRedirect("login");
                return;
            }

            // Retrieve the action parameter to determine the process
            String action = request.getParameter("action");

            if ("updateRideStatus".equals(action)) {
                // Retrieve parameters for updating status
                String bookingId = request.getParameter("bookingId");
                String billId = request.getParameter("billId");

                //Get booking from service
                Booking booking = bookingService.getBookingById(bookingId);

                if (booking == null) {
                    request.setAttribute("errorMessage", "Booking for this ID is not found.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }

                // Extract vehicle ID from the booking
                Vehicle assignedVehicle = booking.getAssignedVehicle();
                if (assignedVehicle == null) {
                    request.setAttribute("errorMessage", "Vehicle for this Booking ID is not found.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }

                int vehicleId = assignedVehicle.getVehicleId();

                // Update booking status to "completed" (or another appropriate status)
                boolean bookingUpdated = bookingService.updateBookingStatus(bookingId, "completed");

                // Update bill status to "paid"
                boolean billUpdated = billService.updateBillStatus(billId, "paid");

                // Update vehicle status to "Active"
                boolean vehicleStatusUpdated = vehicleService.updateVehicleStatus(vehicleId, "Active");

                if (bookingUpdated && billUpdated && vehicleStatusUpdated) {
                    // Forward to a success page or confirmation page
                    request.setAttribute("bookingId", bookingId); // Pass bookingId as a request attribute
                    request.getRequestDispatcher("/WEB-INF/views/booking/paymentSuccess.jsp").forward(request, response);
                } else {
                    // Forward to an error page if the update fails
                    request.setAttribute("errorMessage", "An error occurred while updating payment status.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                }
            } else if ("viewPendingBill".equals(action)) {
                String bookingId = request.getParameter("bookingId");
                Bill bill = billService.getBillByBookingId(bookingId);

                String billId = bill.getBillId();

                if (billId != null) {
                    // Redirect to payment page
                    response.sendRedirect("booking/payment?bookingId=" + bookingId + "&billId=" + billId);
                } else {
                    request.setAttribute("errorMessage", "No Bill found for the requested Booking Id");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                }
            } else {
            	// Existing code to handle booking creation
                String pickupLocation = request.getParameter("pickupLocation");
                String dropLocation = request.getParameter("dropLocation");
                float distance = Float.parseFloat(request.getParameter("distance"));
                float baseAmount = Float.parseFloat(request.getParameter("baseAmount"));
                float taxAmount = Float.parseFloat(request.getParameter("taxAmount"));
                float discountAmount = Float.parseFloat(request.getParameter("discountAmount"));
                float totalAmount = Float.parseFloat(request.getParameter("totalAmount"));
                
                // Parse request parameters to integers
                int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
                int driverId = Integer.parseInt(request.getParameter("driverId"));
                int customerId = Integer.parseInt(request.getParameter("customerId"));

                // Fetch full objects instead of passing just IDs
                Driver driver = driverService.getDriverById(driverId);
                Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
                Customer customer = customerService.getCustomerById(customerId);

                if (driver == null || vehicle == null || customer == null) {
                    request.setAttribute("errorMessage", "Invalid driver, vehicle, or customer data.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }

                // Get bookingId from request, if provided
                String bookingId = request.getParameter("bookingId");
                
                // If bookingId is not provided in the request, handle it appropriately
                if (bookingId == null || bookingId.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "Booking ID is required.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }

                // Create booking object
                Booking booking = new Booking(
                        bookingId,
                        LocalDateTime.now(),
                        pickupLocation,
                        dropLocation,
                        distance,
                        "assigned",
                        driver,
                        vehicle,
                        customer
                );

                // Save booking in the database
                bookingService.createBooking(booking);
                
             // Update the vehicle status to 'Booked'
                boolean vehicleStatusUpdated = vehicleService.updateVehicleStatus(vehicleId, "Booked");
                if (!vehicleStatusUpdated) {
                    request.setAttribute("errorMessage", "Vehicle can not be updated.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }

                // Generate unique bill ID
                String billId =  request.getParameter("billId");

                // Create bill object
                Bill bill = new Bill(billId, booking, baseAmount, taxAmount, discountAmount, totalAmount, "pending", user);

                // Save bill in database
                billService.createBill(bill);
                
             // Redirect to payment page
                response.sendRedirect("booking/payment?bookingId=" + bookingId + "&billId=" + billId
                        + "&baseAmount=" + baseAmount + "&taxAmount=" + taxAmount + "&discountAmount=" + discountAmount + "&totalAmount=" + totalAmount);             

            }
        } catch (Exception e) {
            getServletContext().log("Error processing billing", e);
            request.setAttribute("errorMessage", "An error occurred while processing your booking.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}