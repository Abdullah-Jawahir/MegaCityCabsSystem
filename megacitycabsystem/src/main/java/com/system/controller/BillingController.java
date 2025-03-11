package com.system.controller;

import com.system.model.*;
import com.system.observer.BookingObserver;
import com.system.service.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BillingController extends HttpServlet {

    private static final String ACTION_UPDATE_RIDE_STATUS = "updateRideStatus";
    private static final String ACTION_VIEW_PENDING_BILL = "viewPendingBill";
    private static final String ACTION_PRINT_BILL = "printBill";
    private static final String BILL_STATUS_PAID = "paid";
    private static final String VEHICLE_STATUS_BOOKED = "Booked";
    private static final String ERROR_PAGE = "/error.jsp";
    private static final String PAYMENT_SUCCESS_PAGE = "/WEB-INF/views/booking/paymentSuccess.jsp";
    private final List<BookingObserver> observers = new ArrayList<>();
   
    
    private BookingService bookingService;
    private BillService billService;
    private VehicleService vehicleService;
    private DriverService driverService;
    private CustomerService customerService;

    @Override
    public void init() throws ServletException {
        super.init();
        
        // Initialize services
        bookingService = new BookingService();
        billService = new BillService();
        vehicleService = new VehicleService();
        driverService = new DriverService();
        customerService = new CustomerService();
        
        observers.add(new EmailService());
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

            String action = request.getParameter("action");

            if (ACTION_UPDATE_RIDE_STATUS.equals(action)) {
                updateRideStatus(request, response);
            } else if (ACTION_VIEW_PENDING_BILL.equals(action)) {
                viewPendingBill(request, response);
            } else if (ACTION_PRINT_BILL.equals(action)) {
                handlePrintBill(request, response);
            } else {
                createBookingAndBill(request, response, user);
            }

        } catch (Exception e) {
            getServletContext().log("Error processing billing", e);
            request.setAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }
    }
    
    private void notifyObservers(Booking booking, String billId) {
        for (BookingObserver observer : observers) {
            observer.onBookingConfirmed(booking, billId);
        }
    }

    private void updateRideStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookingId = request.getParameter("bookingId");
        String billId = request.getParameter("billId");
        String paymentType = request.getParameter("paymentType"); 

        Booking booking = bookingService.getBookingById(bookingId);

        if (booking == null) {
            request.setAttribute("errorMessage", "Booking for this ID is not found.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }

        // Update payment type *before* updating the bill status
        boolean paymentTypeUpdated = billService.updateBillPaymentType(billId, paymentType);

        if (!paymentTypeUpdated) {
            request.setAttribute("errorMessage", "An error occurred while updating the payment type.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;  
        }

        // Update bill status only, triggers will handle the rest
        boolean billUpdated = billService.updateBillStatus(billId, BILL_STATUS_PAID);

        if (billUpdated) {
            request.setAttribute("bookingId", bookingId);
            request.getRequestDispatcher(PAYMENT_SUCCESS_PAGE).forward(request, response);

            notifyObservers(booking, billId);

        } else {
            request.setAttribute("errorMessage", "An error occurred while updating payment status.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }
    }
    
    private void viewPendingBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookingId = request.getParameter("bookingId");
        Bill bill = billService.getBillByBookingId(bookingId);

        if (bill == null || bill.getBillId() != null) {
            response.sendRedirect("booking/payment?bookingId=" + bookingId + "&billId=" + bill.getBillId());
        } else {
            request.setAttribute("errorMessage", "No Bill found for the requested Booking Id");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }

    }

    private void handlePrintBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookingId = request.getParameter("bookingId");
        Booking booking = bookingService.getBookingById(bookingId);

        if (booking == null) {
            request.setAttribute("errorMessage", "Booking not found.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }

        Bill bill = billService.getBillByBookingId(bookingId);

        if (bill == null) {
            request.setAttribute("errorMessage", "Bill not found for this booking.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }
        // Convert LocalDateTime to Date

        LocalDateTime ldt = booking.getBookingTime();

        java.util.Date date = java.util.Date.from(ldt.atZone(java.time.ZoneId.systemDefault()).toInstant());


        // Set attributes for the JSP
        request.setAttribute("booking", booking);
        request.setAttribute("bill", bill);
        request.setAttribute("date",date);


        request.getRequestDispatcher("/WEB-INF/views/booking/printBill.jsp").forward(request, response);
    }

    private void createBookingAndBill(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        String pickupLocation = request.getParameter("pickupLocation");
        String dropLocation = request.getParameter("dropLocation");
        float distance = Float.parseFloat(request.getParameter("distance"));
        float baseAmount = Float.parseFloat(request.getParameter("baseAmount"));
        float taxAmount = Float.parseFloat(request.getParameter("taxAmount"));
        float discountAmount = Float.parseFloat(request.getParameter("discountAmount"));
        float totalAmount = Float.parseFloat(request.getParameter("totalAmount"));

        Integer vehicleId = null;
        Integer driverId = null;
        try {
            String vehicleIdParam = request.getParameter("vehicleId");
            if (vehicleIdParam != null && !vehicleIdParam.isEmpty()) {
                vehicleId = Integer.parseInt(vehicleIdParam);
            }

            String driverIdParam = request.getParameter("driverId");
            if (driverIdParam != null && !driverIdParam.isEmpty()) {
                driverId = Integer.parseInt(driverIdParam);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid number format for vehicle or driver.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }

        int customerId = Integer.parseInt(request.getParameter("customerId"));

        if (vehicleId == null) {
            request.setAttribute("errorMessage", "Vehicle ID is required.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }

        Driver driver = (driverId != null) ? driverService.getDriverById(driverId) : null;
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        Customer customer = customerService.getCustomerById(customerId);

        if (driver == null || vehicle == null || customer == null) {
            request.setAttribute("errorMessage", "Invalid driver, vehicle, or customer data.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }

        String bookingId = request.getParameter("bookingId");

        if (bookingId == null || bookingId.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Booking ID is required.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }

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

        boolean bookingCreated = bookingService.createBooking(booking);

        if (bookingCreated) {
            boolean vehicleStatusUpdated = vehicleService.updateVehicleStatus(vehicleId, VEHICLE_STATUS_BOOKED);
            if (!vehicleStatusUpdated) {
                request.setAttribute("errorMessage", "Vehicle can not be updated.");
                request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
                return;
            }

            String billId = request.getParameter("billId");

            Bill bill = new Bill(billId, booking, baseAmount, taxAmount, discountAmount, totalAmount, "pending", user);

            billService.createBill(bill);

            response.sendRedirect("booking/payment?bookingId=" + bookingId + "&billId=" + billId
                    + "&baseAmount=" + baseAmount + "&taxAmount=" + taxAmount + "&discountAmount=" + discountAmount + "&totalAmount=" + totalAmount);
        }
    }
}