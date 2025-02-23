package com.system.dao;

import com.system.model.Admin;
import com.system.utils.DBConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    private Connection connection;

    public AdminDAO() {
        this.connection = DBConnectionFactory.getConnection();
    }

    // Retrieve all admins
    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        String query = "SELECT * FROM admin";  // Singular table name

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Admin admin = new Admin(
                    rs.getInt("admin_id"), // admin_id is the primary key
                    rs.getInt("user_id")    // user_id is the foreign key
                );
                admins.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
    }

    // Retrieve admin by ID
    public Admin getAdminById(int adminId) {
        String query = "SELECT * FROM admin WHERE admin_id = ?";  // Singular table name
        Admin admin = null;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, adminId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    admin = new Admin(
                        rs.getInt("admin_id"), // admin_id is the primary key
                        rs.getInt("user_id")    // user_id is the foreign key
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    // Create new admin
    public boolean createAdmin(Admin admin) {
        String query = "INSERT INTO admin (user_id) VALUES (?)";  // Singular table name
        boolean isSuccess = false;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, admin.getUserId()); // Set the user_id, since admin_id is auto-incremented

            int rowsAffected = pstmt.executeUpdate();
            isSuccess = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    // Delete admin by ID
    public boolean deleteAdmin(int adminId) {
        String query = "DELETE FROM admin WHERE admin_id = ?";  // Singular table name
        boolean isSuccess = false;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, adminId);
            int rowsAffected = pstmt.executeUpdate();
            isSuccess = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }
}
