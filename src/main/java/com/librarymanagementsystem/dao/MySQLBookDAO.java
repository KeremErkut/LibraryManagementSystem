package com.librarymanagementsystem.dao;

import com.librarymanagementsystem.model.Book;
import com.librarymanagementsystem.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * MySQL implementation of the BookDAO interface.
 * Handles database operations for Book objects using JDBC.
 */
public class MySQLBookDAO implements BookDAO {

    private final ConnectionManager connectionManager;

    /**
     * Constructor for MySQLBookDAO.
     *
     * @param connectionManager The ConnectionManager instance to manage database connections.
     */
    public MySQLBookDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT id, title, author, category_id, year FROM books";

        try {
            conn = connectionManager.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    String id = rs.getString("id");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    int categoryId = rs.getInt("category_id");
                    int year = rs.getInt("year");
                    books.add(new Book(id, title, author, categoryId, year));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all books: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.closeResultSet(rs);
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }
        return books;
    }

    @Override
    public boolean addBook(Book book) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        String sql = "INSERT INTO books (id, title, author, category_id, year) VALUES (?, ?, ?, ?, ?)";

        try {
            conn = connectionManager.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, book.getId());
                pstmt.setString(2, book.getTitle());
                pstmt.setString(3, book.getAuthor());
                pstmt.setInt(4, book.getCategory());
                pstmt.setInt(5, book.getYear());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }
        return success;
    }

    @Override
    public boolean updateBook(Book book) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        String sql = "UPDATE books SET title = ?, author = ?, category_id = ?, year = ? WHERE id = ?";

        try {
            conn = connectionManager.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, book.getTitle());
                pstmt.setString(2, book.getAuthor());
                pstmt.setInt(3, book.getCategory());
                pstmt.setInt(4, book.getYear());
                pstmt.setString(5, book.getId()); // WHERE clause parameter

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }
        return success;
    }

    @Override
    public boolean deleteBook(String bookId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        String sql = "DELETE FROM books WHERE id = ?";

        try {
            conn = connectionManager.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, bookId);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }
        return success;
    }

    @Override
    public Book getBookById(String bookId) {
        Book book = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT id, title, author, category_id, year FROM books WHERE id = ?";

        try {
            conn = connectionManager.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, bookId);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    String id = rs.getString("id");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    int categoryId = rs.getInt("category_id");
                    int year = rs.getInt("year");
                    book = new Book(id, title, author, categoryId, year);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving book by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.closeResultSet(rs);
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }
        return book;
    }

    @Override
    public List<Book> getBooksByCategory(int categoryId) {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT id, title, author, category_id, year FROM books WHERE category_id = ?";

        try {
            conn = connectionManager.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, categoryId);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    String id = rs.getString("id");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    int catId = rs.getInt("category_id");
                    int year = rs.getInt("year");
                    books.add(new Book(id, title, author, catId, year));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving books by category: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.closeResultSet(rs);
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }
        return books;
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT id, title, author, category_id, year FROM books WHERE title LIKE ?";

        try {
            conn = connectionManager.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%" + title + "%"); // Use % for partial matching
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    String id = rs.getString("id");
                    String bookTitle = rs.getString("title");
                    String author = rs.getString("author");
                    int categoryId = rs.getInt("category_id");
                    int year = rs.getInt("year");
                    books.add(new Book(id, bookTitle, author, categoryId, year));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching books by title: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.closeResultSet(rs);
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }
        return books;
    }

    @Override
    public List<Book> advancedSearch(String title, String author, int categoryId, int minYear, int maxYear) {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Start with a base SQL query
        StringBuilder sql = new StringBuilder("SELECT id, title, author, category_id, year FROM books WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Dynamically add conditions based on provided search criteria
        if (title != null && !title.isEmpty()) {
            sql.append(" AND title LIKE ?");
            params.add("%" + title + "%");
        }
        if (author != null && !author.isEmpty()) {
            sql.append(" AND author LIKE ?");
            params.add("%" + author + "%");
        }
        if (categoryId > 0) { // Assuming 0 or negative means no category filter
            sql.append(" AND category_id = ?");
            params.add(categoryId);
        }
        if (minYear > 0) { // Assuming 0 or negative means no minYear filter
            sql.append(" AND year >= ?");
            params.add(minYear);
        }
        if (maxYear > 0) { // Assuming 0 or negative means no maxYear filter
            sql.append(" AND year <= ?");
            params.add(maxYear);
        }

        try {
            conn = connectionManager.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql.toString());

                // Set parameters based on their order in the SQL query
                for (int i = 0; i < params.size(); i++) {
                    Object param = params.get(i);
                    if (param instanceof String) {
                        pstmt.setString(i + 1, (String) param);
                    } else if (param instanceof Integer) {
                        pstmt.setInt(i + 1, (Integer) param);
                    }
                    // Add other types if necessary (e.g., Double, Date)
                }

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    String bookId = rs.getString("id");
                    String bookTitle = rs.getString("title");
                    String bookAuthor = rs.getString("author");
                    int catId = rs.getInt("category_id");
                    int bookYear = rs.getInt("year");
                    books.add(new Book(bookId, bookTitle, bookAuthor, catId, bookYear));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during advanced book search: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.closeResultSet(rs);
            connectionManager.closeStatement(pstmt);
            connectionManager.closeConnection(conn);
        }
        return books;
    }
}