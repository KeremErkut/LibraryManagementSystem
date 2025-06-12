package com.librarymanagementsystem.dao;

import com.librarymanagementsystem.model.Category;
import com.librarymanagementsystem.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Used for generated keys for addCategory
import java.util.ArrayList;
import java.util.List;

/**
 * MySQL implementation of the CategoryDAO interface.
 * Handles database operations for Category objects using JDBC.
 */
public class MySQLCategoryDAO implements CategoryDAO {

    private final ConnectionManager connectionManager;

    /**
     * Constructor for MySQLCategoryDAO.
     *
     * @param connectionManager The ConnectionManager instance to manage database connections.
     */
    public MySQLCategoryDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public boolean addCategory(Category category) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        boolean success = false;

        String sql = "INSERT INTO categories (name) VALUES (?)";

        try {
            conn = connectionManager.getConnection();
            if (conn != null) {
                // Use RETURN_GENERATED_KEYS to get the auto-generated ID
                pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, category.getName());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    generatedKeys = pstmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        category.setId(generatedKeys.getInt(1)); // Set the ID back to the category object
                        success = true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding category: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.closeResultSet(generatedKeys);
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }
        return success;
    }

    @Override
    public boolean updateCategory(Category category) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        String sql = "UPDATE categories SET name = ? WHERE id = ?";

        try {
            conn = connectionManager.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, category.getName());
                pstmt.setInt(2, category.getId());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error updating category: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }
        return success;
    }

    @Override
    public boolean deleteCategory(int categoryId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        String sql = "DELETE FROM categories WHERE id = ?";

        try {
            conn = connectionManager.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, categoryId);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error deleting category: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }
        return success;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT id, name FROM categories";

        try {
            conn = connectionManager.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    categories.add(new Category(id, name));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all categories: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.closeResultSet(rs);
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }
        return categories;
    }

    @Override
    public Category getCategoryById(int categoryId) {
        Category category = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT id, name FROM categories WHERE id = ?";

        try {
            conn = connectionManager.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, categoryId);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    category = new Category(id, name);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving category by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.closeResultSet(rs);
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }
        return category;
    }

    @Override
    public Category getCategoryByName(String name) {
        Category category = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT id, name FROM categories WHERE name = ?";

        try {
            conn = connectionManager.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, name);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    int id = rs.getInt("id");
                    String categoryName = rs.getString("name");
                    category = new Category(id, categoryName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving category by name: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.closeResultSet(rs);
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }
        return category;
    }
}