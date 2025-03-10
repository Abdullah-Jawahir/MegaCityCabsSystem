package com.system.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.system.dao.DriverDAO;
import com.system.model.Driver;
import com.system.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class DriverServiceTest {

    private DriverService driverService;

    @Mock
    private DriverDAO driverDAO;

    @Mock
    private Connection connection;

    private User user;
    private Driver driver;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        driverService = new DriverService();
        
        // Inject the mocked driverDAO into driverService using reflection
        Field field = DriverService.class.getDeclaredField("driverDAO");
        field.setAccessible(true);
        field.set(driverService, driverDAO);

        // Set up a sample User and Driver for testing.
        user = new User(
                "John Doe", 
                "johndoe", 
                "securePass", 
                "driver", 
                "john@example.com", 
                "1234567890", 
                LocalDateTime.now()
        );
        driver = new Driver(1, user, "ABC12345", "Available");
    }

    @Test
    void testAddDriver_Success() throws SQLException {
        // Stub DAO to indicate driver is added successfully
        when(driverDAO.addDriver(connection, driver)).thenReturn(true);

        boolean result = driverService.addDriver(connection, driver);
        assertTrue(result);
        verify(driverDAO).addDriver(connection, driver);
    }

    @Test
    void testAddDriver_Failure() throws SQLException {
        // Stub DAO to simulate failure (returns false)
        when(driverDAO.addDriver(connection, driver)).thenReturn(false);

        SQLException exception = assertThrows(SQLException.class, () -> {
            driverService.addDriver(connection, driver);
        });
        assertEquals("Failed to add driver.", exception.getMessage());
        verify(driverDAO).addDriver(connection, driver);
    }

    @Test
    void testGetDriverById() throws Exception {
        // Stub DAO to return the driver for a given ID
        when(driverDAO.getDriverById(1)).thenReturn(driver);

        Driver result = driverService.getDriverById(1);
        assertEquals(driver, result);
        verify(driverDAO).getDriverById(1);
    }

    @Test
    void testGetAllDrivers() throws SQLException {
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        when(driverDAO.getAllDrivers()).thenReturn(drivers);

        List<Driver> result = driverService.getAllDrivers();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(driver, result.get(0));
        verify(driverDAO).getAllDrivers();
    }

    @Test
    void testGetDriverByVehicleId() throws SQLException {
        // Stub DAO to return a driver when searching by vehicle ID
        when(driverDAO.getDriverByVehicleId(10)).thenReturn(driver);

        Driver result = driverService.getDriverByVehicleId(10);
        assertEquals(driver, result);
        verify(driverDAO).getDriverByVehicleId(10);
    }

    @Test
    void testGetAvailableDrivers() throws SQLException {
        List<Driver> availableDrivers = new ArrayList<>();
        availableDrivers.add(driver);
        when(driverDAO.getAvailableDrivers()).thenReturn(availableDrivers);

        List<Driver> result = driverService.getAvailableDrivers();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(driver, result.get(0));
        verify(driverDAO).getAvailableDrivers();
    }

    @Test
    void testGetAvailableDrivers_ReturnsEmptyListWhenNull() throws SQLException {
        // Simulate DAO returning null, the service should then return an empty list
        when(driverDAO.getAvailableDrivers()).thenReturn(null);
        List<Driver> result = driverService.getAvailableDrivers();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(driverDAO).getAvailableDrivers();
    }

    @Test
    void testGetAvailableDriverExcluding_Found() throws SQLException {
        // Create another driver and return a list with both drivers
        Driver anotherDriver = new Driver(2, user, "XYZ98765", "Available");
        List<Driver> availableDrivers = Arrays.asList(driver, anotherDriver);
        when(driverDAO.getAvailableDrivers()).thenReturn(availableDrivers);

        // Excluding driver with ID 1 should return the second driver
        Driver result = driverService.getAvailableDriverExcluding(1);
        assertNotNull(result);
        assertEquals(2, result.getDriverId());
        verify(driverDAO).getAvailableDrivers();
    }

    @Test
    void testGetAvailableDriverExcluding_NotFound() throws SQLException {
        // Only one driver in the list, and its ID is the one to exclude.
        List<Driver> availableDrivers = Collections.singletonList(driver);
        when(driverDAO.getAvailableDrivers()).thenReturn(availableDrivers);

        Driver result = driverService.getAvailableDriverExcluding(1);
        assertNull(result);
        verify(driverDAO).getAvailableDrivers();
    }

    @Test
    void testUpdateDriver_Success() throws Exception {
        // Stub DAO update to succeed
        when(driverDAO.updateDriver(driver)).thenReturn(true);

        boolean result = driverService.updateDriver(driver);
        assertTrue(result);
        verify(driverDAO).updateDriver(driver);
    }

    @Test
    void testUpdateDriver_Failure() throws Exception {
        // Stub DAO update to fail (return false)
        when(driverDAO.updateDriver(driver)).thenReturn(false);

        boolean result = driverService.updateDriver(driver);
        // When update fails, our service returns false
        assertFalse(result);
        verify(driverDAO).updateDriver(driver);
    }

    @Test
    void testDeleteDriver() throws Exception {
        // Stub DAO delete to succeed
        when(driverDAO.deleteDriver(1)).thenReturn(true);

        boolean result = driverService.deleteDriver(1);
        assertTrue(result);
        verify(driverDAO).deleteDriver(1);
    }

    @Test
    void testUpdateDriverStatus() throws Exception {
        // Stub DAO updateDriverStatus to succeed
        when(driverDAO.updateDriverStatus(1, "Busy")).thenReturn(true);

        boolean result = driverService.updateDriverStatus(1, "Busy");
        assertTrue(result);
        verify(driverDAO).updateDriverStatus(1, "Busy");
    }

    @Test
    void testReassignBookings() throws SQLException {
        // Stub DAO reassignBookings to succeed
        when(driverDAO.reassignBookings(1, 2)).thenReturn(true);

        boolean result = driverService.reassignBookings(1, 2);
        assertTrue(result);
        verify(driverDAO).reassignBookings(1, 2);
    }

    @Test
    void testReassignVehicles() throws SQLException {
        // Stub DAO reassignVehicles to succeed
        when(driverDAO.reassignVehicles(1, 2)).thenReturn(true);

        boolean result = driverService.reassignVehicles(1, 2);
        assertTrue(result);
        verify(driverDAO).reassignVehicles(1, 2);
    }

    @Test
    void testGetTopPerformingDrivers() {
        List<Map<String, Object>> topDrivers = new ArrayList<>();
        Map<String, Object> driverMap = new HashMap<>();
        driverMap.put("driverId", 1);
        driverMap.put("performance", 95);
        topDrivers.add(driverMap);

        when(driverDAO.getTopPerformingDrivers(5)).thenReturn(topDrivers);

        List<Map<String, Object>> result = driverService.getTopPerformingDrivers(5);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).get("driverId"));
        assertEquals(95, result.get(0).get("performance"));
        verify(driverDAO).getTopPerformingDrivers(5);
    }
}
