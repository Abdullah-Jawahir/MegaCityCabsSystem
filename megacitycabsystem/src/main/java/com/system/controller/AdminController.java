package com.system.controller;

import com.system.service.AdminService;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class AdminController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private AdminService adminService;

    @Override
    public void init() throws ServletException {
        super.init();
        adminService = new AdminService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");

        // Add null check for action
        if (action == null) {
            request.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp").forward(request, response);
            return;
        }

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
            default:
                request.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
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
            default:
                request.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp").forward(request, response);
        }
    }
}