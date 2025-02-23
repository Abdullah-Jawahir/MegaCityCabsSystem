package com.system.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionFactory {

    // Method to get a database connection from the singleton DBConnection instance
    public static Connection getConnection() {
        Connection connection = null;
        connection = DBConnection.getInstance().getConnection(); // Get the connection
        return connection;
    }
    
    // Helper method to ensure proper connection handling
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close(); // Only close if it's not already closed
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
