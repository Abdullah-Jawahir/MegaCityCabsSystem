package com.system.controller;

import static org.mockito.Mockito.*;

import com.system.model.User;
import com.system.service.UserService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

public class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authController;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        // Inject the mocked UserService into the controller
        // We need to set it via reflection since the controller initializes it in init()
        java.lang.reflect.Field field = AuthenticationController.class.getDeclaredField("userService");
        field.setAccessible(true);
        field.set(authController, userService);
        
        // Common request/response setup
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        // Test the GET request handling (displaying login page)
        authController.doGet(request, response);
        
        // Verify that the request is forwarded to the login page
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_SuccessfulLogin_AdminRole() throws ServletException, IOException {
        // Setup test data
        String username = "admin";
        String password = "password123";
        User adminUser = new User("Admin User", username, password, "admin", "admin@example.com", "1234567890", LocalDateTime.now());
        
        // Setup mock behavior
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(userService.authenticateUser(username, password)).thenReturn(adminUser);
        
        // Execute the method under test
        authController.doPost(request, response);
        
        // Verify interactions
        verify(session).setAttribute("user", adminUser);
        verify(response).sendRedirect("admin");
    }

    @Test
    public void testDoPost_SuccessfulLogin_CustomerRole_NoPendingBooking() throws ServletException, IOException {
        // Setup test data
        String username = "customer";
        String password = "password123";
        User customerUser = new User("Customer User", username, password, "customer", "customer@example.com", "1234567890", LocalDateTime.now());
        
        // Setup mock behavior
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(userService.authenticateUser(username, password)).thenReturn(customerUser);
        
        // No pending booking
        when(session.getAttribute("pendingPickupLocation")).thenReturn(null);
        
        // Execute the method under test
        authController.doPost(request, response);
        
        // Verify interactions
        verify(session).setAttribute("user", customerUser);
        verify(response).sendRedirect("home");
    }

    @Test
    public void testDoPost_SuccessfulLogin_DriverRole_WithPendingBooking() throws ServletException, IOException {
        // Setup test data
        String username = "driver";
        String password = "password123";
        User driverUser = new User("Driver User", username, password, "driver", "driver@example.com", "1234567890", LocalDateTime.now());
        
        // Setup mock behavior
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(userService.authenticateUser(username, password)).thenReturn(driverUser);
        
        // Set up pending booking
        String pickupLocation = "Location A";
        String dropLocation = "Location B";
        String distance = "10.5";
        Integer vehicleId = 123;
        
        when(session.getAttribute("pendingPickupLocation")).thenReturn(pickupLocation);
        when(session.getAttribute("pendingDropLocation")).thenReturn(dropLocation);
        when(session.getAttribute("pendingDistance")).thenReturn(distance);
        when(session.getAttribute("pendingSelectedVehicleId")).thenReturn(vehicleId);
        
        // Execute the method under test
        authController.doPost(request, response);
        
        // Verify interactions
        verify(session).setAttribute("user", driverUser);
        verify(session).removeAttribute("pendingPickupLocation");
        verify(session).removeAttribute("pendingDropLocation");
        verify(session).removeAttribute("pendingDistance");
        verify(session).removeAttribute("pendingSelectedVehicleId");
        
        // Correct way to verify redirect with parameters using Mockito's ArgumentCaptor
        ArgumentCaptor<String> redirectCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).sendRedirect(redirectCaptor.capture());
        
        String expectedUrlStart = "processBooking?pickupLocation=";
        String capturedUrl = redirectCaptor.getValue();
        
        // Verify the URL contains expected components
        assert capturedUrl.startsWith(expectedUrlStart);
        assert capturedUrl.contains("pickupLocation=" + java.net.URLEncoder.encode(pickupLocation, "UTF-8"));
        assert capturedUrl.contains("dropLocation=" + java.net.URLEncoder.encode(dropLocation, "UTF-8"));
        assert capturedUrl.contains("distance=" + java.net.URLEncoder.encode(distance, "UTF-8"));
        assert capturedUrl.contains("selectedVehicleId=" + vehicleId);
    }

    @Test
    public void testDoPost_FailedLogin() throws ServletException, IOException {
        // Setup test data
        String username = "invalid";
        String password = "wrongpassword";
        
        // Setup mock behavior
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(userService.authenticateUser(username, password)).thenReturn(null); // Authentication fails
        
        // Execute the method under test
        authController.doPost(request, response);
        
        // Verify interactions
        verify(session).setAttribute("errorMessage", "Invalid username or password.");
        verify(response).sendRedirect("login");
    }

    @Test
    public void testDoPost_PendingBooking_InvalidVehicleId() throws ServletException, IOException {
        // Setup test data
        String username = "customer";
        String password = "password123";
        User customerUser = new User("Customer User", username, password, "customer", "customer@example.com", "1234567890", LocalDateTime.now());
        
        // Setup mock behavior
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(userService.authenticateUser(username, password)).thenReturn(customerUser);
        
        // Set up pending booking with invalid vehicle ID
        when(session.getAttribute("pendingPickupLocation")).thenReturn("Location A");
        when(session.getAttribute("pendingDropLocation")).thenReturn("Location B");
        when(session.getAttribute("pendingDistance")).thenReturn("10.5");
        when(session.getAttribute("pendingSelectedVehicleId")).thenReturn("not-a-number"); // Invalid ID
        
        // Execute the method under test
        authController.doPost(request, response);
        
        // Verify interactions - should redirect to home with error message
        ArgumentCaptor<String> redirectCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).sendRedirect(redirectCaptor.capture());
        
        String capturedUrl = redirectCaptor.getValue();
        assert capturedUrl.startsWith("home?message=");
        assert capturedUrl.contains(java.net.URLEncoder.encode("An error occurred while processing your booking. Please try again.", "UTF-8"));
    }

    @Test
    public void testDoPost_PendingBooking_NullVehicleId() throws ServletException, IOException {
        // Setup test data
        String username = "customer";
        String password = "password123";
        User customerUser = new User("Customer User", username, password, "customer", "customer@example.com", "1234567890", LocalDateTime.now());
        
        // Setup mock behavior
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(userService.authenticateUser(username, password)).thenReturn(customerUser);
        
        // Set up pending booking with null vehicle ID
        when(session.getAttribute("pendingPickupLocation")).thenReturn("Location A");
        when(session.getAttribute("pendingDropLocation")).thenReturn("Location B");
        when(session.getAttribute("pendingDistance")).thenReturn("10.5");
        when(session.getAttribute("pendingSelectedVehicleId")).thenReturn(null);
        
        // Execute the method under test
        authController.doPost(request, response);
        
        verify(response).sendRedirect("home");
    }

    @Test
    public void testDoDelete_Logout() throws ServletException, IOException {
        // Execute the method under test (logout)
        authController.doDelete(request, response);
        
        // Verify interactions
        verify(session).invalidate();
        verify(response).sendRedirect("/login");
    }
}