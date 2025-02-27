package com.system.dao;

import com.system.model.User;
import com.system.utils.DBConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {

    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());
    private final DBConnection dbConnection;

    public UserDAO() {
        this.dbConnection = DBConnection.getInstance();
    }

    public int addUser(Connection connection, User user) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "INSERT INTO user (name, username, password, role, email, phone, last_login) VALUES (?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPhone());
            statement.setTimestamp(7, user.getLastLogin() != null ? Timestamp.valueOf(user.getLastLogin()) : null);

            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                int generatedId = resultSet.getInt(1);
                user.setId(generatedId);
                return generatedId;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding user", e);
            throw e;
        } finally {
            // Do not close the connection here since it's managed externally
            closeResources(resultSet, statement, null);
        }
        return -1;
    }


    public User getUserById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = dbConnection.getConnection();
            String query = "SELECT * FROM user WHERE user_id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = createUserFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting user by ID: " + id, e);
        } finally {
            closeResources(resultSet, statement, connection);
        }
        return user;
    }

    public void updateUser(User user) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dbConnection.getConnection();
            String query = "UPDATE user SET name = ?, username = ?, password = ?, role = ?, email = ?, phone = ?, last_login = ? WHERE user_id = ?";
            statement = connection.prepareStatement(query);

            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPhone());
            statement.setTimestamp(7, user.getLastLogin() != null ? Timestamp.valueOf(user.getLastLogin()) : null);
            statement.setInt(8, user.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating user: " + user.getId(), e);
        } finally {
            closeResources(null, statement, connection);
        }
    }

    public void deleteUser(int id) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dbConnection.getConnection();
            String query = "DELETE FROM user WHERE user_id = ?";
            statement = connection.prepareStatement(query);

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting user: " + id, e);
        } finally {
            closeResources(null, statement, connection);
        }
    }

    public List<User> getAllUsers() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<>();

        try {
            connection = dbConnection.getConnection();
            String query = "SELECT * FROM user";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                users.add(createUserFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting all users", e);
        } finally {
            closeResources(resultSet, statement, connection);
        }
        return users;
    }

    public User getAuthenticatedUserByEmail(String email) {
        return getUserByEmail(email);
    }

    public int getUserIdByEmail(String email) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            String query = "SELECT user_id FROM user WHERE email = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting user ID by email: " + email, e);
            throw e;
        } finally {
            closeResources(resultSet, statement, connection);
        }
        return -1;
    }

    public User getUserByEmail(String email) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = dbConnection.getConnection();
            String query = "SELECT * FROM user WHERE email = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = createUserFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting user by email: " + email, e);
        } finally {
            closeResources(resultSet, statement, connection);
        }
        return user;
    }

    // New method to get a user by username
    public User getUserByUsername(String username) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = dbConnection.getConnection();
            String query = "SELECT * FROM user WHERE username = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = createUserFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting user by username: " + username, e);
        } finally {
            closeResources(resultSet, statement, connection);
        }
        return user;
    }

    private User createUserFromResultSet(ResultSet rs) throws SQLException {
        Timestamp lastLoginTimestamp = rs.getTimestamp("last_login");
        LocalDateTime lastLogin = (lastLoginTimestamp != null) ? lastLoginTimestamp.toLocalDateTime() : null;

        User user = new User(
                rs.getString("name"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("email"),
                rs.getString("phone"),
                lastLogin
        );
        user.setId(rs.getInt("user_id"));
        return user;
    }

    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error closing ResultSet", e);
        }

        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error closing Statement", e);
        }

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error closing Connection", e);
        }
    }
}