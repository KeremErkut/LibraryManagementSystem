package com.librarymanagementsystem.view;

import com.librarymanagementsystem.auth.UserAuthenticator;
import com.librarymanagementsystem.dao.BookDAO;
import com.librarymanagementsystem.dao.CategoryDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The LoginView class represents the graphical user interface for user login.
 * It allows users to enter their username and password to authenticate.
 */
public class LoginView extends JFrame {

    private final UserAuthenticator authenticator;
    private final BookDAO bookDAO;
    private final CategoryDAO categoryDAO;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    /**
     * Constructs a new LoginView.
     *
     * @param authenticator The UserAuthenticator instance to handle user authentication.
     * @param bookDAO The BookDAO instance to pass to BookManagementView upon successful login.
     * @param categoryDAO The CategoryDAO instance to pass to BookManagementView upon successful login.
     *
     */
    public LoginView(UserAuthenticator authenticator, BookDAO bookDAO, CategoryDAO categoryDAO) {
        this.authenticator = authenticator;
        this.bookDAO = bookDAO;
        this.categoryDAO = categoryDAO;
        initializeUI();
    }

    /**
     * Initializes and lays out the UI components for the login window.
     */
    private void initializeUI() {
        setTitle("Library Management System - Login");
        setSize(400, 250); // Set initial size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation
        setLocationRelativeTo(null); // Center the window on the screen
        setResizable(false); // Prevent resizing

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)); // Gap between components
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding

        // Form panel for username and password fields
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // Rows, cols, hgap, vgap

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20); // Preferred column width
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        // Panel for the login button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButton = new JButton("Login");
        buttonPanel.add(loginButton);

        // Message label for displaying authentication results
        messageLabel = new JLabel("", SwingConstants.CENTER); // Center text horizontally
        messageLabel.setForeground(Color.RED); // Default to red for error messages

        // Add components to the main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(messageLabel, BorderLayout.NORTH); // Place message at the top

        add(mainPanel); // Add the main panel to the frame

        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });

        // Allow pressing Enter key to log in
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });
        usernameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });
    }

    /**
     * Attempts to authenticate the user using the provided credentials.
     * Displays success or error messages to the user.
     * If successful, it closes the login window and opens the BookManagementView.
     */
    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (authenticator.authenticate(username, password)) {
            messageLabel.setForeground(new Color(0, 128, 0));
            messageLabel.setText("Login successful!");
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

            this.dispose(); // Close the current login frame

            // Open BookManagementView upon successful login
            SwingUtilities.invokeLater(() -> {
                // Pass all necessary DAOs and authenticator to BookManagementView
                BookManagementView bookManagementView = new BookManagementView(bookDAO, categoryDAO, authenticator);
                bookManagementView.setVisible(true);
            });

        } else {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Invalid username or password.");
        }
    }

    /**
     * Clears the input fields for username and password.
     */
    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        messageLabel.setText("");
    }
}