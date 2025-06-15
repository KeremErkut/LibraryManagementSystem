package com.librarymanagementsystem.view;

import com.librarymanagementsystem.dao.BookDAO;
import com.librarymanagementsystem.dao.CategoryDAO;
import com.librarymanagementsystem.auth.UserAuthenticator; // To get user role for permissions

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import com.librarymanagementsystem.model.Book;
import com.librarymanagementsystem.model.Category;
import com.librarymanagementsystem.util.Validator;

/**
 * The BookManagementView class represents the main application window
 * for managing books and categories. It allows authenticated users to
 * perform CRUD operations on books, manage categories, and search for books.
 */
public class BookManagementView extends JFrame {

    private final BookDAO bookDAO;
    private final CategoryDAO categoryDAO;
    private final UserAuthenticator authenticator;

    // UI Components for Book Management
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField bookIdField, bookTitleField, bookAuthorField, bookYearField;
    private JComboBox<String> bookCategoryComboBox;
    private JButton addButton, updateButton, deleteButton, clearButton;
    private JPanel categoryPanel;

    // UI Components for Search
    private JTextField searchTitleField, searchAuthorField, searchMinYearField, searchMaxYearField;
    private JComboBox<String> searchCategoryComboBox;
    private JButton searchButton, advancedSearchButton, clearSearchButton;

    // UI Components for Category Management (Admin only)
    private JTextField categoryNameField;
    private JButton addCategoryButton, updateCategoryButton, deleteCategoryButton;
    private JList<String> categoryList;
    private DefaultListModel<String> categoryListModel;

    /**
     * Constructs a new BookManagementView.
     *
     * @param bookDAO The BookDAO implementation for book database operations.
     * @param categoryDAO The CategoryDAO implementation for category database operations.
     * @param authenticator The UserAuthenticator instance to check user roles.
     */
    public BookManagementView(BookDAO bookDAO, CategoryDAO categoryDAO, UserAuthenticator authenticator) {
        this.bookDAO = bookDAO;
        this.categoryDAO = categoryDAO;
        this.authenticator = authenticator;
        initializeUI();
    }

    /**
     * Initializes and lays out the UI components for the main book management window.
     */
    private void initializeUI() {
        setTitle("Library Management System - " + (authenticator.getCurrentUserRole() != null ? authenticator.getCurrentUserRole() : "Guest"));
        setSize(1530, 900); // Adjust size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Main panel using BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Top Panel: Book Management Form ---
        JPanel bookFormPanel = new JPanel(new GridBagLayout());
        bookFormPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; bookFormPanel.add(new JLabel("Book ID:"), gbc);
        gbc.gridx = 1; bookIdField = new JTextField(15); bookFormPanel.add(bookIdField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; bookFormPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; bookTitleField = new JTextField(15); bookFormPanel.add(bookTitleField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; bookFormPanel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1; bookAuthorField = new JTextField(15); bookFormPanel.add(bookAuthorField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; bookFormPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; bookCategoryComboBox = new JComboBox<>(); bookFormPanel.add(bookCategoryComboBox, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; bookFormPanel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 1; bookYearField = new JTextField(15); bookFormPanel.add(bookYearField, gbc);

        // Book Actions Buttons
        JPanel bookActionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        addButton = new JButton("Add Book");
        updateButton = new JButton("Update Book");
        deleteButton = new JButton("Delete Book");
        clearButton = new JButton("Clear Form");

        bookActionPanel.add(addButton);
        bookActionPanel.add(updateButton);
        bookActionPanel.add(deleteButton);
        bookActionPanel.add(clearButton);

        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 2; // Span two columns
        bookFormPanel.add(bookActionPanel, gbc);

        mainPanel.add(bookFormPanel, BorderLayout.WEST); // Place form on the left

        // --- Center Panel: Book List Table ---
        String[] columnNames = {"ID", "Title", "Author", "Category ID", "Year"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER); // Place table in the center

        // --- Right Panel: Search and Category Management ---
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));

        // Search Panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Books"));
        GridBagConstraints searchGbc = new GridBagConstraints();
        searchGbc.insets = new Insets(5, 5, 5, 5);
        searchGbc.fill = GridBagConstraints.HORIZONTAL;

        row = 0;
        searchGbc.gridx = 0; searchGbc.gridy = row; searchPanel.add(new JLabel("Title:"), searchGbc);
        searchGbc.gridx = 1; searchTitleField = new JTextField(15); searchPanel.add(searchTitleField, searchGbc);

        row++;
        searchGbc.gridx = 0; searchGbc.gridy = row; searchPanel.add(new JLabel("Author:"), searchGbc);
        searchGbc.gridx = 1; searchAuthorField = new JTextField(15); searchPanel.add(searchAuthorField, searchGbc);

        row++;
        searchGbc.gridx = 0; searchGbc.gridy = row; searchPanel.add(new JLabel("Category:"), searchGbc);
        searchGbc.gridx = 1; searchCategoryComboBox = new JComboBox<>(); searchPanel.add(searchCategoryComboBox, searchGbc);

        row++;
        searchGbc.gridx = 0; searchGbc.gridy = row; searchPanel.add(new JLabel("Min Year:"), searchGbc);
        searchGbc.gridx = 1; searchMinYearField = new JTextField(15); searchPanel.add(searchMinYearField, searchGbc);

        row++;
        searchGbc.gridx = 0; searchGbc.gridy = row; searchPanel.add(new JLabel("Max Year:"), searchGbc);
        searchGbc.gridx = 1; searchMaxYearField = new JTextField(15); searchPanel.add(searchMaxYearField, searchGbc);

        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        searchButton = new JButton("Search by Title"); // Simple search by title
        advancedSearchButton = new JButton("Advanced Search");
        clearSearchButton = new JButton("Clear Search");
        searchButtonPanel.add(searchButton);
        searchButtonPanel.add(advancedSearchButton);
        searchButtonPanel.add(clearSearchButton);

        searchGbc.gridx = 0; searchGbc.gridy = ++row; searchGbc.gridwidth = 2;
        searchPanel.add(searchButtonPanel, searchGbc);

        rightPanel.add(searchPanel, BorderLayout.NORTH); // Place search panel at top of right side

        // Category Management Panel (Admin only)
        categoryPanel = new JPanel(new GridBagLayout()); // Use the class member
        categoryPanel.setBorder(BorderFactory.createTitledBorder("Category Management"));
        GridBagConstraints catGbc = new GridBagConstraints();
        catGbc.insets = new Insets(5, 5, 5, 5);
        catGbc.fill = GridBagConstraints.HORIZONTAL;

        row = 0;
        catGbc.gridx = 0; catGbc.gridy = row; categoryPanel.add(new JLabel("Category Name:"), catGbc);
        catGbc.gridx = 1; categoryNameField = new JTextField(15); categoryPanel.add(categoryNameField, catGbc);

        JPanel catButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        addCategoryButton = new JButton("Add Category");
        updateCategoryButton = new JButton("Update Category");
        deleteCategoryButton = new JButton("Delete Category");
        catButtonPanel.add(addCategoryButton);
        catButtonPanel.add(updateCategoryButton);
        catButtonPanel.add(deleteCategoryButton);

        catGbc.gridx = 0; catGbc.gridy = ++row; catGbc.gridwidth = 2;
        categoryPanel.add(catButtonPanel, catGbc);

        row++;
        catGbc.gridx = 0; catGbc.gridy = row; catGbc.gridwidth = 2; catGbc.weighty = 1.0; catGbc.fill = GridBagConstraints.BOTH;
        categoryListModel = new DefaultListModel<>();
        categoryList = new JList<>(categoryListModel);
        JScrollPane categoryScrollPane = new JScrollPane(categoryList);
        categoryPanel.add(categoryScrollPane, catGbc);

        rightPanel.add(categoryPanel, BorderLayout.CENTER); // Place category panel in the center of right side

        mainPanel.add(rightPanel, BorderLayout.EAST); // Place the combined right panel on the right

        add(mainPanel); // Add the main panel to the frame

        // Initialize data
        populateCategoryComboBoxes();
        populateBookTable(bookDAO.getAllBooks()); // Initial load of all books

        // Add Listeners
        addBookListeners();
        addSearchListeners();
        addCategoryListeners();
        addTableSelectionListener();

        // Apply permissions based on user role
        applyRolePermissions();
    }

    /**
     * Populates the category combo boxes with categories from the database.
     */
    private void populateCategoryComboBoxes() {
        bookCategoryComboBox.removeAllItems();
        searchCategoryComboBox.removeAllItems();
        categoryListModel.clear();

        searchCategoryComboBox.addItem("All Categories"); // Option to search all categories

        List<Category> categories = categoryDAO.getAllCategories();
        for (Category category : categories) {
            bookCategoryComboBox.addItem(category.getName());
            searchCategoryComboBox.addItem(category.getName());
            categoryListModel.addElement(category.getName() + " (ID: " + category.getId() + ")");
        }
    }

    /**
     * Populates the book table with a given list of books.
     * @param books The list of books to display.
     */
    private void populateBookTable(List<Book> books) {
        tableModel.setRowCount(0); // Clear existing data
        for (Book book : books) {
            Object[] rowData = {book.getId(), book.getTitle(), book.getAuthor(), book.getCategory(), book.getYear()};
            tableModel.addRow(rowData);
        }
    }

    /**
     * Adds action listeners for book management buttons.
     */
    private void addBookListeners() {
        addButton.addActionListener(e -> addBook());
        updateButton.addActionListener(e -> updateBook());
        deleteButton.addActionListener(e -> deleteBook());
        clearButton.addActionListener(e -> clearBookForm());
    }

    /**
     * Adds action listeners for search buttons.
     */
    private void addSearchListeners() {
        searchButton.addActionListener(e -> searchBooksByTitle());
        advancedSearchButton.addActionListener(e -> advancedSearchBooks());
        clearSearchButton.addActionListener(e -> clearSearchForm());
    }

    /**
     * Adds action listeners for category management buttons.
     */
    private void addCategoryListeners() {
        addCategoryButton.addActionListener(e -> addCategory());
        updateCategoryButton.addActionListener(e -> updateCategory());
        deleteCategoryButton.addActionListener(e -> deleteCategory());
        categoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && categoryList.getSelectedValue() != null) {
                String selectedItem = categoryList.getSelectedValue();
                // Extract category name from "Name (ID: X)" format
                String categoryName = selectedItem.substring(0, selectedItem.lastIndexOf(" (ID:"));
                categoryNameField.setText(categoryName);
            }
        });
    }

    /**
     * Adds a listener to the book table to populate the form when a row is selected.
     */
    private void addTableSelectionListener() {
        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && bookTable.getSelectedRow() != -1) {
                int selectedRow = bookTable.getSelectedRow();
                bookIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                bookTitleField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                bookAuthorField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                // Set the correct category in the combobox
                int categoryId = (int) tableModel.getValueAt(selectedRow, 3);
                Category selectedCategory = categoryDAO.getCategoryById(categoryId);
                if (selectedCategory != null) {
                    bookCategoryComboBox.setSelectedItem(selectedCategory.getName());
                }
                bookYearField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });
    }

    /**
     * Method to add a new book.
     */
    private void addBook() {
        String id = bookIdField.getText();
        String title = bookTitleField.getText();
        String author = bookAuthorField.getText();
        String categoryName = (String) bookCategoryComboBox.getSelectedItem();
        String yearString = bookYearField.getText();

        if (Validator.isNullOrEmpty(id) || !Validator.isValidBookId(id)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Book ID (max 50 chars).", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (bookDAO.getBookById(id) != null) {
            JOptionPane.showMessageDialog(this, "Book with this ID already exists.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Validator.isValidTitle(title)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Book Title (max 255 chars).", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Validator.isValidAuthor(author)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Book Author (max 255 chars).", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Validator.isNullOrEmpty(categoryName)) {
            JOptionPane.showMessageDialog(this, "Please select a category.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Validator.isValidYear(yearString)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Year (e.g., 1990).", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int year = Integer.parseInt(yearString);
        Category category = categoryDAO.getCategoryByName(categoryName);
        int categoryId = (category != null) ? category.getId() : -1;

        if (categoryId == -1) {
            JOptionPane.showMessageDialog(this, "Selected category not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book newBook = new Book(id, title, author, categoryId, year);

        if (bookDAO.addBook(newBook)) {
            JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            populateBookTable(bookDAO.getAllBooks()); // Refresh table
            clearBookForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add book.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Method to update an existing book.
     */
    private void updateBook() {
        String id = bookIdField.getText();
        String title = bookTitleField.getText();
        String author = bookAuthorField.getText();
        String categoryName = (String) bookCategoryComboBox.getSelectedItem();
        String yearString = bookYearField.getText();

        if (Validator.isNullOrEmpty(id)) {
            JOptionPane.showMessageDialog(this, "Please select a book to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Basic validations
        if (!Validator.isValidTitle(title)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Book Title (max 255 chars).", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Validator.isValidAuthor(author)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Book Author (max 255 chars).", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Validator.isNullOrEmpty(categoryName)) {
            JOptionPane.showMessageDialog(this, "Please select a category.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Validator.isValidYear(yearString)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Year (e.g., 1990).", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int year = Integer.parseInt(yearString);
        Category category = categoryDAO.getCategoryByName(categoryName);
        int categoryId = (category != null) ? category.getId() : -1;

        if (categoryId == -1) {
            JOptionPane.showMessageDialog(this, "Selected category not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book updatedBook = new Book(id, title, author, categoryId, year);

        if (bookDAO.updateBook(updatedBook)) {
            JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            populateBookTable(bookDAO.getAllBooks()); // Refresh table
            clearBookForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update book. Make sure the ID exists.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Method to delete an existing book.
     */
    private void deleteBook() {
        String id = bookIdField.getText();
        if (Validator.isNullOrEmpty(id)) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (bookDAO.deleteBook(id)) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                populateBookTable(bookDAO.getAllBooks()); // Refresh table
                clearBookForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Clears the book form fields.
     */
    private void clearBookForm() {
        bookIdField.setText("");
        bookTitleField.setText("");
        bookAuthorField.setText("");
        bookYearField.setText("");
        bookCategoryComboBox.setSelectedIndex(0); // Select first item or default
        bookTable.clearSelection(); // Clear table selection
    }

    /**
     * Performs a simple search for books by title.
     */
    private void searchBooksByTitle() {
        String title = searchTitleField.getText();
        if (Validator.isNullOrEmpty(title)) {
            JOptionPane.showMessageDialog(this, "Please enter a title to search.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<Book> searchResults = bookDAO.searchBooksByTitle(title);
        populateBookTable(searchResults);
        if (searchResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No books found with that title.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Performs an advanced search for books.
     */
    private void advancedSearchBooks() {
        String title = searchTitleField.getText();
        String author = searchAuthorField.getText();
        String categoryName = (String) searchCategoryComboBox.getSelectedItem();
        String minYearString = searchMinYearField.getText();
        String maxYearString = searchMaxYearField.getText();

        int categoryId = 0; // 0 indicates no category filter
        if (categoryName != null && !categoryName.equals("All Categories")) {
            Category category = categoryDAO.getCategoryByName(categoryName);
            if (category != null) {
                categoryId = category.getId();
            }
        }

        int minYear = 0; // 0 indicates no min year filter
        if (!Validator.isNullOrEmpty(minYearString) && Validator.isValidInteger(minYearString)) {
            minYear = Integer.parseInt(minYearString);
        } else if (!Validator.isNullOrEmpty(minYearString)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for Min Year.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int maxYear = 0; // 0 indicates no max year filter
        if (!Validator.isNullOrEmpty(maxYearString) && Validator.isValidInteger(maxYearString)) {
            maxYear = Integer.parseInt(maxYearString);
        } else if (!Validator.isNullOrEmpty(maxYearString)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for Max Year.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (minYear > 0 && maxYear > 0 && minYear > maxYear) {
            JOptionPane.showMessageDialog(this, "Min Year cannot be greater than Max Year.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Book> searchResults = bookDAO.advancedSearch(title, author, categoryId, minYear, maxYear);
        populateBookTable(searchResults);
        if (searchResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No books found matching the advanced search criteria.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Clears the search form fields and reloads all books.
     */
    private void clearSearchForm() {
        searchTitleField.setText("");
        searchAuthorField.setText("");
        searchMinYearField.setText("");
        searchMaxYearField.setText("");
        searchCategoryComboBox.setSelectedIndex(0); // Select "All Categories"
        populateBookTable(bookDAO.getAllBooks()); // Reload all books
    }

    /**
     * Method to add a new category. (Admin only)
     */
    private void addCategory() {
        String categoryName = categoryNameField.getText();
        if (!Validator.isValidCategoryName(categoryName)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid category name.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if category already exists
        if (categoryDAO.getCategoryByName(categoryName) != null) {
            JOptionPane.showMessageDialog(this, "Category with this name already exists.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Category newCategory = new Category(categoryName);
        if (categoryDAO.addCategory(newCategory)) {
            JOptionPane.showMessageDialog(this, "Category added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            populateCategoryComboBoxes(); // Refresh category lists
            categoryNameField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add category.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Method to update an existing category. (Admin only)
     */
    private void updateCategory() {
        String selectedCategoryText = categoryList.getSelectedValue();
        if (selectedCategoryText == null) {
            JOptionPane.showMessageDialog(this, "Please select a category to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Extract ID from "(ID: X)" format
        int categoryId = -1;
        try {
            int startIndex = selectedCategoryText.lastIndexOf("(ID:") + 4;
            int endIndex = selectedCategoryText.lastIndexOf(")");
            categoryId = Integer.parseInt(selectedCategoryText.substring(startIndex, endIndex).trim());
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(this, "Could not retrieve category ID from selection.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newCategoryName = categoryNameField.getText();
        if (!Validator.isValidCategoryName(newCategoryName)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid new category name.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if updated category name already exists for another ID
        Category existingCategory = categoryDAO.getCategoryByName(newCategoryName);
        if (existingCategory != null && existingCategory.getId() != categoryId) {
            JOptionPane.showMessageDialog(this, "Another category with this name already exists.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }


        Category updatedCategory = new Category(categoryId, newCategoryName);
        if (categoryDAO.updateCategory(updatedCategory)) {
            JOptionPane.showMessageDialog(this, "Category updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            populateCategoryComboBoxes(); // Refresh category lists
            categoryNameField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update category.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Method to delete an existing category. (Admin only)
     */
    private void deleteCategory() {
        String selectedCategoryText = categoryList.getSelectedValue();
        if (selectedCategoryText == null) {
            JOptionPane.showMessageDialog(this, "Please select a category to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Extract ID from "(ID: X)" format
        int categoryId = -1;
        try {
            int startIndex = selectedCategoryText.lastIndexOf("(ID:") + 4;
            int endIndex = selectedCategoryText.lastIndexOf(")");
            categoryId = Integer.parseInt(selectedCategoryText.substring(startIndex, endIndex).trim());
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(this, "Could not retrieve category ID from selection.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if any books are associated with this category
        List<Book> booksInCategory = bookDAO.getBooksByCategory(categoryId);
        if (!booksInCategory.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cannot delete category: Books are associated with it. Please reassign or delete books first.", "Deletion Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this category?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (categoryDAO.deleteCategory(categoryId)) {
                JOptionPane.showMessageDialog(this, "Category deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                populateCategoryComboBoxes(); // Refresh category lists
                categoryNameField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete category.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Applies UI component visibility and enabled state based on the current user's role.
     * Admin users have access to book and category management.
     * Regular users (or guests) can only search and view books.
     */
    private void applyRolePermissions() {
        String role = authenticator.getCurrentUserRole();
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);

        // Book Management Buttons (Add, Update, Delete)
        addButton.setVisible(isAdmin);
        updateButton.setVisible(isAdmin);
        deleteButton.setVisible(isAdmin);
        clearButton.setVisible(isAdmin);

        // Book form fields editability
        bookIdField.setEditable(isAdmin);
        bookTitleField.setEditable(isAdmin);
        bookAuthorField.setEditable(isAdmin);
        bookYearField.setEditable(isAdmin);
        bookCategoryComboBox.setEnabled(isAdmin);

        // Category Management Panel
        addCategoryButton.setVisible(isAdmin);
        updateCategoryButton.setVisible(isAdmin);
        deleteCategoryButton.setVisible(isAdmin);
        categoryNameField.setEditable(isAdmin);
        categoryList.setEnabled(isAdmin); // Enable/disable selection for interaction

        // Adjust border title
        // Cast the Border to TitledBorder
        TitledBorder categoryPanelBorder = (TitledBorder) categoryPanel.getBorder();

        if (isAdmin) {
            categoryPanelBorder.setTitle("Category Management (Admin Only)");
        } else {
            categoryPanelBorder.setTitle("Categories (View Only)");
        }
        categoryPanel.repaint(); // Repaint the panel to reflect the title change
    }
}