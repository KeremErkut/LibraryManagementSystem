package com.librarymanagementsystem;

import com.librarymanagementsystem.auth.UserAuthenticator;
import com.librarymanagementsystem.dao.MySQLBookDAO;
import com.librarymanagementsystem.dao.MySQLCategoryDAO;
import com.librarymanagementsystem.util.ConnectionManager;
import com.librarymanagementsystem.view.BookManagementView;
import com.librarymanagementsystem.view.LoginView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

/**
 * Main class for the Library Management System application.
 * Initializes the application components and starts the UI.
 */
public class Main {

    // Database connection details
    // IMPORTANT: Replace these with your actual MySQL database credentials
    private static final String DB_URL = "";
    private static final String DB_USERNAME = "library_user"; // Your MySQL username
    private static final String DB_PASSWORD = ""; // Your MySQL password

    public static void main(String[] args) {
        // Step 1: Initialize ConnectionManager
        ConnectionManager connectionManager = new ConnectionManager(DB_URL, DB_USERNAME, DB_PASSWORD);

        // Test database connection (optional, but good for debugging)
        if (connectionManager.getConnection() == null) {
            System.err.println("Failed to connect to the database. Exiting application.");
            JOptionPane.showMessageDialog(null, "Failed to connect to the database. Please check your connection details and MySQL server.", "Database Connection Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit if database connection fails
        } else {
            System.out.println("Successfully connected to the database.");
            // Immediately close the test connection. Note: `getConnection()` creates a new connection,
            // so we are closing the one created specifically for the test.
            // The `ConnectionManager` itself manages connections for DAOs later.
            try (java.sql.Connection testConn = connectionManager.getConnection()) { // Use try-with-resources for auto-closing
                if (testConn != null) {
                    // Connection was successful, no need to close it explicitly here as try-with-resources handles it.
                }
            } catch (SQLException e) {
                // This catch block is mostly for the `try (java.sql.Connection testConn = connectionManager.getConnection())`
                // in case getConnection() itself throws an exception or closing fails.
                System.err.println("Error closing test connection: " + e.getMessage());
            }
        }


        // Step 2: Initialize DAO objects
        MySQLBookDAO bookDAO = new MySQLBookDAO(connectionManager);
        MySQLCategoryDAO categoryDAO = new MySQLCategoryDAO(connectionManager);

        // Step 3: Initialize UserAuthenticator
        UserAuthenticator authenticator = new UserAuthenticator(connectionManager);

        // Step 4: Check for an initial admin user and create if not exists
        // This is a common practice for initial setup or first run
        initializeAdminUser(authenticator, connectionManager);

        // Step 5: Start the Login View on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView(authenticator, bookDAO, categoryDAO); // UPDATED CALL;
            loginView.setVisible(true);

            // Add an action listener to handle successful login in LoginView
            // When LoginView successfully authenticates, it will close itself.
            // We need to pass the logic to open BookManagementView after login.
            // A simple way is to pass a callback or use a property change listener,
            // but for this simple example, we can extend LoginView's success behavior.
            // We'll modify LoginView to directly open BookManagementView upon success
            // and pass the necessary DAOs.

            // Modify LoginView's attemptLogin method to call a method on Main or
            // directly instantiate and show BookManagementView.
            // For now, let's update LoginView's attemptLogin to directly call BookManagementView.
            // We need to pass the DAO objects to LoginView so it can pass them to BookManagementView.
            // Let's refine the LoginView constructor for this.
        });
    }

    /**
     * Initializes an admin user if no users exist in the database.
     * This is useful for the very first run of the application.
     *
     * @param authenticator The UserAuthenticator instance.
     */
    private static void initializeAdminUser(UserAuthenticator authenticator, ConnectionManager connectionManager) {
        // Check if any users exist in the database
        // A simple way is to try to authenticate a non-existent user, or query directly.
        // For simplicity, let's assume if 'admin' user doesn't exist, we create it.
        // In a real app, you might have a dedicated method in UserDAO to check user count.

        // Temporarily, we'll try to find an admin.
        // For a more robust check, you might add a method to UserAuthenticator
        // like `doesUserExist(username)` or `countUsers()`.
        // For now, let's rely on `authenticate` to indirectly check existence.
        // If "admin" user is NOT found, then we create it.
        // This is a simplified approach, a real application might need more robust user management.

        // A better check: directly query the users table for count
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean adminExists = false;

        try {
            conn = connectionManager.getConnection(); // Access connectionManager from authenticator
            if (conn != null) {
                pstmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = 'admin'");
                rs = pstmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    adminExists = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking for admin user existence: " + e.getMessage());
            // This might happen if the 'users' table doesn't exist yet, which is fine for first run.
        } finally {
            connectionManager.closeResultSet(rs);
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }

        if (!adminExists) {
            // Create a default admin user if one does not exist
            String defaultAdminUsername = "admin";
            String defaultAdminPassword = "adminpassword"; // Choose a strong default password

            if (authenticator.createUser(defaultAdminUsername, defaultAdminPassword, "ADMIN")) {
                System.out.println("Default ADMIN user '" + defaultAdminUsername + "' created successfully.");
                JOptionPane.showMessageDialog(null,
                        "Initial ADMIN user created:\nUsername: " + defaultAdminUsername + "\nPassword: " + defaultAdminPassword +
                                "\nPlease change this password after first login for security.",
                        "Initial Setup", JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.err.println("Failed to create default ADMIN user.");
                JOptionPane.showMessageDialog(null, "Failed to create default ADMIN user.", "Initial Setup Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Admin user already exists. Skipping initial admin creation.");
        }
    }
}