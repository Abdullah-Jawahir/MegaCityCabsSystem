package com.system.dao;

import com.system.model.BillingSetting;
import com.system.utils.DBConnectionFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BillingSettingDAO {

    private static final Logger logger = Logger.getLogger(BillingSettingDAO.class.getName());

    // Load all settings into a Map for easy access
    public Map<String, Float> loadAllSettings() {
        Map<String, Float> settings = new HashMap<>();
        String sql = "SELECT setting_name, setting_value FROM billing_settings";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String settingName = resultSet.getString("setting_name");
                float settingValue = resultSet.getFloat("setting_value");
                settings.put(settingName, settingValue);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error loading billing settings: " + e.getMessage());
            e.printStackTrace(); // Consider a more robust error handling mechanism
        } finally {
            closeResources(resultSet, preparedStatement, connection);
        }

        return settings;
    }

    public BillingSetting getSettingByName(String settingName) {
        String sql = "SELECT setting_id, setting_value, description FROM billing_settings WHERE setting_name = ?";
        BillingSetting setting = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, settingName);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int settingId = resultSet.getInt("setting_id");
                float settingValue = resultSet.getFloat("setting_value");
                String description = resultSet.getString("description");
                setting = new BillingSetting(settingId, settingName, settingValue, description);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting billing setting by name: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(resultSet, preparedStatement, connection);
        }

        return setting;
    }


    public boolean updateSetting(BillingSetting setting) {
        String sql = "UPDATE billing_settings SET setting_value = ?, description = ? WHERE setting_id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setFloat(1, setting.getSettingValue());
            preparedStatement.setString(2, setting.getDescription());
            preparedStatement.setInt(3, setting.getSettingId());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating billing setting: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(null, preparedStatement, connection);
        }
    }


    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error closing ResultSet", e);
        }

        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error closing Statement", e);
        }

        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error closing Connection", e);
        }
    }
}