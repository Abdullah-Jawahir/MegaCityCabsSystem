package com.system.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionTest {

    @Test
    public void testGetInstance_returnsSameInstance() {
        DBConnection instance1 = DBConnection.getInstance();
        DBConnection instance2 = DBConnection.getInstance();
        assertSame(instance1, instance2, "Should return the same instance");
    }

    @Test
    public void testGetConnection_returnsNonNullConnection() {
        DBConnection dbConnection = DBConnection.getInstance();
        Connection connection = dbConnection.getConnection();
        assertNotNull(connection, "Connection should not be null");
    }

    @Test
    public void testGetConnection_connectionIsOpen() throws SQLException {
        DBConnection dbConnection = DBConnection.getInstance();
        Connection connection = dbConnection.getConnection();
        assertFalse(connection.isClosed(), "Connection should be open");
    }

    @Test
    public void testGetInstance_loadsDriver() {
        assertDoesNotThrow(DBConnection::getInstance, "Should not throw exception when driver is loaded");
    }
}