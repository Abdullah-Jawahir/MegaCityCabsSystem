package com.system.dao;

import com.system.model.Driver;
import com.system.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DriverDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    private DriverDAO driverDAO;

    @Before
    public void setUp() throws SQLException {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);
        
        // Set up mock behavior
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        
        // Create DriverDAO instance
        driverDAO = new DriverDAO();
    }

    @Test
    public void testAddDriver_Success() throws SQLException {
        // Setup
        User user = new User("John Doe", "johndoe", "password", "driver", 
                            "john@example.com", "1234567890", null);
        user.setId(1);
        
        Driver driver = new Driver(0, user, "DL123456", "Available");
        
        // Mock behavior for successful update
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        
        // Test
        boolean result = driverDAO.addDriver(mockConnection, driver);
        
        // Verify
        assertTrue("Method should return true for successful insert", result);
        
        // Verify PreparedStatement was set up correctly
        verify(mockPreparedStatement).setInt(1, user.getId());
        verify(mockPreparedStatement).setString(2, driver.getLicenseNumber());
        verify(mockPreparedStatement).setString(3, driver.getStatus().toString());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testAddDriver_Failure() throws SQLException {
        // Setup
        User user = new User("John Doe", "johndoe", "password", "driver", 
                           "john@example.com", "1234567890", null);
        user.setId(1);
        
        Driver driver = new Driver(0, user, "DL123456", "Available");
        
        // Mock behavior for failed update
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        
        // Test
        boolean result = driverDAO.addDriver(mockConnection, driver);
        
        // Verify
        assertFalse("Method should return false for failed insert", result);
    }

    @Test
    public void testAddDriver_WithDifferentStatus() throws SQLException {
        // Setup - testing with different status values
        User user = new User("Jane Doe", "janedoe", "password", "driver", 
                            "jane@example.com", "9876543210", null);
        user.setId(2);
        
        // Test each enum value
        for (Driver.DriverStatus status : Driver.DriverStatus.values()) {
            Driver driver = new Driver(0, user, "DL789012", status.toString());
            
            // Reset and configure mock
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);
            
            // Test
            boolean result = driverDAO.addDriver(mockConnection, driver);
            
            // Verify
            assertTrue("Method should return true for successful insert with status: " + status, result);
            verify(mockPreparedStatement).setString(3, status.toString());
        }
    }

    @Test(expected = SQLException.class)
    public void testAddDriver_SQLException() throws SQLException {
        // Setup
        User user = new User("John Doe", "johndoe", "password", "driver", 
                           "john@example.com", "1234567890", null);
        user.setId(1);
        
        Driver driver = new Driver(0, user, "DL123456", "Available");
        
        // Mock SQLException
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));
        
        // Test - should throw SQLException
        driverDAO.addDriver(mockConnection, driver);
    }
}