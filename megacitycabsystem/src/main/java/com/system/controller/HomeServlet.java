package com.system.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.system.model.Vehicle;
import com.system.service.VehicleService;


public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private VehicleService vehicleService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        vehicleService = new VehicleService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        List<Vehicle> availableVehicles = vehicleService.getAllAvailableVehicles();
        
        request.setAttribute("availableVehicles", availableVehicles); 

        // Forward the request to index.jsp
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}