package com.system.controller;

import com.system.service.UserService;
import com.system.model.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AuthenticationController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserService userService;
    private static final Logger logger = Logger.getLogger(AuthenticationController.class.getName());

    @Override
    public void init() throws ServletException {
        userService = new UserService(); // Initialize the UserService to interact with user data
    }

    // Handle GET requests for displaying the login page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    // Handle POST requests for processing the login
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username"); // Get username instead of email
        String password = request.getParameter("password");

        User user = userService.authenticateUser(username, password); // Authenticate user using service layer

        if (user != null) {
            // Successful login: Store user session
            HttpSession session = request.getSession();
            session.setAttribute("user", user); // Store user in session

            // Redirect based on the user role
            if (user.getRole().equals("admin")) {
                response.sendRedirect("admin");
            } else if (user.getRole().equals("driver") || user.getRole().equals("customer")) {
            	 handlePendingBooking(request, response, session);
            }
        } else {
            // Authentication failed: Set error message in the session and redirect to login page
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Invalid username or password."); //changed
            response.sendRedirect("login");
        }
    }

    private void handlePendingBooking(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        // Check if there was a pending booking
        String pendingPickupLocation = (String) session.getAttribute("pendingPickupLocation");
        String pendingDropLocation = (String) session.getAttribute("pendingDropLocation");
        String pendingDistance = (String) session.getAttribute("pendingDistance");
        Object pendingSelectedVehicleIdObj = session.getAttribute("pendingSelectedVehicleId"); // Get as Object

        // Check if all pending attributes are present
        if (pendingPickupLocation != null && pendingDropLocation != null && pendingDistance != null && pendingSelectedVehicleIdObj != null) {
            try {
                if(pendingSelectedVehicleIdObj == null || pendingSelectedVehicleIdObj.toString().isEmpty()){
                logger.log(Level.SEVERE, "pendingSelectedVehicleIdObj is null, cannot continue process");
                response.sendRedirect("home?message=" + URLEncoder.encode("An error occurred while processing your booking. Please try again.", "UTF-8"));
                return;
                }

                // Convert pendingSelectedVehicleId to int
                int pendingSelectedVehicleId = Integer.parseInt(pendingSelectedVehicleIdObj.toString());

                // Clear the pending booking attributes from session
                session.removeAttribute("pendingPickupLocation");
                session.removeAttribute("pendingDropLocation");
                session.removeAttribute("pendingDistance");
                session.removeAttribute("pendingSelectedVehicleId");

                // Redirect to booking confirmation with parameters
                String redirectURL = "processBooking?pickupLocation=" + URLEncoder.encode(pendingPickupLocation, "UTF-8")
                        + "&dropLocation=" + URLEncoder.encode(pendingDropLocation, "UTF-8")
                        + "&distance=" + URLEncoder.encode(pendingDistance, "UTF-8")
                        + "&selectedVehicleId=" + pendingSelectedVehicleId;

                response.sendRedirect(redirectURL);
            } catch (NumberFormatException e) {
                // Handle the case where pendingSelectedVehicleId is not a valid integer
                logger.log(Level.SEVERE, "Error converting pendingSelectedVehicleId to integer", e);
                // Redirect to a safe page with an error message or the home page
                response.sendRedirect("home?message=" + URLEncoder.encode("An error occurred while processing your booking. Please try again.", "UTF-8"));
            }
        } else {
            // If any of the pending attributes are missing, redirect to home
            response.sendRedirect("home");
        }
    }

    // Logout functionality
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate(); // Invalidate the session to log out the user
        response.sendRedirect("/login"); // Redirect to login page after logout
    }
}