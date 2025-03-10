package com.system.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.system.dao.BillDAO;
import com.system.dao.BillingSettingDAO;
import com.system.model.Bill;
import com.system.model.Booking;
import com.system.model.Customer;
import com.system.model.Driver;
import com.system.model.User;
import com.system.model.Vehicle;

public class BillServiceTest {

    @Mock
    private BillDAO billDAO;
    
    @Mock
    private BookingService bookingService;
    
    @Mock
    private BillingSettingDAO billingSettingDAO;
    
    @Mock
    private VehicleService vehicleService;
    
    @InjectMocks
    @Spy
    private BillService billService;
    
    // Test data
    private User testUser;
    private Vehicle testVehicle;
    private Customer testCustomer;
    private Driver testDriver;
    private Booking testBooking;
    private String bookingId;
    private float ratePerKm = 2.5f;
    private float testDistance = 10.0f;
    private float taxRate = 0.10f;
    private float discountRate = 0.05f;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        // Setup test data
        bookingId = "BOOK" + UUID.randomUUID().toString().substring(0, 6);
        
        // Setup test user
        testUser = new User("Test User", "testuser", "password", "CUSTOMER", 
                "test@example.com", "1234567890", LocalDateTime.now());
        testUser.setId(1);
        
        // Setup test vehicle
        testVehicle = new Vehicle(1, "ABC123", "Toyota Camry", "Active", 3, LocalDateTime.now(), ratePerKm);
        
        // Setup test customer
        testCustomer = new Customer(1, testUser, "REG123", "123 Test St", "NIC12345");
        
        // Setup test driver
        User driverUser = new User("Driver Name", "driver1", "password", "DRIVER", 
                "driver@example.com", "9876543210", LocalDateTime.now());
        driverUser.setId(2);
        testDriver = new Driver(1, driverUser, "DL12345", "Available");
        
        // Setup test booking
        testBooking = new Booking(
                bookingId, 
                LocalDateTime.now(), 
                "Pickup Location",
                "Destination",
                testDistance,
                "pending",
                testDriver,
                testVehicle,
                testCustomer
        );
        
        // Setup billing settings
        Map<String, Float> billingSettings = new HashMap<>();
        billingSettings.put("tax_rate", taxRate);
        billingSettings.put("default_discount_rate", discountRate);
        when(billingSettingDAO.loadAllSettings()).thenReturn(billingSettings);
        
        // Call loadBillingSettings directly since the constructor in the real class would do this
        doReturn(billingSettings).when(billingSettingDAO).loadAllSettings();
        billService.reloadBillingSettings();
    }
    
    @Test
    public void testGenerateBill_Success() {
        // Arrange
        when(bookingService.getBookingById(bookingId)).thenReturn(testBooking);
        when(billDAO.createBill(any(Bill.class))).thenReturn(true);
        
        // Calculate expected amounts
        float expectedBaseAmount = testDistance * ratePerKm; // 10 * 2.5 = 25.0
        float expectedTaxAmount = expectedBaseAmount * taxRate; // 25 * 0.1 = 2.5
        float expectedTotalBeforeDiscount = expectedBaseAmount + expectedTaxAmount; // 25 + 2.5 = 27.5
        float expectedDiscountAmount = expectedTotalBeforeDiscount * discountRate; // 27.5 * 0.05 = 1.375
        float expectedTotalAmount = expectedTotalBeforeDiscount - expectedDiscountAmount; // 27.5 - 1.375 = 26.125
        
        // Mock calls for calculating amounts
        doReturn(expectedBaseAmount).when(billService).calculateBaseAmount(testBooking);
        doReturn(expectedTaxAmount).when(billService).calculateTaxAmount(expectedBaseAmount);
        doReturn(expectedDiscountAmount).when(billService).calculateDiscountAmount(expectedTotalBeforeDiscount);
        
        // Act
        boolean result = billService.generateBill(bookingId, testUser);
        
        // Assert
        assertTrue(result);
        verify(bookingService).getBookingById(bookingId);
        verify(billService).calculateBaseAmount(testBooking);
        verify(billService).calculateTaxAmount(expectedBaseAmount);
        verify(billService).calculateDiscountAmount(expectedTotalBeforeDiscount);
        
        // Verify bill creation with correct values
        verify(billDAO).createBill(argThat(bill -> 
            bill.getBooking().equals(testBooking) &&
            Math.abs(bill.getBaseAmount() - expectedBaseAmount) < 0.001 &&
            Math.abs(bill.getTaxAmount() - expectedTaxAmount) < 0.001 &&
            Math.abs(bill.getDiscountAmount() - expectedDiscountAmount) < 0.001 &&
            Math.abs(bill.getTotalAmount() - expectedTotalAmount) < 0.001 &&
            bill.getStatus().equals("pending") &&
            bill.getGeneratedBy().equals(testUser)
        ));
    }
    
    @Test
    public void testGenerateBill_NullBooking() {
        // Arrange
        when(bookingService.getBookingById(bookingId)).thenReturn(null);
        
        // Act
        boolean result = billService.generateBill(bookingId, testUser);
        
        // Assert
        assertFalse(result);
        verify(bookingService).getBookingById(bookingId);
        verify(billDAO, never()).createBill(any(Bill.class));
    }
    
    @Test
    public void testGenerateBill_NullUser() {
        // Arrange
        when(bookingService.getBookingById(bookingId)).thenReturn(testBooking);
        
        // Act
        boolean result = billService.generateBill(bookingId, null);
        
        // Assert
        assertFalse(result);
        verify(bookingService).getBookingById(bookingId);
        verify(billDAO, never()).createBill(any(Bill.class));
    }
    
    @Test
    public void testGenerateBill_BillCreationFails() {
        // Arrange
        when(bookingService.getBookingById(bookingId)).thenReturn(testBooking);
        when(billDAO.createBill(any(Bill.class))).thenReturn(false);
        
        // Act
        boolean result = billService.generateBill(bookingId, testUser);
        
        // Assert
        assertFalse(result);
        verify(bookingService).getBookingById(bookingId);
        verify(billDAO).createBill(any(Bill.class));
    }
    
    @Test
    public void testGenerateBill_ExceptionThrown() {
        // Arrange
        when(bookingService.getBookingById(bookingId)).thenThrow(new RuntimeException("Test exception"));
        
        // Act
        boolean result = billService.generateBill(bookingId, testUser);
        
        // Assert
        assertFalse(result);
        verify(bookingService).getBookingById(bookingId);
        verify(billDAO, never()).createBill(any(Bill.class));
    }
    
    @Test
    public void testCalculateBaseAmount_WithVehicle() {
        // Act
        float result = billService.calculateBaseAmount(testBooking);
        
        // Assert
        float expected = testDistance * ratePerKm;
        assertEquals(expected, result, 0.001);
    }
    
    @Test
    public void testCalculateBaseAmount_NoVehicle() {
        // Arrange
        testBooking = new Booking(
                bookingId, 
                LocalDateTime.now(), 
                "Pickup Location",
                "Destination",
                testDistance,
                "pending",
                testDriver,
                null, // No vehicle
                testCustomer
        );
        
        // Act
        float result = billService.calculateBaseAmount(testBooking);
        
        // Assert
        float expected = testDistance * 5.0f; // Default rate
        assertEquals(expected, result, 0.001);
    }
    
    @Test
    public void testCalculateTaxAmount() {
        // Arrange
        float baseAmount = 100.0f;
        
        // Act
        float result = billService.calculateTaxAmount(baseAmount);
        
        // Assert
        float expected = baseAmount * taxRate;
        assertEquals(expected, result, 0.001);
    }
    
    @Test
    public void testCalculateDiscountAmount() {
        // Arrange
        float amountBeforeDiscount = 100.0f;
        
        // Act
        float result = billService.calculateDiscountAmount(amountBeforeDiscount);
        
        // Assert
        float expected = amountBeforeDiscount * discountRate;
        assertEquals(expected, result, 0.001);
    }
    
    @Test
    public void testRecalculateBill() {
        // Arrange
        Bill existingBill = new Bill(
                "BILL123",
                testBooking,
                25.0f,
                2.5f,
                1.375f,
                26.125f,
                "pending",
                testUser
        );
        
        // Create a modified booking with different distance
        float newDistance = 20.0f;
        Booking updatedBooking = new Booking(
                bookingId, 
                LocalDateTime.now(), 
                "New Pickup",
                "New Destination",
                newDistance,
                "pending",
                testDriver,
                testVehicle,
                testCustomer
        );
        
        // Calculate expected amounts
        float expectedBaseAmount = newDistance * ratePerKm; // 20 * 2.5 = 50.0
        float expectedTaxAmount = expectedBaseAmount * taxRate; // 50 * 0.1 = 5.0
        float expectedTotalBeforeDiscount = expectedBaseAmount + expectedTaxAmount; // 50 + 5 = 55.0
        float expectedDiscountAmount = expectedTotalBeforeDiscount * discountRate; // 55 * 0.05 = 2.75
        float expectedTotalAmount = expectedTotalBeforeDiscount - expectedDiscountAmount; // 55 - 2.75 = 52.25
        
        // Mock the calculation methods
        doReturn(expectedBaseAmount).when(billService).calculateBaseAmount(updatedBooking);
        doReturn(expectedTaxAmount).when(billService).calculateTaxAmount(expectedBaseAmount);
        doReturn(expectedDiscountAmount).when(billService).calculateDiscountAmount(expectedTotalBeforeDiscount);
        
        // Act
        Bill result = billService.recalculateBill(existingBill, updatedBooking);
        
        // Assert
        assertEquals(updatedBooking, result.getBooking());
        assertEquals(expectedBaseAmount, result.getBaseAmount(), 0.001);
        assertEquals(expectedTaxAmount, result.getTaxAmount(), 0.001);
        assertEquals(expectedDiscountAmount, result.getDiscountAmount(), 0.001);
        assertEquals(expectedTotalAmount, result.getTotalAmount(), 0.001);
        // Status and generated by should remain unchanged
        assertEquals("pending", result.getStatus());
        assertEquals(testUser, result.getGeneratedBy());
    }
}