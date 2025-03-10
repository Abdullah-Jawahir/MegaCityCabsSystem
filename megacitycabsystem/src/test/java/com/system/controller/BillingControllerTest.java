package com.system.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.system.model.*;
import com.system.service.*;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BillingControllerTest {

    private BillingController billingController;

    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private HttpSession session;
    
    @Mock
    private BookingService bookingService;
    
    @Mock
    private BillService billService;
    
    @Mock
    private VehicleService vehicleService;
    
    @Mock
    private DriverService driverService;
    
    @Mock
    private CustomerService customerService;
    
    @Mock
    private ServletContext servletContext;
    
    @Mock
    private RequestDispatcher requestDispatcher;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        
        billingController = new BillingController() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public ServletContext getServletContext() {
                return servletContext;
            }
        };
        
        // Use reflection to inject the mocked services
        java.lang.reflect.Field bookingServiceField = BillingController.class.getDeclaredField("bookingService");
        bookingServiceField.setAccessible(true);
        bookingServiceField.set(billingController, bookingService);
        
        java.lang.reflect.Field billServiceField = BillingController.class.getDeclaredField("billService");
        billServiceField.setAccessible(true);
        billServiceField.set(billingController, billService);
        
        java.lang.reflect.Field vehicleServiceField = BillingController.class.getDeclaredField("vehicleService");
        vehicleServiceField.setAccessible(true);
        vehicleServiceField.set(billingController, vehicleService);
        
        java.lang.reflect.Field driverServiceField = BillingController.class.getDeclaredField("driverService");
        driverServiceField.setAccessible(true);
        driverServiceField.set(billingController, driverService);
        
        java.lang.reflect.Field customerServiceField = BillingController.class.getDeclaredField("customerService");
        customerServiceField.setAccessible(true);
        customerServiceField.set(billingController, customerService);
        
        // Mock session setup
        when(request.getSession(false)).thenReturn(session);
        
        // Mock ServletContext for error handling
        when(servletContext.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testCreateBookingAndBillSuccess() throws ServletException, IOException {
        // Mock user in session
        User user = new User("John Doe", "johndoe", "password123", "customer", "john@example.com", "1234567890", LocalDateTime.now());
        when(session.getAttribute("user")).thenReturn(user);
        
        // Mock request parameters
        when(request.getParameter("action")).thenReturn(null); // No action means createBookingAndBill
        when(request.getParameter("pickupLocation")).thenReturn("Start Point");
        when(request.getParameter("dropLocation")).thenReturn("End Point");
        when(request.getParameter("distance")).thenReturn("10.5");
        when(request.getParameter("baseAmount")).thenReturn("500.0");
        when(request.getParameter("taxAmount")).thenReturn("50.0");
        when(request.getParameter("discountAmount")).thenReturn("25.0");
        when(request.getParameter("totalAmount")).thenReturn("525.0");
        when(request.getParameter("vehicleId")).thenReturn("1");
        when(request.getParameter("driverId")).thenReturn("2");
        when(request.getParameter("customerId")).thenReturn("3");
        when(request.getParameter("bookingId")).thenReturn("BOOK123");
        when(request.getParameter("billId")).thenReturn("BILL123");
        
        // Mock service responses
        User driverUser = new User("Driver Name", "driver", "driver123", "driver", "driver@example.com", "9876543210", LocalDateTime.now());
        Driver driver = new Driver(2, driverUser, "DL12345", "Available");
        when(driverService.getDriverById(2)).thenReturn(driver);
        
        Vehicle vehicle = new Vehicle(1, "ABC123", "SUV", "Active", 2, LocalDateTime.now(), 15.0f);
        when(vehicleService.getVehicleById(1)).thenReturn(vehicle);
        
        User customerUser = new User("Customer Name", "customer", "cust123", "customer", "customer@example.com", "5555555555", LocalDateTime.now());
        Customer customer = new Customer(3, customerUser, "REG123", "123 Main St", "NIC12345");
        when(customerService.getCustomerById(3)).thenReturn(customer);
        
        when(bookingService.createBooking(any(Booking.class))).thenReturn(true);
        when(vehicleService.updateVehicleStatus(eq(1), eq("Booked"))).thenReturn(true);
        
        // Execute the method
        billingController.doPost(request, response);
        
        // Verify booking was created with correct parameters
        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingService).createBooking(bookingCaptor.capture());
        
        Booking capturedBooking = bookingCaptor.getValue();
        assertEquals("BOOK123", capturedBooking.getBookingId());
        assertEquals("Start Point", capturedBooking.getPickupLocation());
        assertEquals("End Point", capturedBooking.getDestination());
        assertEquals(10.5f, capturedBooking.getDistance(), 0.001);
        assertEquals("assigned", capturedBooking.getStatus());
        assertEquals(driver, capturedBooking.getAssignedDriver());
        assertEquals(vehicle, capturedBooking.getAssignedVehicle());
        assertEquals(customer, capturedBooking.getCustomer());
        
        // Verify vehicle status was updated
        verify(vehicleService).updateVehicleStatus(1, "Booked");
        
        // Verify bill was created with correct parameters
        ArgumentCaptor<Bill> billCaptor = ArgumentCaptor.forClass(Bill.class);
        verify(billService).createBill(billCaptor.capture());
        
        Bill capturedBill = billCaptor.getValue();
        assertEquals("BILL123", capturedBill.getBillId());
        assertEquals(capturedBooking, capturedBill.getBooking());
        assertEquals(500.0f, capturedBill.getBaseAmount(), 0.001);
        assertEquals(50.0f, capturedBill.getTaxAmount(), 0.001);
        assertEquals(25.0f, capturedBill.getDiscountAmount(), 0.001);
        assertEquals(525.0f, capturedBill.getTotalAmount(), 0.001);
        assertEquals("pending", capturedBill.getStatus());
        assertEquals(user, capturedBill.getGeneratedBy());
        
        // Verify response was redirected correctly
        verify(response).sendRedirect("booking/payment?bookingId=BOOK123&billId=BILL123&baseAmount=500.0&taxAmount=50.0&discountAmount=25.0&totalAmount=525.0");
    }
    
    @Test
    public void testCreateBookingAndBillMissingVehicleId() throws ServletException, IOException {
        // Mock user in session
        User user = new User("John Doe", "johndoe", "password123", "customer", "john@example.com", "1234567890", LocalDateTime.now());
        when(session.getAttribute("user")).thenReturn(user);
        
        // Mock request parameters with missing vehicleId
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("pickupLocation")).thenReturn("Start Point");
        when(request.getParameter("dropLocation")).thenReturn("End Point");
        when(request.getParameter("distance")).thenReturn("10.5");
        when(request.getParameter("baseAmount")).thenReturn("500.0");
        when(request.getParameter("taxAmount")).thenReturn("50.0");
        when(request.getParameter("discountAmount")).thenReturn("25.0");
        when(request.getParameter("totalAmount")).thenReturn("525.0");
        when(request.getParameter("vehicleId")).thenReturn(""); // Empty vehicleId
        when(request.getParameter("driverId")).thenReturn("2");
        when(request.getParameter("customerId")).thenReturn("3");
        when(request.getParameter("bookingId")).thenReturn("BOOK123");
        
        // Execute the method
        billingController.doPost(request, response);
        
        // Verify error handling
        verify(request).setAttribute("errorMessage", "Vehicle ID is required.");
        verify(request).getRequestDispatcher("/error.jsp");
        verify(requestDispatcher).forward(request, response);
        
        // Verify booking was not created
        verify(bookingService, never()).createBooking(any(Booking.class));
    }
    
    @Test
    public void testCreateBookingAndBillInvalidDriver() throws ServletException, IOException {
        // Mock user in session
        User user = new User("John Doe", "johndoe", "password123", "customer", "john@example.com", "1234567890", LocalDateTime.now());
        when(session.getAttribute("user")).thenReturn(user);
        
        // Mock request parameters
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("pickupLocation")).thenReturn("Start Point");
        when(request.getParameter("dropLocation")).thenReturn("End Point");
        when(request.getParameter("distance")).thenReturn("10.5");
        when(request.getParameter("baseAmount")).thenReturn("500.0");
        when(request.getParameter("taxAmount")).thenReturn("50.0");
        when(request.getParameter("discountAmount")).thenReturn("25.0");
        when(request.getParameter("totalAmount")).thenReturn("525.0");
        when(request.getParameter("vehicleId")).thenReturn("1");
        when(request.getParameter("driverId")).thenReturn("2");
        when(request.getParameter("customerId")).thenReturn("3");
        when(request.getParameter("bookingId")).thenReturn("BOOK123");
        
        // Mock services to return null driver
        when(driverService.getDriverById(2)).thenReturn(null);
        
        Vehicle vehicle = new Vehicle(1, "ABC123", "SUV", "Active", 2, LocalDateTime.now(), 15.0f);
        when(vehicleService.getVehicleById(1)).thenReturn(vehicle);
        
        User customerUser = new User("Customer Name", "customer", "cust123", "customer", "customer@example.com", "5555555555", LocalDateTime.now());
        Customer customer = new Customer(3, customerUser, "REG123", "123 Main St", "NIC12345");
        when(customerService.getCustomerById(3)).thenReturn(customer);
        
        // Execute the method
        billingController.doPost(request, response);
        
        // Verify error handling
        verify(request).setAttribute("errorMessage", "Invalid driver, vehicle, or customer data.");
        verify(request).getRequestDispatcher("/error.jsp");
        verify(requestDispatcher).forward(request, response);
        
        // Verify booking was not created
        verify(bookingService, never()).createBooking(any(Booking.class));
    }
    
    @Test
    public void testCreateBookingAndBillVehicleUpdateFails() throws ServletException, IOException {
        // Mock user in session
        User user = new User("John Doe", "johndoe", "password123", "customer", "john@example.com", "1234567890", LocalDateTime.now());
        when(session.getAttribute("user")).thenReturn(user);
        
        // Mock request parameters
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("pickupLocation")).thenReturn("Start Point");
        when(request.getParameter("dropLocation")).thenReturn("End Point");
        when(request.getParameter("distance")).thenReturn("10.5");
        when(request.getParameter("baseAmount")).thenReturn("500.0");
        when(request.getParameter("taxAmount")).thenReturn("50.0");
        when(request.getParameter("discountAmount")).thenReturn("25.0");
        when(request.getParameter("totalAmount")).thenReturn("525.0");
        when(request.getParameter("vehicleId")).thenReturn("1");
        when(request.getParameter("driverId")).thenReturn("2");
        when(request.getParameter("customerId")).thenReturn("3");
        when(request.getParameter("bookingId")).thenReturn("BOOK123");
        
        // Mock service responses
        User driverUser = new User("Driver Name", "driver", "driver123", "driver", "driver@example.com", "9876543210", LocalDateTime.now());
        Driver driver = new Driver(2, driverUser, "DL12345", "Available");
        when(driverService.getDriverById(2)).thenReturn(driver);
        
        Vehicle vehicle = new Vehicle(1, "ABC123", "SUV", "Active", 2, LocalDateTime.now(), 15.0f);
        when(vehicleService.getVehicleById(1)).thenReturn(vehicle);
        
        User customerUser = new User("Customer Name", "customer", "cust123", "customer", "customer@example.com", "5555555555", LocalDateTime.now());
        Customer customer = new Customer(3, customerUser, "REG123", "123 Main St", "NIC12345");
        when(customerService.getCustomerById(3)).thenReturn(customer);
        
        when(bookingService.createBooking(any(Booking.class))).thenReturn(true);
        when(vehicleService.updateVehicleStatus(eq(1), eq("Booked"))).thenReturn(false); // Vehicle update fails
        
        // Execute the method
        billingController.doPost(request, response);
        
        // Verify error handling
        verify(request).setAttribute("errorMessage", "Vehicle can not be updated.");
        verify(request).getRequestDispatcher("/error.jsp");
        verify(requestDispatcher).forward(request, response);
        
        // Verify booking was created but bill was not
        verify(bookingService).createBooking(any(Booking.class));
        verify(billService, never()).createBill(any(Bill.class));
    }
}