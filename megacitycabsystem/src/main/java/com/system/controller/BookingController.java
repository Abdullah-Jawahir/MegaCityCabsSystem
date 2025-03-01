package com.system.controller;

import com.system.model.Bill;
import com.system.model.Booking;
import com.system.model.Customer;
import com.system.model.Driver;
import com.system.model.User;
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
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
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
	private static final Logger logger = Logger.getLogger(BookingController.class.getName());

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
        } else if ("createBooking".equals(action)) {

        	List<Customer> customers = customerService.getAllCustomers();
        	List<Vehicle> vehicles = vehicleService.getAllAvailableVehicles();

            // Create a list of wrapped vehicles with their drivers
            List<VehicleDriverWrapper> availableVehicles = new ArrayList<>();

            for (Vehicle vehicle : vehicles) {
                Driver driver = driverService.getDriverByVehicleId(vehicle.getVehicleId());
                availableVehicles.add(new VehicleDriverWrapper(vehicle, driver));
            }

        	request.setAttribute("customers", customers);
        	request.setAttribute("availableVehicles", availableVehicles);
        	request.getRequestDispatcher("/WEB-INF/views/admin/bookings/addBooking.jsp").forward(request, response);

        } else if ("editBookingDetails".equals(action)) {
            String bookingId = request.getParameter("bookingId");
            Booking booking = bookingService.getBookingById(bookingId);

            if (booking != null) {
                // Get current vehicle ID
                int currentVehicleId = (booking.getAssignedVehicle() != null) ?
                                     booking.getAssignedVehicle().getVehicleId() : -1;

                // Get all vehicles
                List<Vehicle> vehicles = vehicleService.getAllAvailableVehicles();

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

            //Get the booking for future code change
            Booking deleteBooking = bookingService.getBookingById(bookingId);

            // Check and update the Vehicle ID
            if(deleteBooking == null){
                request.setAttribute("errorMessage", "There is no Booking to delete: " + bookingId);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
                return;
            }

             //Get the vehicle
            Vehicle deleteBookingVehicle = deleteBooking.getAssignedVehicle();

            // Get the  Vehicle to delete
            if(deleteBookingVehicle == null){
                 request.setAttribute("errorMessage", "There is no vehicle to remove and reset" + bookingId);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
                return;
            }
            int vehicleId = deleteBookingVehicle.getVehicleId();

            //Handle for booking
            boolean isSuccess = bookingService.deleteBooking(bookingId);

            if (isSuccess) {

               boolean canVehicleStatus = vehicleService.updateVehicleStatus(vehicleId, "Active");

                if(!canVehicleStatus){
                    request.setAttribute("errorMessage", "The booking has been deleted. However, can not update the vehicle: " + bookingId);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                    dispatcher.forward(request, response);
                    return;
                }

                response.sendRedirect("booking?success=Booking details deleted successfully");
            } else {
                request.setAttribute("errorMessage", "Failed to delete booking.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        } else if ("viewCustomerBookings".equals(action)) {
            // Retrieve the user from the session
            HttpSession session = request.getSession(false);
            User user = (session != null) ? (User) session.getAttribute("user") : null;

            if (user == null) {
                // User is not logged in, redirect to login page or display an error message
                response.sendRedirect("login?message=Please login to view your bookings");
                return; // Stop further processing
            }

            // Retrieve customer based on the user's information
            Customer customer = customerService.getCustomerByUserId(user.getId());

            if (customer == null) {
                request.setAttribute("errorMessage", "Customer not found for the logged-in user.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
                return;
            }

            // Retrieve bookings for the customer
            List<Booking> customerBookings = bookingService.getBookingsByCustomerId(customer.getCustomerId());

            // Set the bookings as an attribute in the request
            request.setAttribute("customerBookings", customerBookings);

            // Forward to the customer booking view page
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/customer/viewBookings.jsp");
            dispatcher.forward(request, response);
        }
    }

    // Handle POST requests for creating or updating bookings
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("processCreateBooking".equals(action)) {
            try {
                // Retrieve parameters from the request
                String pickupLocation = request.getParameter("pickupLocation");
                String destination = request.getParameter("destination");
                float distance = Float.parseFloat(request.getParameter("distance")); //No need for get request
                String status = request.getParameter("status");
                int customerId = Integer.parseInt(request.getParameter("customerId"));
                int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));

                // Generate booking ID
                String bookingId = "BK" + UUID.randomUUID().toString().substring(0, 8);

                // Get the Customer, Vehicle, and Driver objects
                Customer customer = customerService.getCustomerById(customerId);
                Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
                Driver driver = driverService.getDriverByVehicleId(vehicleId);

                if (driver == null) {
                    request.setAttribute("error", "No driver available for the vehicle. Please select another vehicle.");
                    request.getRequestDispatcher("/WEB-INF/views/admin/bookings/addBooking.jsp").forward(request, response);
                    return;
                }

                // Create the Booking object
                Booking booking = new Booking(
                        bookingId,
                        LocalDateTime.now(),
                        pickupLocation,
                        destination,
                        distance,
                        status,
                        driver,
                        vehicle,
                        customer
                );

                // Save the booking to the database
                boolean bookingCreated = bookingService.createBooking(booking);

                if (bookingCreated) {
                    // Get vehicle
                    Vehicle bookingVehicle = booking.getAssignedVehicle();

                    if(bookingVehicle == null){
                      request.setAttribute("errorMessage", "Vehicle is null.");
                      request.getRequestDispatcher("/error.jsp").forward(request, response);
                      return;
                    }

                    //Make sure the vehicle is set as booked.
                    boolean vehicleStatusUpdated = vehicleService.updateVehicleStatus(bookingVehicle.getVehicleId(), "Booked");

                    if(!vehicleStatusUpdated){
                      request.setAttribute("errorMessage", "Vehicle can not be updated.");
                      request.getRequestDispatcher("/error.jsp").forward(request, response);
                      return;
                    }

                	 // Get User
                    HttpSession session = request.getSession(false);
                    User user = (session != null) ? (User) session.getAttribute("user") : null;

                    if(user == null){
                        logger.log(Level.SEVERE,"User is null, can't create the bill");
                    }

                	// Generate the Bill after the booking is created
                    boolean billGenerated = billService.generateBill(bookingId, user);

                     if (billGenerated) {
                        response.sendRedirect("booking?success=Booking and Bill created successfully");
                    }
                    else{
                        response.sendRedirect("booking?success=Booking created successfully but Bill not generated.");
                     }

                } else {
                    request.setAttribute("error", "Failed to create booking. Please try again.");
                    // Preserve the valid input values
                    request.setAttribute("pickupLocation", pickupLocation);
                    request.setAttribute("destination", destination);
                    request.setAttribute("status", status);
                    request.setAttribute("customerId", customerId);
                    request.setAttribute("vehicleId", vehicleId);
                    request.getRequestDispatcher("/WEB-INF/views/admin/bookings/addBooking.jsp").forward(request, response);
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error processing booking creation: " + e.getMessage(), e);
                request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/admin/bookings/addBooking.jsp").forward(request, response);
            }
        } else if ("updateStatus".equals(action)) {
            String bookingId = request.getParameter("bookingId");
            String status = request.getParameter("status");

            //Get the booking to the to get assigned.
            Booking updateBooking = bookingService.getBookingById(bookingId);

            // Double check before action
            if(updateBooking == null){
                request.setAttribute("errorMessage", "Booking not found for id: " + bookingId);
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            //Check that there even is a vehicle.
            Vehicle updateBookingVehicle = updateBooking.getAssignedVehicle();

            // Double check before action
            if(updateBookingVehicle == null){
                request.setAttribute("errorMessage", "Vehicle not found for Booking ID, Please do check if the customer or vehicle ID even exists:" + bookingId);
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            boolean isSuccess = bookingService.updateBookingStatus(bookingId, status);
            boolean vehicleUpdateSuccess = false; 

            if (isSuccess) {
                // Update vehicle status based on booking status
                String vehicleStatus = null;
                switch (status) {
                    case "completed":
                        vehicleStatus = "Active";
                        break;
                    case "cancelled":
                        vehicleStatus = "Active";
                        break;
                    case "assigned":
                    case "in-progress":
                    case "pending":
                        vehicleStatus = "Booked";
                        break;
                    default:
                        // Handle cases where no specific vehicle status update is needed
                        vehicleStatus = null; // Do not do anything to the vehicle
                        break;
                }

                if (vehicleStatus != null) {
                     vehicleUpdateSuccess = vehicleService.updateVehicleStatus(updateBookingVehicle.getVehicleId(), vehicleStatus);

                     if(!vehicleUpdateSuccess){
                         logger.log(Level.WARNING, "Failed to update Vehicle status while trying to change.");
                         request.setAttribute("errorMessage", "Successfully changed booking. However, the vehicle did not update");
                         RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                         dispatcher.forward(request, response);
                      }
                }

                response.sendRedirect("booking");  //Redirect, because a booking has been updated to those statuses and nothing more needed
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
            float distance = Float.parseFloat(request.getParameter("distance")); // NO need for get from jsp
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
                   // Get vehicle
                    Vehicle updateBookingVehicle = updatedBooking.getAssignedVehicle();

                    if(updateBookingVehicle == null){
                      request.setAttribute("errorMessage", "Vehicle is null.");
                      request.getRequestDispatcher("/error.jsp").forward(request, response);
                      return;
                    }

                   //Make sure the vehicle is set as booked.
                    boolean vehicleStatusUpdated = vehicleService.updateVehicleStatus(updateBookingVehicle.getVehicleId(), status);

                    if(!vehicleStatusUpdated){
                      request.setAttribute("errorMessage", "Vehicle can not be updated.");
                      request.getRequestDispatcher("/error.jsp").forward(request, response);
                      return;
                    }
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
        } else if ("cancelBooking".equals(action)) {
            cancelBooking(request, response);
        }

    }

    private void cancelBooking(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookingId = request.getParameter("bookingId");

        try {
            // 1. Update Booking Status to "cancelled"
            boolean bookingUpdateSuccess = bookingService.updateBookingStatus(bookingId, "cancelled");

            if (bookingUpdateSuccess) {

                 // 2 Get Booking and vehicle
                Booking canBooking = bookingService.getBookingById(bookingId);

                if(canBooking == null){
                  request.setAttribute("errorMessage", "Can Booking id is not found.");
                  request.getRequestDispatcher("/error.jsp").forward(request, response);
                  return;
                }

                 // 2 Get Booking and vehicle
                Vehicle canBookingVehicle = canBooking.getAssignedVehicle();

                if(canBookingVehicle == null){
                  request.setAttribute("errorMessage", "Vehicle id is not found.");
                  request.getRequestDispatcher("/error.jsp").forward(request, response);
                  return;
                }


                //Update the vehicle back
                boolean canVehicleStatus = vehicleService.updateVehicleStatus(canBookingVehicle.getVehicleId(), "Active");

                if(!canVehicleStatus){
                    request.setAttribute("errorMessage", "Can not update the vehicle back from Booking.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                 }

                // 3.  Get Bill associated with booking
                Bill bill = billService.getBillByBookingId(bookingId);
                String billId = bill.getBillId();

                if (billId != null) {
                    boolean billStatusUpdateSuccess = billService.updateBillStatus(billId, "cancelled");

                    if (billStatusUpdateSuccess) {
                        response.sendRedirect("booking?action=viewCustomerBookings&success=Booking and Bill cancelled successfully");
                    } else {
                        logger.log(Level.WARNING, "Failed to update Bill status for booking ID: " + bookingId);
                        request.setAttribute("errorMessage", "Booking cancelled, but failed to cancel the associated bill. Please contact support.");
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                        dispatcher.forward(request, response);
                    }
                } else {
                    logger.log(Level.WARNING, "No Bill found for booking ID: " + bookingId);
                    response.sendRedirect("booking?success=Booking cancelled, but no bill was found.");
                }
            } else {
                logger.log(Level.WARNING, "Failed to update Booking status for booking ID: " + bookingId);
                request.setAttribute("errorMessage", "Failed to cancel booking.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cancelling booking: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "An unexpected error occurred while cancelling the booking: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}