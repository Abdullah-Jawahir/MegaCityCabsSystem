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


public class AuthenticationController extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserService userService;

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
            // Successful login: Store user session and redirect based on user role
            HttpSession session = request.getSession();
            session.setAttribute("user", user); // Store user in session
            
            // Forward user data to index.jsp
            request.setAttribute("user", user); // Send user data to JSP
            
            // Check if there was a pending booking
            String pendingPickupLocation = (String) session.getAttribute("pendingPickupLocation");
            String pendingDropLocation = (String) session.getAttribute("pendingDropLocation");
            String pendingDistance = (String) session.getAttribute("pendingDistance");  // Retrieve pending distance
            
            if (pendingPickupLocation != null && pendingDropLocation != null && pendingDistance != null) {
                // Clear the pending booking from session
                session.removeAttribute("pendingPickupLocation");
                session.removeAttribute("pendingDropLocation");
                session.removeAttribute("pendingDistance");
                
                // Redirect to booking confirmation with distance
                response.sendRedirect("processBooking?pickupLocation=" + 
                    URLEncoder.encode(pendingPickupLocation, "UTF-8") + 
                    "&dropLocation=" + URLEncoder.encode(pendingDropLocation, "UTF-8") +
                    "&distance=" + URLEncoder.encode(pendingDistance, "UTF-8"));  // Add distance to URL
                return;
            }
            
            // Redirect based on the user role
            if (user.getRole().equals("admin")) {
                response.sendRedirect("admin");
            } else if (user.getRole().equals("driver") || user.getRole().equals("customer")) {
            	response.sendRedirect("home");
            }
        } else {
            // Authentication failed: Set error message in the session and redirect to login page
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Invalid username or password."); //changed
            response.sendRedirect("login");
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