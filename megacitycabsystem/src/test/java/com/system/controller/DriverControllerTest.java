package com.system.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.system.model.User;
import com.system.service.RegistrationService;

class DriverControllerTest {

    @InjectMocks
    private DriverController driverController;

    @Mock
    private RegistrationService registrationService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        driverController.init(); // Initialize dependencies
    }

    @Test
    void testRegisterDriver_Success() throws ServletException, IOException {
        // Mock input parameters
        when(request.getParameter("action")).thenReturn("registerDriver");
        when(request.getParameter("name")).thenReturn("John Doe");
        when(request.getParameter("username")).thenReturn("johndoe");
        when(request.getParameter("password")).thenReturn("securePass");
        when(request.getParameter("email")).thenReturn("john@example.com");
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(request.getParameter("licenseNumber")).thenReturn("ABC12345");

        // Simulate request dispatcher for forwarding
        when(request.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(requestDispatcher);

        // Simulate successful registration
        when(registrationService.registerDriver(any(User.class), eq("ABC12345"), eq("Available"))).thenReturn(true);

        // Call the method
        driverController.doPost(request, response);

        // Verify request attributes
        ArgumentCaptor<String> successMessageCaptor = ArgumentCaptor.forClass(String.class);
        verify(request).setAttribute(eq("success"), successMessageCaptor.capture());
        assertEquals("Registration successful! Please login to continue.", successMessageCaptor.getValue());

        // Verify forward to login page
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testRegisterDriver_Failure() throws ServletException, IOException {
        // Mock input parameters
        when(request.getParameter("action")).thenReturn("registerDriver");
        when(request.getParameter("name")).thenReturn("John Doe");
        when(request.getParameter("username")).thenReturn("johndoe");
        when(request.getParameter("password")).thenReturn("securePass");
        when(request.getParameter("email")).thenReturn("john@example.com");
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(request.getParameter("licenseNumber")).thenReturn("ABC12345");

        // Simulate request dispatcher for forwarding
        when(request.getRequestDispatcher("/WEB-INF/views/register.jsp")).thenReturn(requestDispatcher);

        // Simulate registration failure
        when(registrationService.registerDriver(any(User.class), eq("ABC12345"), eq("Available"))).thenReturn(false);

        // Call the method
        driverController.doPost(request, response);

        // Verify request attributes
        ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);
        verify(request).setAttribute(eq("error"), errorMessageCaptor.capture());
        assertEquals("Failed to create driver account. Please try again.", errorMessageCaptor.getValue());

        // Verify forward to register page
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testRegisterDriver_MissingFields() throws ServletException, IOException {
        // Mock missing parameters
        when(request.getParameter("action")).thenReturn("registerDriver");
        when(request.getParameter("name")).thenReturn(null); // Missing name
        when(request.getParameter("requestDispatcher")).thenReturn("/WEB-INF/views/register.jsp");

        // Simulate request dispatcher for forwarding
        when(request.getRequestDispatcher("/WEB-INF/views/register.jsp")).thenReturn(requestDispatcher);

        // Call the method
        driverController.doPost(request, response);

        // Verify request attributes
        ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);
        verify(request).setAttribute(eq("error"), errorMessageCaptor.capture());
        assertEquals("All fields are required.", errorMessageCaptor.getValue());

        // Verify forward to register page
        verify(requestDispatcher).forward(request, response);
    }
}
