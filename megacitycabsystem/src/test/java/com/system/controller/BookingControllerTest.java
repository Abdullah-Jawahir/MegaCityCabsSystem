package com.system.controller;

import static org.mockito.Mockito.*;

import com.system.model.*;
import com.system.service.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class BookingControllerTest {

    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private RequestDispatcher dispatcher;
    
    @Mock
    private HttpSession session;
    
    @Mock
    private BookingService bookingService;
    
    @Mock
    private CustomerService customerService;
    
    @Mock
    private VehicleService vehicleService;
    
    @Mock
    private DriverService driverService;
    
    @Mock
    private BillService billService;
    
    private BookingController bookingController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        // Initialize the controller
        bookingController = new BookingController() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void init() {
                // Override to avoid actual initialization
            }
        };
        
        // Set the mocked services using reflection
        java.lang.reflect.Field bookingServiceField = BookingController.class.getDeclaredField("bookingService");
        bookingServiceField.setAccessible(true);
        bookingServiceField.set(bookingController, bookingService);
        
        java.lang.reflect.Field customerServiceField = BookingController.class.getDeclaredField("customerService");
        customerServiceField.setAccessible(true);
        customerServiceField.set(bookingController, customerService);
        
        java.lang.reflect.Field vehicleServiceField = BookingController.class.getDeclaredField("vehicleService");
        vehicleServiceField.setAccessible(true);
        vehicleServiceField.set(bookingController, vehicleService);
        
        java.lang.reflect.Field driverServiceField = BookingController.class.getDeclaredField("driverService");
        driverServiceField.setAccessible(true);
        driverServiceField.set(bookingController, driverService);
        
        java.lang.reflect.Field billServiceField = BookingController.class.getDeclaredField("billService");
        billServiceField.setAccessible(true);
        billServiceField.set(bookingController, billService);
    }

    @Test
    public void testProcessCreateBooking_Success() throws Exception {
        // Arrange
        String pickupLocation = "123 Main St";
        String destination = "456 Park Ave";
        float distance = 10.5f;
        String status = "pending";
        int customerId = 1;
        int vehicleId = 2;
        
        // Mock request parameters
        when(request.getParameter("action")).thenReturn("processCreateBooking");
        when(request.getParameter("pickupLocation")).thenReturn(pickupLocation);
        when(request.getParameter("destination")).thenReturn(destination);
        when(request.getParameter("distance")).thenReturn(String.valueOf(distance));
        when(request.getParameter("status")).thenReturn(status);
        when(request.getParameter("customerId")).thenReturn(String.valueOf(customerId));
        when(request.getParameter("vehicleId")).thenReturn(String.valueOf(vehicleId));
        
        // Mock session
        when(request.getSession(false)).thenReturn(session);
        User mockUser = new User("John Doe", "johndoe", "password", "CUSTOMER", "john@example.com", "1234567890", LocalDateTime.now());
        mockUser.setId(5);
        when(session.getAttribute("user")).thenReturn(mockUser);
        
        // Mock customer
        Customer mockCustomer = new Customer(customerId, mockUser, "REG123", "123 Customer St", "NIC12345");
        when(customerService.getCustomerById(customerId)).thenReturn(mockCustomer);
        
        // Mock vehicle
        Vehicle mockVehicle = new Vehicle(vehicleId, "ABC123", "Toyota Camry", "Active", 3, LocalDateTime.now(), 1.5f);
        when(vehicleService.getVehicleById(vehicleId)).thenReturn(mockVehicle);
        
        // Mock driver
        User driverUser = new User("Driver Name", "driver1", "password", "DRIVER", "driver@example.com", "9876543210", LocalDateTime.now());
        driverUser.setId(6);
        Driver mockDriver = new Driver(3, driverUser, "DL12345", "Available");
        when(driverService.getDriverByVehicleId(vehicleId)).thenReturn(mockDriver);
        
        // Mock successful booking creation
        when(bookingService.createBooking(any(Booking.class))).thenReturn(true);
        
        // Mock successful vehicle status update
        when(vehicleService.updateVehicleStatus(vehicleId, "Booked")).thenReturn(true);
        
        // Mock successful bill generation
        when(billService.generateBill(anyString(), any(User.class))).thenReturn(true);
        
        // Act
        bookingController.doPost(request, response);
        
        // Assert
        verify(bookingService).createBooking(any(Booking.class));
        verify(vehicleService).updateVehicleStatus(vehicleId, "Booked");
        verify(billService).generateBill(anyString(), eq(mockUser));
        verify(response).sendRedirect("booking?success=Booking and Bill created successfully");
    }
    
    @Test
    public void testProcessCreateBooking_NoVehicleSelected() throws Exception {
        // Arrange
        when(request.getParameter("action")).thenReturn("processCreateBooking");
        when(request.getParameter("pickupLocation")).thenReturn("123 Main St");
        when(request.getParameter("destination")).thenReturn("456 Park Ave");
        when(request.getParameter("distance")).thenReturn("10.5");
        when(request.getParameter("status")).thenReturn("pending");
        when(request.getParameter("customerId")).thenReturn("1");
        when(request.getParameter("vehicleId")).thenReturn("");  // Empty vehicle ID
        
        when(request.getRequestDispatcher("/WEB-INF/views/admin/bookings/addBooking.jsp")).thenReturn(dispatcher);
        
        // Act
        bookingController.doPost(request, response);
        
        // Assert
        verify(request).setAttribute("error", "Please select a vehicle.");
        verify(request, never()).getSession(false);
        verify(dispatcher).forward(request, response);
    }
    
    @Test
    public void testProcessCreateBooking_NoDriverAvailable() throws Exception {
        // Arrange
        String pickupLocation = "123 Main St";
        String destination = "456 Park Ave";
        float distance = 10.5f;
        String status = "pending";
        int customerId = 1;
        int vehicleId = 2;
        
        // Mock request parameters
        when(request.getParameter("action")).thenReturn("processCreateBooking");
        when(request.getParameter("pickupLocation")).thenReturn(pickupLocation);
        when(request.getParameter("destination")).thenReturn(destination);
        when(request.getParameter("distance")).thenReturn(String.valueOf(distance));
        when(request.getParameter("status")).thenReturn(status);
        when(request.getParameter("customerId")).thenReturn(String.valueOf(customerId));
        when(request.getParameter("vehicleId")).thenReturn(String.valueOf(vehicleId));
        
        // Mock customer
        User mockUser = new User("John Doe", "johndoe", "password", "CUSTOMER", "john@example.com", "1234567890", LocalDateTime.now());
        mockUser.setId(5);
        Customer mockCustomer = new Customer(customerId, mockUser, "REG123", "123 Customer St", "NIC12345");
        when(customerService.getCustomerById(customerId)).thenReturn(mockCustomer);
        
        // Mock vehicle
        Vehicle mockVehicle = new Vehicle(vehicleId, "ABC123", "Toyota Camry", "Active", null, LocalDateTime.now(), 1.5f);
        when(vehicleService.getVehicleById(vehicleId)).thenReturn(mockVehicle);
        
        // No driver available for the vehicle
        when(driverService.getDriverByVehicleId(vehicleId)).thenReturn(null);
        
        when(request.getRequestDispatcher("/WEB-INF/views/admin/bookings/addBooking.jsp")).thenReturn(dispatcher);
        
        // Act
        bookingController.doPost(request, response);
        
        // Assert
        verify(request).setAttribute("error", "No driver available for the vehicle. Please select another vehicle.");
        verify(dispatcher).forward(request, response);
        verify(bookingService, never()).createBooking(any(Booking.class));
    }
    
    @Test
    public void testProcessCreateBooking_BookingCreationFails() throws Exception {
        // Arrange
        String pickupLocation = "123 Main St";
        String destination = "456 Park Ave";
        float distance = 10.5f;
        String status = "pending";
        int customerId = 1;
        int vehicleId = 2;
        
        // Mock request parameters
        when(request.getParameter("action")).thenReturn("processCreateBooking");
        when(request.getParameter("pickupLocation")).thenReturn(pickupLocation);
        when(request.getParameter("destination")).thenReturn(destination);
        when(request.getParameter("distance")).thenReturn(String.valueOf(distance));
        when(request.getParameter("status")).thenReturn(status);
        when(request.getParameter("customerId")).thenReturn(String.valueOf(customerId));
        when(request.getParameter("vehicleId")).thenReturn(String.valueOf(vehicleId));
        
        // Mock customer
        User mockUser = new User("John Doe", "johndoe", "password", "CUSTOMER", "john@example.com", "1234567890", LocalDateTime.now());
        mockUser.setId(5);
        Customer mockCustomer = new Customer(customerId, mockUser, "REG123", "123 Customer St", "NIC12345");
        when(customerService.getCustomerById(customerId)).thenReturn(mockCustomer);
        
        // Mock vehicle
        Vehicle mockVehicle = new Vehicle(vehicleId, "ABC123", "Toyota Camry", "Active", 3, LocalDateTime.now(), 1.5f);
        when(vehicleService.getVehicleById(vehicleId)).thenReturn(mockVehicle);
        
        // Mock driver
        User driverUser = new User("Driver Name", "driver1", "password", "DRIVER", "driver@example.com", "9876543210", LocalDateTime.now());
        driverUser.setId(6);
        Driver mockDriver = new Driver(3, driverUser, "DL12345", "Available");
        when(driverService.getDriverByVehicleId(vehicleId)).thenReturn(mockDriver);
        
        // Mock failed booking creation
        when(bookingService.createBooking(any(Booking.class))).thenReturn(false);
        
        when(request.getRequestDispatcher("/WEB-INF/views/admin/bookings/addBooking.jsp")).thenReturn(dispatcher);
        
        // Act
        bookingController.doPost(request, response);
        
        // Assert
        verify(bookingService).createBooking(any(Booking.class));
        verify(request).setAttribute("error", "Failed to create booking. Please try again.");
        verify(request).setAttribute("pickupLocation", pickupLocation);
        verify(request).setAttribute("destination", destination);
        verify(request).setAttribute("status", status);
        verify(request).setAttribute("customerId", customerId);
        verify(request).setAttribute("vehicleId", vehicleId);
        verify(dispatcher).forward(request, response);
    }
    
    @Test
    public void testProcessCreateBooking_VehicleUpdateFails() throws Exception {
        // Arrange
        String pickupLocation = "123 Main St";
        String destination = "456 Park Ave";
        float distance = 10.5f;
        String status = "pending";
        int customerId = 1;
        int vehicleId = 2;
        
        // Mock request parameters
        when(request.getParameter("action")).thenReturn("processCreateBooking");
        when(request.getParameter("pickupLocation")).thenReturn(pickupLocation);
        when(request.getParameter("destination")).thenReturn(destination);
        when(request.getParameter("distance")).thenReturn(String.valueOf(distance));
        when(request.getParameter("status")).thenReturn(status);
        when(request.getParameter("customerId")).thenReturn(String.valueOf(customerId));
        when(request.getParameter("vehicleId")).thenReturn(String.valueOf(vehicleId));
        
        // Mock session
        when(request.getSession(false)).thenReturn(session);
        User mockUser = new User("John Doe", "johndoe", "password", "CUSTOMER", "john@example.com", "1234567890", LocalDateTime.now());
        mockUser.setId(5);
        when(session.getAttribute("user")).thenReturn(mockUser);
        
        // Mock customer
        Customer mockCustomer = new Customer(customerId, mockUser, "REG123", "123 Customer St", "NIC12345");
        when(customerService.getCustomerById(customerId)).thenReturn(mockCustomer);
        
        // Mock vehicle
        Vehicle mockVehicle = new Vehicle(vehicleId, "ABC123", "Toyota Camry", "Active", 3, LocalDateTime.now(), 1.5f);
        when(vehicleService.getVehicleById(vehicleId)).thenReturn(mockVehicle);
        
        // Mock driver
        User driverUser = new User("Driver Name", "driver1", "password", "DRIVER", "driver@example.com", "9876543210", LocalDateTime.now());
        driverUser.setId(6);
        Driver mockDriver = new Driver(3, driverUser, "DL12345", "Available");
        when(driverService.getDriverByVehicleId(vehicleId)).thenReturn(mockDriver);
        
        // Mock successful booking creation
        when(bookingService.createBooking(any(Booking.class))).thenReturn(true);
        
        // Mock failed vehicle status update
        when(vehicleService.updateVehicleStatus(vehicleId, "Booked")).thenReturn(false);
        
        when(request.getRequestDispatcher("/error.jsp")).thenReturn(dispatcher);
        
        // Act
        bookingController.doPost(request, response);
        
        // Assert
        verify(bookingService).createBooking(any(Booking.class));
        verify(vehicleService).updateVehicleStatus(vehicleId, "Booked");
        verify(request).setAttribute("errorMessage", "Vehicle can not be updated.");
        verify(dispatcher).forward(request, response);
    }
    
    @Test
    public void testProcessCreateBooking_BillGenerationFails() throws Exception {
        // Arrange
        String pickupLocation = "123 Main St";
        String destination = "456 Park Ave";
        float distance = 10.5f;
        String status = "pending";
        int customerId = 1;
        int vehicleId = 2;
        
        // Mock request parameters
        when(request.getParameter("action")).thenReturn("processCreateBooking");
        when(request.getParameter("pickupLocation")).thenReturn(pickupLocation);
        when(request.getParameter("destination")).thenReturn(destination);
        when(request.getParameter("distance")).thenReturn(String.valueOf(distance));
        when(request.getParameter("status")).thenReturn(status);
        when(request.getParameter("customerId")).thenReturn(String.valueOf(customerId));
        when(request.getParameter("vehicleId")).thenReturn(String.valueOf(vehicleId));
        
        // Mock session
        when(request.getSession(false)).thenReturn(session);
        User mockUser = new User("John Doe", "johndoe", "password", "CUSTOMER", "john@example.com", "1234567890", LocalDateTime.now());
        mockUser.setId(5);
        when(session.getAttribute("user")).thenReturn(mockUser);
        
        // Mock customer
        Customer mockCustomer = new Customer(customerId, mockUser, "REG123", "123 Customer St", "NIC12345");
        when(customerService.getCustomerById(customerId)).thenReturn(mockCustomer);
        
        // Mock vehicle
        Vehicle mockVehicle = new Vehicle(vehicleId, "ABC123", "Toyota Camry", "Active", 3, LocalDateTime.now(), 1.5f);
        when(vehicleService.getVehicleById(vehicleId)).thenReturn(mockVehicle);
        
        // Mock driver
        User driverUser = new User("Driver Name", "driver1", "password", "DRIVER", "driver@example.com", "9876543210", LocalDateTime.now());
        driverUser.setId(6);
        Driver mockDriver = new Driver(3, driverUser, "DL12345", "Available");
        when(driverService.getDriverByVehicleId(vehicleId)).thenReturn(mockDriver);
        
        // Mock successful booking creation
        when(bookingService.createBooking(any(Booking.class))).thenReturn(true);
        
        // Mock successful vehicle status update
        when(vehicleService.updateVehicleStatus(vehicleId, "Booked")).thenReturn(true);
        
        // Mock failed bill generation
        when(billService.generateBill(anyString(), any(User.class))).thenReturn(false);
        
        // Act
        bookingController.doPost(request, response);
        
        // Assert
        verify(bookingService).createBooking(any(Booking.class));
        verify(vehicleService).updateVehicleStatus(vehicleId, "Booked");
        verify(billService).generateBill(anyString(), eq(mockUser));
        verify(response).sendRedirect("booking?success=Booking created successfully but Bill not generated.");
    }
    
    @Test
    public void testProcessCreateBooking_UnexpectedException() throws Exception {
        // Arrange
        when(request.getParameter("action")).thenReturn("processCreateBooking");
        when(request.getParameter("pickupLocation")).thenReturn("123 Main St");
        when(request.getParameter("destination")).thenReturn("456 Park Ave");
        when(request.getParameter("distance")).thenReturn("10.5");
        when(request.getParameter("status")).thenReturn("pending");
        when(request.getParameter("customerId")).thenReturn("1");
        when(request.getParameter("vehicleId")).thenReturn("2");
        
        // Simulate an exception during customerService.getCustomerById()
        when(customerService.getCustomerById(anyInt())).thenThrow(new RuntimeException("Database connection failed"));
        
        when(request.getRequestDispatcher("/WEB-INF/views/admin/bookings/addBooking.jsp")).thenReturn(dispatcher);
        
        // Act
        bookingController.doPost(request, response);
        
        // Assert
        verify(request).setAttribute(eq("error"), contains("An unexpected error occurred"));
        verify(dispatcher).forward(request, response);
    }
}