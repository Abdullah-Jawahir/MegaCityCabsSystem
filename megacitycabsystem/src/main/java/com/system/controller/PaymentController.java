package com.system.controller;

import com.system.model.*;
import com.system.service.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PaymentController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookingService bookingService;
    private BillService billService;

    @Override
    public void init() throws ServletException {
        super.init();
        bookingService = new BookingService();
        billService = new BillService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get the bookingId and billId from the request parameters
            String bookingId = request.getParameter("bookingId");
            String billId = request.getParameter("billId");

            // Fetch the booking and bill objects from the services
            Booking booking = bookingService.getBookingById(bookingId);
            Bill bill = billService.getBillById(billId);

            // If either the booking or bill is not found, show an error
            if (booking == null || bill == null) {
                request.setAttribute("errorMessage", "Invalid booking or bill ID.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            // Get the additional attributes needed for the payment
            float baseAmount = bill.getBaseAmount();
            float taxAmount = bill.getTaxAmount();
            float totalAmount = bill.getTotalAmount();

            // Set attributes for forwarding to the JSP page
            request.setAttribute("booking", booking);
            request.setAttribute("bill", bill);
            request.setAttribute("baseAmount", baseAmount);
            request.setAttribute("taxAmount", taxAmount);
            request.setAttribute("totalAmount", totalAmount);

            // Forward the request to payment.jsp
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/booking/payment.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            getServletContext().log("Error in PaymentController", e);
            request.setAttribute("errorMessage", "An error occurred while processing your payment.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
