package com.system.controller;

import com.system.model.Booking;
import com.system.model.Driver;
import com.system.service.AdminService;
import com.system.service.BookingService;
import com.system.service.CustomerService;
import com.system.service.DriverService;
import com.system.service.VehicleService;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class AdminController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private AdminService adminService;
    private CustomerService customerService;
    private BookingService bookingService;
    private VehicleService vehicleService;
    private DriverService driverService;

    @Override
    public void init() throws ServletException {
        super.init();
        adminService = new AdminService();
        customerService = new CustomerService();
        bookingService = new BookingService();
        vehicleService = new VehicleService();
        driverService = new DriverService();
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
        return ((current - previous) / previous) * 100.0;
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
            case "updateDashboardPeriod":
                // Just reload the dashboard with the new period
                request.getRequestDispatcher("admin?action=dashboard").forward(request, response);
                break;
            default:
                request.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp").forward(request, response);
        }
    }
}