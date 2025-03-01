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

public class BookingProcessController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private VehicleService vehicleService;
    private DriverService driverService;
    private CustomerService customerService;
    private BillService billService;
    private BookingService bookingService;

    private static final Logger logger = Logger.getLogger(BookingProcessController.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();
        vehicleService = new VehicleService();
        driverService = new DriverService();
        customerService = new CustomerService();
        billService = new BillService();
        bookingService = new BookingService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get the current session
            HttpSession session = request.getSession(false);

            // Check if user is logged in
            User user = (session != null) ? (User) session.getAttribute("user") : null;

            if (user == null) {
                // Store locations temporarily for later use
                session = request.getSession(true);
                session.setAttribute("pendingPickupLocation", request.getParameter("pickupLocation"));
                session.setAttribute("pendingDropLocation", request.getParameter("dropLocation"));
                session.setAttribute("pendingDistance", request.getParameter("distance"));
                session.setAttribute("pendingSelectedVehicleId", request.getParameter("selectedVehicleId"));
                session.setAttribute("message", "Please log in to continue with your booking.");

                // Redirect to the login controller
                response.sendRedirect("login");
                return;
            }

            // User is logged in, proceed with booking
            String pickupLocation = request.getParameter("pickupLocation");
            String dropLocation = request.getParameter("dropLocation");
            String distanceStr = request.getParameter("distance");
            int selectedVehicleId = Integer.parseInt(request.getParameter("selectedVehicleId"));

            // Validate that distance is present
            if (distanceStr == null || distanceStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Distance is required to continue with booking. Please select it by using map.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }

            // Parse distance
            float distance = Float.parseFloat(distanceStr);

            // Generate booking ID
            String bookingId = "BK" + UUID.randomUUID().toString().substring(0, 8);

            // Get first available vehicle
            Vehicle vehicle = vehicleService.getVehicleById(selectedVehicleId);
            if (vehicle == null) {
                request.setAttribute("errorMessage", "No vehicles currently available. Please try again later.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }

            // Get driver assigned to this vehicle
            Driver driver = driverService.getDriverByVehicleId(vehicle.getVehicleId());
            if (driver == null) {
                request.setAttribute("errorMessage", "No driver available for the vehicle. Please try again later.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }

            // Get customer details from logged-in user
            Customer customer = customerService.getCustomerByUserId(user.getId());
            if (customer == null) {
                request.setAttribute("errorMessage", "Customer details not found. Please update your profile.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }

            // Create the Booking object (status: in-progress)
            Booking booking = new Booking(
                    bookingId,
                    LocalDateTime.now(),
                    pickupLocation,
                    dropLocation,
                    distance,
                    "in-progress",
                    driver,
                    vehicle,
                    customer
            );

            // Calculate fare
            float baseAmount = billService.calculateBaseAmount(booking);
            float taxAmount = billService.calculateTaxAmount(baseAmount);
            float discountAmount = billService.calculateDiscountAmount(baseAmount + taxAmount);
            float totalAmount = baseAmount + taxAmount - discountAmount;

            // Generate a bill (status: unpaid)
            String billId = "BILL" + UUID.randomUUID().toString().substring(0, 6);
            Bill bill = new Bill(billId, booking, baseAmount, taxAmount, discountAmount, totalAmount, "pending", user);
            
            // Store the booking & bill in request
            request.setAttribute("booking", booking);
            request.setAttribute("bill", bill);

            // Forward to confirmation page where user sees details before confirming
            request.getRequestDispatcher("/WEB-INF/views/booking/confirm.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid distance format. Please enter a valid number using decimal points.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in booking process", e);
            request.setAttribute("errorMessage", "An error occurred while processing your booking. Please try again.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}