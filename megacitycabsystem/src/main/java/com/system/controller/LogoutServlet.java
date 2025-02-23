package com.system.controller;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invalidate the user session to log them out
        HttpSession session = request.getSession(false); // Get the session without creating a new one if not already created
        if (session != null) {
            session.invalidate(); // Invalidate the session, effectively logging out the user
        }

        // Redirect to the login page or home page after logout
        response.sendRedirect("login"); // You can change "login" to any page you wish to redirect to
    }
}