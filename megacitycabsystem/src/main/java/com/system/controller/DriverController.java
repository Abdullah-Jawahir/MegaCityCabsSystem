package com.system.controller;


import com.system.model.Customer;
import com.system.model.Driver;
import com.system.model.User;
import com.system.service.DriverService;
import com.system.service.RegistrationService;
import com.system.service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class DriverController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DriverService driverService;
    private UserService userService;
    private RegistrationService registrationService;
    

    @Override
    public void init() throws ServletException {
        driverService = new DriverService();
        userService = new UserService();
        registrationService = new RegistrationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("manageDrivers")) {
                List<Driver> drivers = driverService.getAllDrivers();
                request.setAttribute("drivers", drivers);
                request.getRequestDispatcher("/WEB-INF/views/admin/manageDrivers.jsp").forward(request, response);
            } else if (action.equals("editDriver")) {
                int driverId = Integer.parseInt(request.getParameter("driverId"));
                Driver driver = driverService.getDriverById(driverId);

                if (driver != null) {
                    request.setAttribute("driver", driver);
                    request.getRequestDispatcher("/WEB-INF/views/admin/drivers/editDriver.jsp").forward(request, response);
                } else {
                    response.sendRedirect("driver?error=Driver not found");
                }
            } else if (action.equals("deleteDriver")) {
                try {
                    int driverId = Integer.parseInt(request.getParameter("driverId"));
                    Driver driver = driverService.getDriverById(driverId);
                    
                    if (driver != null) {
                        // Soft delete: update the driver's status to "Inactive"
                        boolean statusUpdated = driverService.updateDriverStatus(driverId, "Inactive");
                        
                        if (statusUpdated) {
                            response.sendRedirect("driver?success=Driver marked as Inactive successfully");
                        } else {
                            response.sendRedirect("driver?error=Unable to mark driver as Inactive");
                        }
                    } else {
                        response.sendRedirect("driver?error=Driver not found");
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect("driver?error=Invalid driver ID");
                } catch (Exception e) {
                    response.sendRedirect("driver?error=Error deleting driver: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            response.sendRedirect("driver?error=" + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action.equals("registerDriver")) {
            try {
                // User Data
                String name = request.getParameter("name");
                String password = request.getParameter("password");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");

                // Driver-specific Data
                String licenseNumber = request.getParameter("licenseNumber");
                // Set status to "Available" to match the enum in database
                String status = "Available";
                
                System.out.println("DEBUG: Starting driver registration process");
                System.out.println("DEBUG: Received parameters - name: " + name + ", email: " + email + 
                                 ", phone: " + phone + ", licenseNumber: " + licenseNumber);

                if (name == null || password == null || email == null || phone == null || 
                    licenseNumber == null) {
                    request.setAttribute("error", "All fields are required.");
                    request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                    return;
                }
                
                System.out.println("DEBUG: Creating User object");
                User user;
                
                try {
                    user = new User(name, password, "driver", email, phone, LocalDateTime.now());
                } catch (IllegalArgumentException e) {
                    // Catch specific validation errors (like phone number format)
                    request.setAttribute("error", e.getMessage());
                    // Preserve the valid input values so user doesn't have to retype everything
                    request.setAttribute("name", name);
                    request.setAttribute("email", email);
                    request.setAttribute("licenseNumber", licenseNumber);
                    request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                    return;
                }
                
                System.out.println("DEBUG: Calling userService.addUser");
                boolean registrationSuccess = false;
                
                try {
                	registrationSuccess = registrationService.registerDriver(user, licenseNumber, status);
                    System.out.println("DEBUG: userService.addUser returned: " + registrationSuccess);
                } catch (Exception e) {
                    System.out.println("DEBUG: Exception in userService.addUser: " + e.getMessage());
                    request.setAttribute("error", "Error creating account. Please try again.");
                    // Preserve the valid input values
                    request.setAttribute("name", name);
                    request.setAttribute("email", email);
                    request.setAttribute("licenseNumber", licenseNumber);
                    request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                    return;
                }
                
                if (registrationSuccess) {
                    request.setAttribute("success", "Registration successful! Please login to continue.");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Failed to create driver account. Please try again.");
                    request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                }
                
            } catch (Exception e) {
                System.out.println("DEBUG: Top-level exception in registration: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "An unexpected error occurred. Please try again.");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            }
        } else if (action.equals("updateDriver")) {
            // Handle updating an existing driver
            int driverId = Integer.parseInt(request.getParameter("driverId"));
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String licenseNumber = request.getParameter("licenseNumber");
            String status = request.getParameter("status");

            // Fetch the existing Driver object to retain the User reference
            Driver existingDriver = driverService.getDriverById(driverId);
            if (existingDriver == null) {
                request.setAttribute("error", "Driver not found");
                request.getRequestDispatcher("/WEB-INF/views/admin/editDriver.jsp").forward(request, response);
                return;
            }
            
            User user = existingDriver.getUser();
            
            if (user != null) {
            	// Update the necessary common fields for user
            	user.setName(name);
            	user.setPhone(phone);
                user.setEmail(email);
                boolean isUserUpdated = userService.updateUser(user);
                
                // Update the necessary fields for driver
                existingDriver.setLicenseNumber(licenseNumber);
                existingDriver.setStatusFromString(status);

                boolean isDriverUpdated = driverService.updateDriver(existingDriver);
                
                if (isUserUpdated && isDriverUpdated) {
                    response.sendRedirect("driver"); 
                } else {
                    request.setAttribute("error", "Error while updating the driver");
                    request.getRequestDispatcher("/WEB-INF/views/admin/editDriver.jsp").forward(request, response);
                }
                
            }
            
        }
    }

}
