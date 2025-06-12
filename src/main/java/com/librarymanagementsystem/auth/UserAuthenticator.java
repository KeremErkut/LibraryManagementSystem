package com.librarymanagementsystem.auth;

import com.librarymanagementsystem.util.ConnectionManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64; // For Base64 encoding/decoding

/**
 * Handles user authentication, including password hashing, verification,
 * and session management. Interacts with the 'users' table in the database.
 */
public class UserAuthenticator {

    private final ConnectionManager connectionManager;
    private String currentUserRole; // Stores the role of the currently logged-in user

    /**
     * Constructor for UserAuthenticator.
     *
     * @param connectionManager The ConnectionManager instance to manage database connections.
     */
    public UserAuthenticator(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Authenticates a user based on username and password.
     *
     * @param username The username provided by the user.
     * @param password The plain text password provided by the user.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean authenticate(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean authenticated = false;

        String sql = "SELECT password_hash, role FROM users WHERE username = ?";

        try {
            conn = connectionManager.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    String userRole = rs.getString("role");

                    if (verifyPassword(password, storedHash)) {
                        this.currentUserRole = userRole; // Set the role of the authenticated user
                        authenticated = true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during authentication: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.closeResultSet(rs);
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }
        return authenticated;
    }

    /**
     * Hashes a plain text password using SHA-256.
     *
     * @param password The plain text password.
     * @return The SHA-256 hashed password encoded in Base64, or null if an error occurs.
     */
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash); // Encode to Base64 for safe storage
        } catch (NoSuchAlgorithmException e) {
            System.err.println("SHA-256 algorithm not found: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Verifies a plain text password against a stored hashed password.
     *
     * @param plainTextPassword The plain text password to verify.
     * @param hashedPassword    The stored hashed password.
     * @return true if passwords match, false otherwise.
     */
    public boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        String hashedInputPassword = hashPassword(plainTextPassword);
        return hashedInputPassword != null && hashedInputPassword.equals(hashedPassword);
    }

    /**
     * Creates a new user in the database.
     * This method is typically used by an admin or for initial setup.
     *
     * @param username The username for the new user.
     * @param password The plain text password for the new user.
     * @param role     The role of the new user (e.g., "ADMIN", "USER").
     * @return true if the user was successfully created, false otherwise.
     */
    public boolean createUser(String username, String password, String role) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        // Hash the password before storing it
        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) {
            System.err.println("Failed to hash password for user: " + username);
            return false;
        }

        String sql = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)";

        try {
            conn = connectionManager.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, hashedPassword);
                pstmt.setString(3, role);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating user '" + username + "': " + e.getMessage());
            // Check for duplicate entry error (e.g., if username is not unique)
            if (e.getSQLState().startsWith("23")) { // SQLState for integrity constraint violation
                System.err.println("Username already exists or other data integrity issue.");
            }
            e.printStackTrace();
        } finally {
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }
        return success;
    }

    /**
     * Retrieves the role of the currently authenticated user.
     * @return The role string (e.g., "ADMIN", "USER"), or null if no user is authenticated.
     */
    public String getCurrentUserRole() {
        return currentUserRole;
    }

    /**
     * Resets the current user role, effectively logging out the user.
     */
    public void logout() {
        this.currentUserRole = null; // Clear the current user's role
        System.out.println("User logged out.");
    }
}