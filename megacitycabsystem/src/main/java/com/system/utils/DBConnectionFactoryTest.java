package com.system.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionFactoryTest {

    @Test
    public void testGetConnection_returnsNonNullConnection() {
        Connection connection = DBConnectionFactory.getConnection();
        assertNotNull(connection, "Connection should not be null");
    }

    @Test
    public void testGetConnection_connectionIsOpen() throws SQLException {
        Connection connection = DBConnectionFactory.getConnection();
        assertFalse(connection.isClosed(), "Connection should be open");
    }

    @Test
    public void testGetConnection_returnsSameConnection() {
        Connection connection1 = DBConnectionFactory.getConnection();
        Connection connection2 = DBConnection.getInstance().getConnection();
        assertSame(connection1, connection2, "Factory should return the same connection as singleton");
    }
}