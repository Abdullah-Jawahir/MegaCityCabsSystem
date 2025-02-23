package com.system.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class RegisterController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward the request to register.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/register.jsp");
        dispatcher.forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get the action parameter to determine which registration to process
        String action = request.getParameter("action");
        
        if (action == null) {
            // No action provided; redirect back to register.jsp
            response.sendRedirect(request.getContextPath() + "/register.jsp");
        } else if (action.equals("registerCustomer")) {
            registerCustomer(request, response);
        } else if (action.equals("registerDriver")) {
            registerDriver(request, response);
        } else {
            // For unrecognized actions, redirect back to the registration page
            response.sendRedirect(request.getContextPath() + "/register.jsp");
        }
    }
    
    // Dispatches the request to the Customer Controller
    private void registerCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to the URL mapped to the Customer Controller
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer?action=registerCustomer");
        dispatcher.forward(request, response);
    }
    
    // Dispatches the request to the Driver Controller
    private void registerDriver(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to the URL mapped to the Driver Controller
        RequestDispatcher dispatcher = request.getRequestDispatcher("/driver?action=registerDriver");
        dispatcher.forward(request, response);
    }
}
