package com.system.controller;


import com.system.model.Customer;
import com.system.model.Driver;
import com.system.model.User;
import com.system.model.Vehicle;
import com.system.model.VehicleDriverWrapper;
import com.system.service.DriverService;
import com.system.service.RegistrationService;
import com.system.service.UserService;
import com.system.service.VehicleService;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DriverController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DriverService driverService;
    private UserService userService;
    private RegistrationService registrationService;
    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        driverService = new DriverService();
        userService = new UserService();
        registrationService = new RegistrationService();
        vehicleService = new VehicleService();
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
                    String errorMessage = "Driver not found";
                    response.sendRedirect("driver?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
                }
                
            } else if (action.equals("viewAssignDrivers")) {
                List<Driver> availableDrivers = driverService.getAvailableDrivers();
                List<Vehicle> availableVehicles = vehicleService.getActiveVehiclesWithoutDriver();

                // Fetch ONLY the vehicles that HAVE an assigned driver
                List<Vehicle> assignedVehicles = new ArrayList<>();
                List<Vehicle> allVehicles = vehicleService.getAllVehicles();

                for (Vehicle vehicle : allVehicles) {
                    if (vehicle.getDriverId() != null) {
                        assignedVehicles.add(vehicle);
                    }
                }

                List<VehicleDriverWrapper> assignedDriversWrappers = new ArrayList<>();
                for (Vehicle vehicle : assignedVehicles) {
                    Driver assignedDriver = driverService.getDriverByVehicleId(vehicle.getVehicleId());
                    assignedDriversWrappers.add(new VehicleDriverWrapper(vehicle, assignedDriver));
                }

                request.setAttribute("availableDrivers", availableDrivers);
                request.setAttribute("availableVehicles", availableVehicles);
                request.setAttribute("assignedDrivers", assignedDriversWrappers); // Set to the WRAPPER list

                request.getRequestDispatcher("/WEB-INF/views/admin/drivers/assignDrivers.jsp").forward(request, response);
                
            } else if (action.equals("unassignDriver")) { // New Action
                try {
                    int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));

                    // Get the vehicle object to retrieve the driver ID and status before unassignment
                    Vehicle vehicle = vehicleService.getVehicleById(vehicleId);

                    if (vehicle == null) {
                        String errorMessage = "Vehicle not found.";
                        response.sendRedirect("driver?action=viewAssignDrivers&error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
                        return;
                    }

                    // Check if the vehicle is currently booked
                    if ("Booked".equalsIgnoreCase(vehicle.getStatus())) {
                        String errorMessage = "Cannot unassign driver from vehicle that is currently Booked.";
                        response.sendRedirect("driver?action=viewAssignDrivers&error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
                        return;
                    }

                    Integer driverId = vehicle.getDriverId();  // Get the driver ID from the vehicle

                    // Unassign the driver from the vehicle
                    boolean isUnassigned = vehicleService.unassignDriverFromVehicle(vehicleId);

                    if (isUnassigned) {
                        // Set driver status to 'Available' IF there was a driver assigned
                        if (driverId != null) {
                            boolean isStatusUpdated = driverService.updateDriverStatus(driverId, "Available");

                            if (isStatusUpdated) {
                                String successMessage = "Driver unassigned successfully and status updated.";
                                response.sendRedirect("driver?action=viewAssignDrivers&success=" + URLEncoder.encode(successMessage, StandardCharsets.UTF_8.toString()));
                            } else {
                                String warningMessage = "Driver unassigned, but failed to update driver status.";
                                response.sendRedirect("driver?action=viewAssignDrivers&warning=" + URLEncoder.encode(warningMessage, StandardCharsets.UTF_8.toString()));
                            }
                        } else {
                            String successMessage = "Vehicle was already unassigned.";
                            response.sendRedirect("driver?action=viewAssignDrivers&success=" + URLEncoder.encode(successMessage, StandardCharsets.UTF_8.toString()));
                        }

                    } else {
                        String errorMessage = "Failed to unassign driver.";
                        response.sendRedirect("driver?action=viewAssignDrivers&error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
                    }
                } catch (NumberFormatException e) {
                    String errorMessage = "Invalid vehicle ID.";
                    response.sendRedirect("driver?action=viewAssignDrivers&error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
                } catch (Exception e) {
                    String errorMessage = "Error unassigning driver: " + e.getMessage();
                    response.sendRedirect("driver?action=viewAssignDrivers&error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
                }
                
            } else if (action.equals("deleteDriver")) {
                try {
                    int driverId = Integer.parseInt(request.getParameter("driverId"));
                    Driver driver = driverService.getDriverById(driverId);

                    if (driver != null) {
                        // Soft delete: update the driver's status to "Inactive"
                        boolean statusUpdated = driverService.updateDriverStatus(driverId, "Inactive");

                        if (statusUpdated) {
                            String successMessage = "Driver marked as Inactive successfully";
                            response.sendRedirect("driver?success=" + URLEncoder.encode(successMessage, StandardCharsets.UTF_8.toString()));
                        } else {
                            String errorMessage = "Unable to mark driver as Inactive";
                            response.sendRedirect("driver?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
                        }
                    } else {
                        String errorMessage = "Driver not found";
                        response.sendRedirect("driver?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
                    }
                } catch (NumberFormatException e) {
                    String errorMessage = "Invalid driver ID";
                    response.sendRedirect("driver?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
                } catch (Exception e) {
                    String errorMessage = "Error deleting driver: " + e.getMessage();
                    response.sendRedirect("driver?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
                }
            }

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            response.sendRedirect("driver?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action.equals("registerDriver")) {
            try {
                // User Data
                String name = request.getParameter("name");
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");

                // Driver-specific Data
                String licenseNumber = request.getParameter("licenseNumber");
                // Set status to "Available" to match the enum in database
                String status = "Available";

                System.out.println("DEBUG: Starting driver registration process");
                System.out.println("DEBUG: Received parameters - name: " + name + ", username: " + username + ", email: " + email +
                                 ", phone: " + phone + ", licenseNumber: " + licenseNumber);

                if (name == null || username == null || password == null || email == null || phone == null ||
                    licenseNumber == null) {
                    request.setAttribute("error", "All fields are required.");
                    request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                    return;
                }

                System.out.println("DEBUG: Creating User object");
                User user;

                try {
                    user = new User(name, username, password, "driver", email, phone, LocalDateTime.now());
                } catch (IllegalArgumentException e) {
                    // Catch specific validation errors (like phone number format)
                    request.setAttribute("error", e.getMessage());
                    // Preserve the valid input values so user doesn't have to retype everything
                    request.setAttribute("name", name);
                    request.setAttribute("username", username);
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
                    request.setAttribute("username", username);
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
            String username = request.getParameter("username");
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
                user.setUsername(username);
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

        } else if (action.equals("assignDriverToVehicle")) {
            try {
                int driverId = Integer.parseInt(request.getParameter("driverId"));
                int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));

                Driver driver = driverService.getDriverById(driverId);
                Vehicle vehicle = vehicleService.getVehicleById(vehicleId);

                if (driver == null || vehicle == null) {
                    request.setAttribute("error", "Invalid driver or vehicle selection.");
                    forwardToAssignDriversPage(request, response);
                    return;
                }

                // Assign the driver to the vehicle
                boolean isAssigned = vehicleService.assignDriverToVehicle(driverId, vehicleId);

                if (isAssigned) {
                    // Set driver status to 'Assigned'
                    boolean isStatusUpdated = driverService.updateDriverStatus(driverId, "Assigned");

                    if (isStatusUpdated) {
                        request.setAttribute("success", "Driver assigned successfully and status updated.");
                    } else {
                        request.setAttribute("warning", "Driver assigned, but failed to update driver status.");
                    }
                } else {
                    request.setAttribute("error", "Failed to assign driver.");
                }

                // Reload the assign drivers page with updated data
                forwardToAssignDriversPage(request, response);

            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid driver or vehicle ID format.");
                forwardToAssignDriversPage(request, response);
            } catch (Exception e) {
                request.setAttribute("error", "Error assigning driver: " + e.getMessage());
                forwardToAssignDriversPage(request, response);
            }
        }
    }

    private void forwardToAssignDriversPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch available drivers and vehicles (without assigned drivers)
        List<Driver> availableDrivers = driverService.getAvailableDrivers();
        List<Vehicle> unassignedVehicles = vehicleService.getActiveVehiclesWithoutDriver();

        // Fetch ONLY the vehicles that HAVE an assigned driver
        List<Vehicle> assignedVehicles = new ArrayList<>(); // List to store ONLY assigned vehicles
        List<Vehicle> allVehicles = vehicleService.getAllVehicles(); // Get all vehicles

        for (Vehicle vehicle : allVehicles) {
            if (vehicle.getDriverId() != null) {  // Check if the vehicle has a driver assigned
                assignedVehicles.add(vehicle); // Add to the assignedVehicles list
            }
        }

        List<VehicleDriverWrapper> assignedDriversWrappers = new ArrayList<>(); // List to store wrapper objects
        for (Vehicle vehicle : assignedVehicles) {
            Driver assignedDriver = driverService.getDriverByVehicleId(vehicle.getVehicleId());
            assignedDriversWrappers.add(new VehicleDriverWrapper(vehicle, assignedDriver));
        }

        // Set attributes for JSP
        request.setAttribute("availableDrivers", availableDrivers);
        request.setAttribute("availableVehicles", unassignedVehicles);
        request.setAttribute("assignedDrivers", assignedDriversWrappers); // Set to the WRAPPER list

        // Forward to the JSP page
        request.getRequestDispatcher("/WEB-INF/views/admin/drivers/assignDrivers.jsp").forward(request, response);
    }


}