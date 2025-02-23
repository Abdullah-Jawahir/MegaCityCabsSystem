package com.system.controller;

import com.system.model.Bill;
import com.system.model.Booking;
import com.system.model.Customer;
import com.system.model.Driver;
import com.system.model.Vehicle;
import com.system.model.VehicleDriverWrapper;
import com.system.service.BillService;
import com.system.service.BookingService;
import com.system.service.CustomerService;
import com.system.service.DriverService;
import com.system.service.VehicleService;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class BookingController extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BookingService bookingService;
	private DriverService driverService;
	private VehicleService vehicleService;
	private CustomerService customerService;
	private BillService billService;

    @Override
    public void init() throws ServletException {
        super.init();
        bookingService = new BookingService();
        driverService = new DriverService();
        vehicleService = new VehicleService();
        customerService = new CustomerService();
        billService = new BillService();
        
    }

    // Handle GET requests for viewing all bookings
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null || "manageBookings".equals(action)) {
            List<Booking> bookings = bookingService.getAllBookings();
            request.setAttribute("bookings", bookings);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin/manageBookings.jsp");
            dispatcher.forward(request, response);
        } else if ("editBookingDetails".equals(action)) {
            String bookingId = request.getParameter("bookingId");
            Booking booking = bookingService.getBookingById(bookingId);

            if (booking != null) {
                // Get current vehicle ID
                int currentVehicleId = (booking.getAssignedVehicle() != null) ? 
                                     booking.getAssignedVehicle().getVehicleId() : -1;
                
                // Get all vehicles
                List<Vehicle> vehicles = vehicleService.getAllVehicles();
                
                // Create a list of wrapped vehicles with their drivers
                List<VehicleDriverWrapper> availableVehicles = new ArrayList<>();
                
                for (Vehicle vehicle : vehicles) {
                    Driver driver = driverService.getDriverByVehicleId(vehicle.getVehicleId());
                    availableVehicles.add(new VehicleDriverWrapper(vehicle, driver));
                }
                
                // If the current vehicle isn't in the available list, add it
                if (currentVehicleId != -1) {
                    boolean found = vehicles.stream()
                        .anyMatch(v -> v.getVehicleId() == currentVehicleId);
                    
                    if (!found) {
                        Vehicle currentVehicle = vehicleService.getVehicleById(currentVehicleId);
                        if (currentVehicle != null) {
                            Driver currentDriver = driverService.getDriverByVehicleId(currentVehicleId);
                            availableVehicles.add(new VehicleDriverWrapper(currentVehicle, currentDriver));
                        }
                    }
                }

                // Get customers for the dropdown
                List<Customer> customers = customerService.getAllCustomers();

                // Set attributes for the JSP
                request.setAttribute("booking", booking);
                request.setAttribute("availableVehicles", availableVehicles);
                request.setAttribute("customers", customers);

                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin/bookings/editBookingDetails.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect("booking?action=manageBookings");
            }
        } else if ("deleteBookingDetails".equals(action)) {
            // Handle delete request
            String bookingId = request.getParameter("bookingId");
            boolean isSuccess = bookingService.deleteBooking(bookingId);
            
            if (isSuccess) {
                response.sendRedirect("booking?success=Booking details deleted successfully");
            } else {
                request.setAttribute("errorMessage", "Failed to delete booking.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        }
    }

    // Handle POST requests for creating or updating bookings
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("createBooking".equals(action)) {
            String bookingId = request.getParameter("bookingId");
            String pickupLocation = request.getParameter("pickupLocation");
            String destination = request.getParameter("destination");
            float distance = Float.parseFloat(request.getParameter("distance"));
            String status = request.getParameter("status");

            // Create a new booking object
            Booking booking = new Booking(bookingId, LocalDateTime.now(), pickupLocation, destination, distance, status, null, null, null);
            boolean isSuccess = bookingService.createBooking(booking);
            if (isSuccess) {
                response.sendRedirect("booking");
            } else {
                request.setAttribute("errorMessage", "Failed to create booking.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        } else if ("updateStatus".equals(action)) {
            String bookingId = request.getParameter("bookingId");
            String status = request.getParameter("status");
            boolean isSuccess = bookingService.updateBookingStatus(bookingId, status);
            if (isSuccess) {
                response.sendRedirect("booking");
            } else {
                request.setAttribute("errorMessage", "Failed to update booking status.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        } else if ("updateBookingDetails".equals(action)) {
            // Existing booking details retrieval
            String bookingId = request.getParameter("bookingId");
            String pickupLocation = request.getParameter("pickupLocation");
            String destination = request.getParameter("destination");
            float distance = Float.parseFloat(request.getParameter("distance"));
            String status = request.getParameter("status");

            // Retrieve IDs
            String driverIdStr = request.getParameter("driverId");
            String vehicleIdStr = request.getParameter("vehicleId");
            String customerIdStr = request.getParameter("customerId");

            // Convert IDs
            int driverId = (driverIdStr != null && !driverIdStr.isEmpty()) ? Integer.parseInt(driverIdStr) : -1;
            int vehicleId = (vehicleIdStr != null && !vehicleIdStr.isEmpty()) ? Integer.parseInt(vehicleIdStr) : -1;
            int customerId = (customerIdStr != null && !customerIdStr.isEmpty()) ? Integer.parseInt(customerIdStr) : -1;

            // Fetch existing booking and bill
            Booking existingBooking = bookingService.getBookingById(bookingId);
            Bill existingBill = billService.getBillByBookingId(bookingId);

            if (existingBooking != null && existingBill != null) {
                // Preserve booking time
                LocalDateTime bookingTime = existingBooking.getBookingTime();

                // Fetch related entities
                Driver assignedDriver = driverService.getDriverById(driverId);
                Vehicle assignedVehicle = vehicleService.getVehicleById(vehicleId);
                Customer customer = customerService.getCustomerById(customerId);

                // Create updated booking
                Booking updatedBooking = new Booking(
                    bookingId, bookingTime, pickupLocation, destination, 
                    distance, status, assignedDriver, assignedVehicle, customer
                );

                // Start transaction
                boolean success = true;
                try {
                    // Update booking
                    success = bookingService.updateBookingDetails(updatedBooking);

                    if (success) {
                        // Recalculate and update bill
                        Bill updatedBill = billService.recalculateBill(existingBill, updatedBooking);
                        success = billService.updateBill(updatedBill);
                    }

                    if (success) {
                        response.sendRedirect("booking?success=Booking and bill updated successfully");
                    } else {
                        throw new Exception("Failed to update booking or bill");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("errorMessage", "Failed to update booking details and billing information.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                    dispatcher.forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "Booking or bill not found.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        }

    }
    
    @Override
    public void destroy() {
        super.destroy();
    }
}
