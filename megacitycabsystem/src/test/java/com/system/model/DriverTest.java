package com.system.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DriverTest {

    private User user;

    @BeforeEach
    void setUp() {
        // Create a sample user using the full constructor.
        // Using a fixed LocalDateTime for predictable testing.
        user = new User("John Doe", "johndoe", "securePass", "driver", "john@example.com", "1234567890", 
                LocalDateTime.of(2025, 3, 9, 10, 0));
    }

    @Test
    void testConstructorAndGetters() {
        int driverId = 1;
        String licenseNumber = "ABC12345";
        String statusString = "Available";
        Driver driver = new Driver(driverId, user, licenseNumber, statusString);

        // Verify that the constructor correctly sets all fields.
        assertEquals(driverId, driver.getDriverId());
        assertEquals(user, driver.getUser());
        assertEquals(licenseNumber, driver.getLicenseNumber());
        assertEquals(Driver.DriverStatus.Available, driver.getStatus());
    }

    @Test
    void testSetters() {
        int driverId = 1;
        String licenseNumber = "ABC12345";
        String statusString = "Available";
        Driver driver = new Driver(driverId, user, licenseNumber, statusString);

        // Test setUser: change the associated user
        User newUser = new User("Jane Doe", "janedoe", "newPass", "driver", "jane@example.com", "0987654321", 
                LocalDateTime.of(2025, 3, 9, 11, 0));
        driver.setUser(newUser);
        assertEquals(newUser, driver.getUser());

        // Test setLicenseNumber: update the license number
        String newLicenseNumber = "XYZ98765";
        driver.setLicenseNumber(newLicenseNumber);
        assertEquals(newLicenseNumber, driver.getLicenseNumber());

        // Test setStatus: change the driver's status using the enum directly
        driver.setStatus(Driver.DriverStatus.Busy);
        assertEquals(Driver.DriverStatus.Busy, driver.getStatus());

        // Test setStatusFromString: update the status from a string value
        driver.setStatusFromString("Inactive");
        assertEquals(Driver.DriverStatus.Inactive, driver.getStatus());
    }

    @Test
    void testInvalidStatus() {
        int driverId = 1;
        String licenseNumber = "ABC12345";
        // Attempt to create a Driver with an invalid status string.
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Driver(driverId, user, licenseNumber, "InvalidStatus");
        });
        String expectedMessage = "No enum constant";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
