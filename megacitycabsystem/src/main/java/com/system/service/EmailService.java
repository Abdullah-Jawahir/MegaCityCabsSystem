package com.system.service;

import com.system.model.Booking;
import com.system.model.Bill;
import com.system.model.Customer;
import com.system.observer.BookingObserver;

import javax.mail.*;
import javax.mail.internet.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import java.io.IOException;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailService implements BookingObserver {
    private static final Logger logger = Logger.getLogger(EmailService.class.getName());
    private static final String EMAIL_TEMPLATE_PATH = "booking_confirmation_email.html";
    private static final DateTimeFormatter READABLE_DATE_FORMAT = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy hh:mm a");

    private String emailUsername;
    private String emailPassword;
    private String emailFrom;
    private BillService billService;
    private TemplateEngine templateEngine;

    public EmailService() {
        // Load email configurations from environment variables
        this.emailUsername = getEnvOrDefault("EMAIL_USERNAME", "abcdtester11@gmail.com");
        this.emailPassword = getEnvOrDefault("EMAIL_PASSWORD", "");
        this.emailFrom = getEnvOrDefault("EMAIL_FROM", "megacitycabs@megacitycabs.com");
        
        // Initialize services
        this.billService = new BillService();

        // Setup Thymeleaf template engine
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");

        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
    }

    @Override
    public void onBookingConfirmed(Booking booking, String billId) {
        try {
            sendBookingConfirmationEmail(booking, billId);
        } catch (MessagingException | IOException e) {
            logger.log(Level.SEVERE, "Error sending email notification", e);
        }
    }

    private void sendBookingConfirmationEmail(Booking booking, String billId) throws MessagingException, IOException {
        if (emailUsername.isEmpty() || emailPassword.isEmpty()) {
            logger.warning("Email credentials not configured. Skipping email notification.");
            return;
        }

        Customer customer = booking.getCustomer();

        Context context = new Context();
        context.setVariable("customerName", customer.getUser().getName());
        context.setVariable("bookingId", booking.getBookingId());
        String formattedBookingTime = booking.getBookingTime().format(READABLE_DATE_FORMAT);
        context.setVariable("bookingTime", formattedBookingTime);
        context.setVariable("pickupLocation", booking.getPickupLocation());
        context.setVariable("dropLocation", booking.getDestination());
        context.setVariable("billId", billId);

        String baseUrl = "http://localhost:8080/megacitycabsystem";
        Bill bill = billService.getBillById(billId);

        if (bill != null) {
            context.setVariable("baseAmount", bill.getBaseAmount());
            context.setVariable("taxAmount", bill.getTaxAmount());
            context.setVariable("discountAmount", bill.getDiscountAmount());
            context.setVariable("totalAmount", bill.getTotalAmount());
            context.setVariable("billStatus", bill.getStatus());
            context.setVariable("baseUrl", baseUrl);
        } else {
            logger.warning("Bill not found for billId: " + billId);
        }

        StringWriter stringWriter = new StringWriter();
        templateEngine.process(EMAIL_TEMPLATE_PATH, context, stringWriter);
        String emailContent = stringWriter.toString();

        // Email Configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

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

    private String getEnvOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
}
