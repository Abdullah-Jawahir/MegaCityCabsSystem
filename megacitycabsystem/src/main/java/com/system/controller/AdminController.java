package com.system.controller;

import com.system.model.Booking;
import com.system.model.Driver;
import com.system.model.User;
import com.system.service.AdminService;
import com.system.service.BookingService;
import com.system.service.CustomerService;
import com.system.service.DriverService;
import com.system.service.VehicleService;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.system.utils.PasswordHasher;
import com.system.service.UserService;

public class AdminController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private AdminService adminService;
    private CustomerService customerService;
    private BookingService bookingService;
    private VehicleService vehicleService;
    private DriverService driverService;
    private UserService userService;
    private static final Logger logger = Logger.getLogger(AdminController.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();
        adminService = new AdminService();
        customerService = new CustomerService();
        bookingService = new BookingService();
        vehicleService = new VehicleService();
        driverService = new DriverService();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");

        // Add null check for action
        if (action == null) {
            // Load dashboard data
            loadDashboardData(request);
            request.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp").forward(request, response);
            return;
        }
        
        // Get User 
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        switch (action) {
            case "manageCustomers":
                request.getRequestDispatcher("customer?action=manageCustomers").forward(request, response);
                break;
            case "manageBookings":
                request.getRequestDispatcher("booking?action=manageBookings").forward(request, response);
                break;
            case "manageVehicles":
                request.getRequestDispatcher("vehicle?action=manageVehicles").forward(request, response);
                break;
            case "manageDrivers":
                request.getRequestDispatcher("driver?action=manageDrivers").forward(request, response);
                break;
            case "updateBillSettings":
                response.sendRedirect("billSettings");
                break;
            case "viewAdminProfile":
                if (user != null) {
                    // Set the user object as an attribute in the request
                    request.setAttribute("adminUser", user);
                    request.getRequestDispatcher("/WEB-INF/views/admin/adminProfile.jsp").forward(request, response);
                } else {
                    // User is not logged in or session expired, redirect to login page
                    response.sendRedirect("login?message=Please login to view your profile");
                }
                break;
            case "viewUpdateAdminSetting":
                if (user != null) {
                    // Set the user object as an attribute in the request
                    request.setAttribute("adminUser", user);
                    request.getRequestDispatcher("/WEB-INF/views/admin/updateAdminProfile.jsp").forward(request, response);
                } else {
                    // User is not logged in or session expired, redirect to login page
                    response.sendRedirect("login?message=Please login to view your profile");
                }
                break;
            case "viewAssignDrivers":
            	request.getRequestDispatcher("driver?action=viewAssignDrivers").forward(request, response);
                break;
            case "viewSupportPage":
            	request.getRequestDispatcher("/WEB-INF/views/admin/adminSupport.jsp").forward(request, response);
                break;
            case "dashboard":
                // Load dashboard data
                loadDashboardData(request);
                request.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp").forward(request, response);
                break;
            default:
                // Load dashboard data
                loadDashboardData(request);
                request.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp").forward(request, response);
        }
    }

    private void loadDashboardData(HttpServletRequest request) {
        // Get time period from request parameter, default to "last7days"
        String timePeriod = request.getParameter("timePeriod");
        if (timePeriod == null) {
            timePeriod = "last7days";
        }
        
        // Get customer statistics
        int totalCustomers = customerService.getTotalCustomers();
        int previousPeriodCustomers = customerService.getTotalCustomersForPeriod(getPreviousPeriod(timePeriod));
        int currentPeriodCustomers = customerService.getTotalCustomersForPeriod(timePeriod);
        double customerGrowth = calculateGrowthPercentage(previousPeriodCustomers, currentPeriodCustomers);
        
        // Get booking statistics
        int activeBookings = bookingService.getActiveBookings();
        int previousPeriodBookings = bookingService.getTotalBookingsForPeriod(getPreviousPeriod(timePeriod));
        int currentPeriodBookings = bookingService.getTotalBookingsForPeriod(timePeriod);
        double bookingGrowth = calculateGrowthPercentage(previousPeriodBookings, currentPeriodBookings);
        
        // Get vehicle statistics
        int availableVehicles = vehicleService.getAvailableVehiclesCount();
        int newVehicles = vehicleService.getNewVehiclesCount(timePeriod);
        
        // Get revenue statistics
        double monthlyRevenue = bookingService.getTotalRevenueForPeriod(timePeriod);
        double previousPeriodRevenue = bookingService.getTotalRevenueForPeriod(getPreviousPeriod(timePeriod));
        double revenueGrowth = calculateGrowthPercentage(previousPeriodRevenue, monthlyRevenue);
        
        // Get recent bookings
        List<Booking> recentBookings = bookingService.getRecentBookings(5); // Get 5 most recent bookings
        
        // Get top performing drivers
        List<Map<String, Object>> topDrivers = driverService.getTopPerformingDrivers(3); // Get top 3 drivers
        
        // Set attributes for the JSP
        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("customerGrowth", customerGrowth);
        request.setAttribute("activeBookings", activeBookings);
        request.setAttribute("bookingGrowth", bookingGrowth);
        request.setAttribute("availableVehicles", availableVehicles);
        request.setAttribute("newVehicles", newVehicles);
        request.setAttribute("monthlyRevenue", monthlyRevenue);
        request.setAttribute("revenueGrowth", revenueGrowth);
        request.setAttribute("recentBookings", recentBookings);
        request.setAttribute("topDrivers", topDrivers);
        request.setAttribute("selectedTimePeriod", timePeriod);
    }
    
    private String getPreviousPeriod(String currentPeriod) {
        // Logic to determine the previous time period based on the current one
        switch (currentPeriod) {
            case "last7days":
                return "previous7days";
            case "last30days":
                return "previous30days";
            case "thisMonth":
                return "lastMonth";
            case "thisYear":
                return "lastYear";
            default:
                return "previous7days";
        }
    }
    
    private double calculateGrowthPercentage(double previous, double current) {
        if (previous == 0) {
            return 100.0; // If previous was 0, assume 100% growth
        }

        double growthPercentage = ((current - previous) / previous) * 100.0;

        // Round the result to 2 decimal places, except for when it's exactly 100.0
        if (growthPercentage != 100.0) {
            DecimalFormat df = new DecimalFormat("#.00");
            return Double.parseDouble(df.format(growthPercentage));
        }

        return growthPercentage;
    }
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	
    	HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
    	
        String action = request.getParameter("action");

       switch (action) {
           case "updateAdminProfile":
            try {
                

                if (user == null) {
                    response.sendRedirect("login?message=Please login to update profile");
                    return;
                }

                // Get parameters from the request
                String name = request.getParameter("name");
                String username = request.getParameter("username");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String newPassword = request.getParameter("newPassword");
                String confirmPassword = request.getParameter("confirmPassword");

                //Validate the data before
                if (name == null || name.trim().isEmpty()) {
                        request.setAttribute("errorMessage", "Name is required.");
                        request.setAttribute("adminUser", user);
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin/updateAdminProfile.jsp");
                        dispatcher.forward(request, response);
                        return;
                }

                //Hash to ensure it cannot be hacked so new has been created
                if (username == null || username.trim().isEmpty()) {
                        request.setAttribute("errorMessage", "Username is required.");
                        request.setAttribute("adminUser", user);
                         RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin/updateAdminProfile.jsp");
                        dispatcher.forward(request, response);
                         return;
                }

                //Hash also for email
                if (email == null || email.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "Email is required.");
                    request.setAttribute("adminUser", user);
                     RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin/updateAdminProfile.jsp");
                    dispatcher.forward(request, response);
                    return;
                }

                // Validate the numbers
                if (phone == null || phone.trim().isEmpty()) {
                     request.setAttribute("errorMessage", "Phone is required.");
                     request.setAttribute("adminUser", user);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin/updateAdminProfile.jsp");
                     dispatcher.forward(request, response);
                    return;
                 }

                //Double check
                if ((newPassword != null && !newPassword.trim().isEmpty()) ||
                       (confirmPassword != null && !confirmPassword.trim().isEmpty())) {
                   if (!newPassword.equals(confirmPassword)) {
                       request.setAttribute("errorMessage", "New password and confirm password do not match.");
                      request.setAttribute("adminUser", user);
                       RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin/updateAdminProfile.jsp");
                      dispatcher.forward(request, response);
                      return;
                   }
               }

               //Update the user object and call the service to update
                user.setName(name);
                user.setUsername(username);
                user.setEmail(email);
                user.setPhone(phone);

               //Hash and set the new password if provided
                if (newPassword != null && !newPassword.trim().isEmpty()) {
                   String hashedPassword = PasswordHasher.hash(newPassword);
                   user.setPassword(hashedPassword);
              }
               
                //Now, with good and safety we do the good old services
                boolean updateSuccess = userService.updateUser(user);

              // With is we can go to the part that it is what we want with 100x times more safety than before
              if (updateSuccess) {
                session.setAttribute("user", user); // Update session with new user data
                  response.sendRedirect("admin?action=viewAdminProfile&message=Profile updated successfully");
                } else {
                    request.setAttribute("errorMessage", "Failed to update profile. Please try again.");
                   request.setAttribute("adminUser", user);
                   RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin/updateAdminProfile.jsp");
                    dispatcher.forward(request, response);
                }
        //Catch errors that may come.
         } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating admin profile: " + e.getMessage(), e);
              request.setAttribute("errorMessage", "An error occurred while updating your profile. Please try again.");
            request.setAttribute("adminUser", user);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin/updateAdminProfile.jsp");
            dispatcher.forward(request, response);
        }
            // Break is used!
            break;
            
            case "createAdmin":
                request.getRequestDispatcher("user?action=createAdmin").forward(request, response);
                break;
                
            case "registerCustomer":
                request.getRequestDispatcher("customer?action=registerCustomer").forward(request, response);
                break;
            case "createBooking":
                request.getRequestDispatcher("booking?action=createBooking").forward(request, response);
                break;
            case "createVehicle":
                request.getRequestDispatcher("vehicle?action=createVehicle").forward(request, response);
                break;
            case "deleteDriver":
                request.getRequestDispatcher("driver?action=deleteDriver").forward(request, response);
                break;
            case "deleteCustomer":
                request.getRequestDispatcher("user?action=deleteCustomer").forward(request, response);
                break;
            case "deleteVehicle":
                request.getRequestDispatcher("vehicle?action=deleteVehicle").forward(request, response);
                break;
            case "deleteBooking":
                request.getRequestDispatcher("booking?action=deleteBooking").forward(request, response);
                break;
            case "updateDashboardPeriod":
                // 1. Retrieve the timePeriod parameter from the request
                String timePeriod = request.getParameter("timePeriod");

                // 2. Set the timePeriod as an attribute in the request
                request.setAttribute("selectedTimePeriod", timePeriod);

                // 3. Reload the dashboard with the new period
                loadDashboardData(request); // Load data with the updated timePeriod attribute
                request.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp").forward(request, response);
                break;
          default:
               request.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp").forward(request, response);
       }

    }

   
}