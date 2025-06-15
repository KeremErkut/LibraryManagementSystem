package com.librarymanagementsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages database connections for the Library Management System.
 * Handles opening, closing, and executing basic database operations.
 */
public class ConnectionManager {
    // Database connection details
    private final String DB_URL;
    private final String DB_USERNAME;
    private final String DB_PASSWORD;

    /**
     * Constructor for ConnectionManager.
     * Initializes database connection parameters.
     *
     * @param url The database URL.
     * @param user The database username.
     * @param pass The database password.
     */
    public ConnectionManager(String url, String user, String pass) {
        this.DB_URL = "jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC";
        this.DB_USERNAME = "root";
        this.DB_PASSWORD = "Kerem123+";

        // Optional: Load the JDBC driver once when the manager is initialized
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Make sure it's included in your project dependencies.");
            e.printStackTrace();
        }
    }

    /**
     * Establishes and returns a new database connection.
     *
     * @return A valid database Connection object, or null if connection fails.
     */
    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            // System.out.println("Database connection established successfully."); // For debugging
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Closes a given database connection.
     *
     * @param conn The Connection object to be closed.
     */
    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                // System.out.println("Database connection closed."); // For debugging
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes a given Statement object.
     *
     * @param stmt The Statement object to be closed.
     */
    public void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
                // System.out.println("Statement closed."); // For debugging
            } catch (SQLException e) {
                System.err.println("Error closing Statement: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes a given ResultSet object.
     *
     * @param rs The ResultSet object to be closed.
     */
    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                // System.out.println("ResultSet closed."); // For debugging
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Executes an SQL UPDATE, INSERT, or DELETE statement.
     * This method is provided as a utility but it's generally better to use PreparedStatement
     * in DAO classes for safety and performance.
     *
     * @param sql The SQL string to be executed.
     * @return The number of rows affected, or 0 if no rows affected.
     */
    public int executeUpdate(String sql) {
        Connection conn = null;
        Statement stmt = null;
        int rowsAffected = 0;
        try {
            conn = getConnection();
            if (conn != null) {
                stmt = conn.createStatement();
                rowsAffected = stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            System.err.println("SQL update error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeStatement(stmt);
            closeConnection(conn);
        }
        return rowsAffected;
    }

    /**
     * Executes an SQL SELECT query.
     * This method is provided as a utility but it's generally better to use PreparedStatement
     * in DAO classes for safety and performance.
     *
     * @param sql The SQL query string to be executed.
     * @return A ResultSet containing the data, or null if an error occurs.
     */
    public ResultSet executeQuery(String sql) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
            }
        } catch (SQLException e) {
            System.err.println("SQL query error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Note: Connection and Statement are NOT closed here because ResultSet might still be in use.
            // They should be closed by the calling DAO method.
        }
        return rs;
    }
}