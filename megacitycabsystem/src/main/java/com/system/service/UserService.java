package com.system.service;

import com.system.dao.UserDAO;
import com.system.model.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    //  Add a user using an existing connection (for transactional use)
    public int addUser(Connection connection, User user) throws SQLException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        // Note: We assume caller has already performed any existence checks.
        return userDAO.addUser(connection, user);
    }

    // Get a user by ID
    public User getUserById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + id);
        }
        User user = userDAO.getUserById(id);
        if (user == null) {
            logger.log(Level.INFO, "No user found with ID: {0}", id);
        }
        return user;
    }

    // Get a user by email
    public User getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            logger.log(Level.INFO, "No user found with email: {0}", email);
        }
        return user;
    }

    // Update user details
    public boolean updateUser(User user) {
        try {
            if (user == null || user.getId() <= 0) {
                throw new IllegalArgumentException("Invalid user data for update");
            }
            userDAO.updateUser(user);
            logger.log(Level.INFO, "Successfully updated user: {0}", user.getName());
            return true;
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Invalid user data provided for update", e);
            throw e;
        }
    }

    // Delete a user
    public boolean deleteUser(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid user ID for deletion: " + id);
        }
        userDAO.deleteUser(id);
        logger.log(Level.INFO, "Successfully deleted user with ID: {0}", id);
        return true;
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> users = userDAO.getAllUsers();
        logger.log(Level.INFO, "Retrieved {0} users", users.size());
        return users;
    }

    // Authenticate user by username
    public User authenticateUser(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be empty");
        }

        User user = userDAO.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            logger.log(Level.INFO, "Successful authentication for user: {0}", username);
            return user;
        }

        logger.log(Level.WARNING, "Failed authentication attempt for username: {0}", username);
        return null;
    }

    // Get userId by email
    public int getUserIdByEmail(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }

            int userId = userDAO.getUserIdByEmail(email);
            if (userId == -1) {
                logger.log(Level.INFO, "No user found with email: {0}", email);
            }
            return userId;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving user ID for email: " + email, e);
            throw new RuntimeException("Failed to retrieve user ID", e);
        }
    }
}