package com.system.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SupportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set the content type to HTML
        response.setContentType("text/html");

        // Forward the request to the support.jsp page
        request.getRequestDispatcher("/WEB-INF/views/customer/support.jsp").forward(request, response);
    }
}