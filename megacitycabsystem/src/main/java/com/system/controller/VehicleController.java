package com.system.controller;

import com.system.model.Driver;
import com.system.model.Vehicle;
import com.system.service.DriverService;
import com.system.service.VehicleService;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class VehicleController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private VehicleService vehicleService;
    private DriverService driverService;

    @Override
    public void init() throws ServletException {
        super.init();
        vehicleService = new VehicleService();
        driverService = new DriverService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("manageVehicles".equals(action)) {
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            List<Driver> allDrivers = driverService.getAllDrivers();
            List<Driver> availableDrivers = driverService.getAvailableDrivers(); // Fetch available drivers

            request.setAttribute("vehicles", vehicles);
            request.setAttribute("allDrivers", allDrivers);
            request.setAttribute("availableDrivers", availableDrivers); // Pass available drivers to JSP

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin/manageVehicles.jsp");
            dispatcher.forward(request, response);
        } else if ("editVehicle".equals(action)) {
            try {
                int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
                Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
                
                List<Driver> availableDrivers = driverService.getAvailableDrivers();
                
                // If the vehicle already has an assigned driver and they're not in the available drivers list, add them.
                if (vehicle.getDriverId() > 0) {
                    boolean exists = availableDrivers.stream()
                                        .anyMatch(d -> d.getDriverId() == vehicle.getDriverId());
                    if (!exists) {
                        // Optionally, fetch the driver details separately if not in availableDrivers.
                        Driver assignedDriver = driverService.getDriverById(vehicle.getDriverId());
                        if (assignedDriver != null) {
                            availableDrivers.add(assignedDriver);
                        }
                    }
                }
                
                request.setAttribute("vehicle", vehicle);
                
                request.setAttribute("availableDrivers", availableDrivers);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin/vehicles/editVehicle.jsp");
                dispatcher.forward(request, response);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid vehicle ID.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        } else if ("deleteVehicle".equals(action)) {
            try {
                int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
                // Retrieve the existing vehicle to update the driver's status if needed
                Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
                
                // Instead of hard deleting, perform a soft delete by updating status to 'Retired'
                boolean isSuccess = vehicleService.updateVehicleStatus(vehicleId, "Retired");
                if (isSuccess) {
                    // If the vehicle had a driver assigned, update their status to 'Available'
                    if (vehicle != null && vehicle.getDriverId() > 0) {
                        driverService.updateDriverStatus(vehicle.getDriverId(), "Available");
                    }
                    response.sendRedirect("vehicle?action=manageVehicles");
                } else {
                    request.setAttribute("errorMessage", "Failed to retire vehicle.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                    dispatcher.forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid vehicle ID.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("createVehicle".equals(action)) {
            String plateNumber = request.getParameter("plateNumber");
            String model = request.getParameter("model");
            String status = request.getParameter("status");
            String ratePerKmStr = request.getParameter("ratePerKm");

            try {
                int driverId = request.getParameter("driverId") != null && !request.getParameter("driverId").isEmpty()
                        ? Integer.parseInt(request.getParameter("driverId"))
                        : 0;
                
                // Parse the ratePerKm String to float, handling potential NumberFormatExceptions
                float ratePerKm = (ratePerKmStr != null && !ratePerKmStr.isEmpty()) ? Float.parseFloat(ratePerKmStr) : 5.0f;

                Vehicle vehicle = new Vehicle(plateNumber, model, status, driverId, LocalDateTime.now(), ratePerKm);
                boolean isSuccess = vehicleService.createVehicle(vehicle);

                if (isSuccess) {
                    if (driverId > 0) {
                        boolean isDriverUpdated = driverService.updateDriverStatus(driverId, "Busy");
                        if (!isDriverUpdated) {
                            request.setAttribute("errorMessage", "Failed to update driver status.");
                            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                            dispatcher.forward(request, response);
                            return;
                        }
                    }
                    response.sendRedirect("vehicle?action=manageVehicles");
                } else {
                    request.setAttribute("errorMessage", "Failed to create vehicle.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                    dispatcher.forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid input data.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        } else if ("updateVehicle".equals(action)) {
            try {
                int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
                String plateNumber = request.getParameter("plateNumber");
                String model = request.getParameter("model");
                String status = request.getParameter("status");
                
             // Safely parse driverId
                int newDriverId = 0;
                String driverIdParam = request.getParameter("driverId");
                if (driverIdParam != null && !driverIdParam.isEmpty()) {
                    newDriverId = Integer.parseInt(driverIdParam);
                }

                // Safely parse ratePerKm
                float ratePerKm = 5.0f; // Default value
                String ratePerKmParam = request.getParameter("ratePerKm");
                if (ratePerKmParam != null && !ratePerKmParam.isEmpty()) {
                    ratePerKm = Float.parseFloat(ratePerKmParam);
                }
                
                Vehicle existingVehicle = vehicleService.getVehicleById(vehicleId);
                int previousDriverId = existingVehicle.getDriverId();
                
                boolean isSuccess = vehicleService.updateVehicle(vehicleId, plateNumber, model, status, newDriverId, ratePerKm);
                if (isSuccess) {
                    if (previousDriverId != newDriverId) {
                        if (previousDriverId > 0) {
                            boolean updatedOldDriver = driverService.updateDriverStatus(previousDriverId, "Available");
                            if (!updatedOldDriver) {
                                request.setAttribute("errorMessage", "Failed to update the status of the previous driver.");
                                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                                dispatcher.forward(request, response);
                                return;
                            }
                        }
                        if (newDriverId > 0) {
                            boolean updatedNewDriver = driverService.updateDriverStatus(newDriverId, "Busy");
                            if (!updatedNewDriver) {
                                request.setAttribute("errorMessage", "Failed to update the status of the new driver.");
                                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                                dispatcher.forward(request, response);
                                return;
                            }
                        }
                    }
                    response.sendRedirect("vehicle?action=manageVehicles");
                } else {
                    request.setAttribute("errorMessage", "Failed to update vehicle.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                    dispatcher.forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid input data.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
            }
        }
    }
}
