package com.system.controller;

import com.google.gson.Gson;
import com.system.model.Vehicle;
import com.system.service.VehicleService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class AvailableVehiclesController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        vehicleService = new VehicleService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String distanceStr = request.getParameter("distance");

            // Validate distance
            float distance;
            try {
                distance = Float.parseFloat(distanceStr);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid distance value.");
                return;
            }

            // Adapt logic to handle the existing getAvailableVehicles method
            List<Vehicle> availableVehicles = new ArrayList<>(); // Create a list to hold the vehicle
            Vehicle availableVehicle = vehicleService.getAvailableVehicles();

            if (availableVehicle != null) {
                availableVehicles.add(availableVehicle);
            }

            // Convert list to JSON
            Gson gson = new Gson();
            String json = gson.toJson(availableVehicles);

            // Set response headers
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Send JSON response
            response.getWriter().write(json);

        } catch (Exception e) {
            // Log the exception
            e.printStackTrace(); // Or use a logger like log4j

            // Create a JSON error response
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Important: Set error status
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String errorMessage = "{ \"error\": \"" + e.getMessage() + "\" }";  // Create JSON object
            response.getWriter().write(errorMessage); // Send error message as JSON
        }
    }
}