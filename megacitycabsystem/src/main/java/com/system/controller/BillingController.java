package com.system.controller;

import com.system.model.*;
import com.system.service.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class BillingController extends HttpServlet {

    private static final String ACTION_UPDATE_RIDE_STATUS = "updateRideStatus";
    private static final String ACTION_VIEW_PENDING_BILL = "viewPendingBill";
    private static final String ACTION_PRINT_BILL = "printBill";
    private static final String BOOKING_STATUS_COMPLETED = "completed";
    private static final String BILL_STATUS_PAID = "paid";
    private static final String VEHICLE_STATUS_ACTIVE = "Active";
    private static final String VEHICLE_STATUS_BOOKED = "Booked";
    private static final String ERROR_PAGE = "/error.jsp";
    private static final String PAYMENT_SUCCESS_PAGE = "/WEB-INF/views/booking/paymentSuccess.jsp";
    private static final String EMAIL_TEMPLATE_PATH = "booking_confirmation_email.html";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Logger logger = Logger.getLogger(BillingController.class.getName());
    
    // Email configuration properties from environment variables
    private String emailUsername;
    private String emailPassword;
    private String emailFrom;
    
    private BookingService bookingService;
    private BillService billService;
    private VehicleService vehicleService;
    private DriverService driverService;
    private CustomerService customerService;
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        super.init();
        
        // Initialize services
        bookingService = new BookingService();
        billService = new BillService();
        vehicleService = new VehicleService();
        driverService = new DriverService();
        customerService = new CustomerService();

        // Get email configuration from environment variables
        emailUsername = getEnvOrDefault("EMAIL_USERNAME", "abcdtester11@gmail.com");
        emailPassword = getEnvOrDefault("EMAIL_PASSWORD", "");
        emailFrom = getEnvOrDefault("EMAIL_FROM", "megacitycabs@megacitycabs.com");
        
        // Log warning if email credentials are not set
        if (emailUsername.isEmpty() || emailPassword.isEmpty()) {
            logger.warning("Email credentials are not set in environment variables. Emails will not be sent.");
        }

        // Setup Thymeleaf template engine
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }
    
    // Helper method to get environment variable with default value
    private String getEnvOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession(false);
            User user = (session != null) ? (User) session.getAttribute("user") : null;

            if (user == null) {
                response.sendRedirect("login");
                return;
            }

            String action = request.getParameter("action");

            if (ACTION_UPDATE_RIDE_STATUS.equals(action)) {
                updateRideStatus(request, response);
            } else if (ACTION_VIEW_PENDING_BILL.equals(action)) {
                viewPendingBill(request, response);
            } else if (ACTION_PRINT_BILL.equals(action)) {
                handlePrintBill(request, response);
            } else {
                createBookingAndBill(request, response, user);
            }

        } catch (Exception e) {
            getServletContext().log("Error processing billing", e);
            request.setAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }
    }

    private void updateRideStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookingId = request.getParameter("bookingId");
        String billId = request.getParameter("billId");

        Booking booking = bookingService.getBookingById(bookingId);

        if (booking == null) {
            request.setAttribute("errorMessage", "Booking for this ID is not found.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }

        // Update bill status only, triggers will handle the rest
        boolean billUpdated = billService.updateBillStatus(billId, BILL_STATUS_PAID);

        if (billUpdated) {
            request.setAttribute("bookingId", bookingId);
            request.getRequestDispatcher(PAYMENT_SUCCESS_PAGE).forward(request, response);

            try {
                sendBookingConfirmationEmail(booking, billId);
            } catch (MessagingException e) {
                logger.log(Level.SEVERE, "Failed to send confirmation email.", e);
            }

        } else {
            request.setAttribute("errorMessage", "An error occurred while updating payment status.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }
    }
    
    private void viewPendingBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookingId = request.getParameter("bookingId");
        Bill bill = billService.getBillByBookingId(bookingId);

        if (bill == null || bill.getBillId() != null) {
            response.sendRedirect("booking/payment?bookingId=" + bookingId + "&billId=" + bill.getBillId());
        } else {
            request.setAttribute("errorMessage", "No Bill found for the requested Booking Id");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }

    }

    private void handlePrintBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookingId = request.getParameter("bookingId");
        Booking booking = bookingService.getBookingById(bookingId);

        if (booking == null) {
            request.setAttribute("errorMessage", "Booking not found.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }

        Bill bill = billService.getBillByBookingId(bookingId);

        if (bill == null) {
            request.setAttribute("errorMessage", "Bill not found for this booking.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }
        // Convert LocalDateTime to Date

        LocalDateTime ldt = booking.getBookingTime();

        java.util.Date date = java.util.Date.from(ldt.atZone(java.time.ZoneId.systemDefault()).toInstant());


        // Set attributes for the JSP
        request.setAttribute("booking", booking);
        request.setAttribute("bill", bill);
        request.setAttribute("date",date);


        request.getRequestDispatcher("/WEB-INF/views/booking/printBill.jsp").forward(request, response);
    }

    private void createBookingAndBill(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        String pickupLocation = request.getParameter("pickupLocation");
        String dropLocation = request.getParameter("dropLocation");
        float distance = Float.parseFloat(request.getParameter("distance"));
        float baseAmount = Float.parseFloat(request.getParameter("baseAmount"));
        float taxAmount = Float.parseFloat(request.getParameter("taxAmount"));
        float discountAmount = Float.parseFloat(request.getParameter("discountAmount"));
        float totalAmount = Float.parseFloat(request.getParameter("totalAmount"));

        Integer vehicleId = null;
        Integer driverId = null;
        try {
            String vehicleIdParam = request.getParameter("vehicleId");
            if (vehicleIdParam != null && !vehicleIdParam.isEmpty()) {
                vehicleId = Integer.parseInt(vehicleIdParam);
            }

            String driverIdParam = request.getParameter("driverId");
            if (driverIdParam != null && !driverIdParam.isEmpty()) {
                driverId = Integer.parseInt(driverIdParam);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid number format for vehicle or driver.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }

        int customerId = Integer.parseInt(request.getParameter("customerId"));

        if (vehicleId == null) {
            request.setAttribute("errorMessage", "Vehicle ID is required.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }

        Driver driver = (driverId != null) ? driverService.getDriverById(driverId) : null;
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        Customer customer = customerService.getCustomerById(customerId);

        if (driver == null || vehicle == null || customer == null) {
            request.setAttribute("errorMessage", "Invalid driver, vehicle, or customer data.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }

        String bookingId = request.getParameter("bookingId");

        if (bookingId == null || bookingId.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Booking ID is required.");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }

        Booking booking = new Booking(
                bookingId,
                LocalDateTime.now(),
                pickupLocation,
                dropLocation,
                distance,
                "assigned",
                driver,
                vehicle,
                customer
        );

        boolean bookingCreated = bookingService.createBooking(booking);

        if (bookingCreated) {
            boolean vehicleStatusUpdated = vehicleService.updateVehicleStatus(vehicleId, VEHICLE_STATUS_BOOKED);
            if (!vehicleStatusUpdated) {
                request.setAttribute("errorMessage", "Vehicle can not be updated.");
                request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
                return;
            }

            String billId = request.getParameter("billId");

            Bill bill = new Bill(billId, booking, baseAmount, taxAmount, discountAmount, totalAmount, "pending", user);

            billService.createBill(bill);

            response.sendRedirect("booking/payment?bookingId=" + bookingId + "&billId=" + billId
                    + "&baseAmount=" + baseAmount + "&taxAmount=" + taxAmount + "&discountAmount=" + discountAmount + "&totalAmount=" + totalAmount);
        }
    }

    private void sendBookingConfirmationEmail(Booking booking, String billId) throws MessagingException, IOException {
        // Check if email credentials are configured
        if (emailUsername.isEmpty() || emailPassword.isEmpty()) {
            logger.warning("Email credentials not configured. Skipping email notification.");
            return;
        }
        
        Customer customer = booking.getCustomer();

        Context context = new Context();
        context.setVariable("customerName", customer.getUser().getName());
        context.setVariable("bookingId", booking.getBookingId());
        context.setVariable("bookingTime", booking.getBookingTime().format(DATE_TIME_FORMATTER));
        context.setVariable("pickupLocation", booking.getPickupLocation());
        context.setVariable("dropLocation", booking.getDestination());
        context.setVariable("billId", billId);

        Bill bill = billService.getBillById(billId);
        if (bill != null) {
            context.setVariable("baseAmount", bill.getBaseAmount());
            context.setVariable("taxAmount", bill.getTaxAmount());
            context.setVariable("discountAmount", bill.getDiscountAmount());
            context.setVariable("totalAmount", bill.getTotalAmount());
        } else {
            logger.warning("Bill not found for billId: " + billId);
        }

        StringWriter stringWriter = new StringWriter();
        templateEngine.process(EMAIL_TEMPLATE_PATH, context, stringWriter);
        String emailContent = stringWriter.toString();

        // Gmail SMTP configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create authenticator with Gmail credentials
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUsername, emailPassword);
            }
        });

        try {
            InternetAddress internetAddress = new InternetAddress(customer.getUser().getEmail());
            Address[] addresses = {internetAddress};
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailFrom, "Megacity Cab Service"));
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.setSubject("Megacity Cab - Booking Confirmation");
            message.setContent(emailContent, "text/html; charset=utf-8");

            Transport.send(message);
            logger.info("Confirmation email sent to " + customer.getUser().getEmail());
        } catch (MessagingException e) {
            logger.log(Level.SEVERE, "Error sending email", e);
            throw e;
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
    }
}