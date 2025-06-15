# Library Management System

## About the Project

This project is a simple Library Management System developed using Java Swing for the user interface and MySQL for database management. The application is designed to manage books and categories. It features two distinct user roles, `ADMIN` and `USER`, each with different authorization levels.

## Features

* **User Authentication:**
    * Ability to log in with `ADMIN` and `USER` roles.
    * Automatic creation of default `ADMIN` and `USER` accounts (on first application run).
* **Book Management (ADMIN Privilege):**
    * Add new books.
    * Update existing book information.
    * Delete books.
    * Clear form fields.
* **Category Management (ADMIN Privilege):**
    * Add new categories.
    * Update existing category names.
    * Delete categories (along with associated books).
* **Book Search and Display:**
    * Simple search by title.
    * Advanced search by title, author, category, minimum year, and maximum year.
    * Ability to list all books.
    * Detailed and adjustable column widths in the book list table for better readability.
* **Role-Based Permissions:**
    * `ADMIN` role: Can perform all book and category management operations.
    * `USER` role: Can only search and view books; restricted from adding, updating, deleting, and category management functions.
* **Database Integration:**
    * Persistent data storage using MySQL database.
    * Database connectivity via JDBC (Java Database Connectivity).

## Technologies Used

* **Java (JDK 24.0.1)**
* **Java Swing:** For graphical user interface development.
* **Maven:** For project dependency management and build automation.
* **MySQL 8.4.0 (or compatible version):** Database system.
* **MySQL Connector/J 8.0.33:** Java driver for MySQL connectivity.

## Setup and Running the Application

### Prerequisites

* **Java Development Kit (JDK) 24.0.1** or a newer version must be installed.
* **Apache Maven** must be installed.
* **MySQL Server 8.4.0** or a newer version must be installed and running.
* **MySQL Workbench** or another MySQL client (for database configuration).

### Step 1: Database Setup

1.  **Start MySQL Server:** Ensure your MySQL server is running. (For Linux: `sudo systemctl start mysql`)
2.  **Create Database:** Using your MySQL client, create a new database named `library_db`:
    ```sql
    CREATE DATABASE IF NOT EXISTS library_db;
    ```
3.  **Create User (Recommended):** For security, it's better to create a dedicated user for your application instead of using `root`. Create a user named `library_user` with a strong password (`your_application_password`) and grant all privileges on `library_db`:
    ```sql
    CREATE USER 'library_user'@'localhost' IDENTIFIED WITH mysql_native_password BY 'your_application_password';
    GRANT ALL PRIVILEGES ON library_db.* TO 'library_user'@'localhost';
    FLUSH PRIVILEGES;
    ```
    * **NOTE:** Ensure your password complies with MySQL's password policy (e.g., must contain uppercase, lowercase, numbers, special characters). Otherwise, you might encounter `ERROR 1819`.

4.  **Table Creation:** The application's `Main.java` class will automatically create the necessary tables upon successful database connection if they do not exist.
    * Typically, these include `users` for authentication, and `categories` and `books` for library data.

### Step 2: Project Configuration

1.  **Clone the Repository:** Clone this repository to your local machine:
    ```bash
    git clone <(https://github.com/KeremErkut/LibraryManagementSystem)>
    cd LibraryManagementSystem
    ```
2.  **Update Database Credentials:** Open the `src/main/java/com/librarymanagementsystem/Main.java` file. Update the database username and password in the following lines to match your MySQL setup:
    ```java
    private static final String DB_USERNAME = "root"; // Or 'library_user'
    private static final String DB_PASSWORD = "your_mysql_password"; // ENTER YOUR CORRECT PASSWORD HERE!
    ```
3.  **Load Maven Dependencies:** In the project directory (where `pom.xml` is located), open a terminal and install Maven dependencies:
    ```bash
    mvn clean install
    ```
    This command will download all necessary libraries (e.g., MySQL Connector/J).

### Step 3: Running the Application

1.  **Run from IntelliJ IDEA:**
    * Open IntelliJ IDEA and import the project (`File -> Open` and select the project directory).
    * Ensure Maven dependencies are loaded correctly (you might need to `Reload All Maven Projects` from the Maven panel on the right).
    * Navigate to `src/main/java/com/librarymanagementsystem/Main.java`.
    * Click the green run button next to the `main` method or select `Run -> Run 'Main'` from the top menu.

2.  **Run from Terminal (Advanced):**
    After performing a Maven build, you can run the application using the generated JAR file in the `target` folder.
    ```bash
    # First, build the package with Maven
    mvn package

    # Then, run the JAR file
    # The JAR name and path might vary based on your project setup; check the 'target' folder.
    java -jar target/LibraryManagementSystem-1.0-SNAPSHOT.jar
    ```

### Default Users

Upon the first run of the application (or if these users do not yet exist in the database), the following default users will be automatically created:

* **ADMIN:**
    * Username: `admin`
    * Password: `adminpassword`
* **USER:**
    * Username: `user`
    * Password: `userpassword`

**Security Note:** It is strongly recommended to change these default passwords immediately after the first login.

## Project Structure

```bash
.
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/librarymanagementsystem/
│   │   │       ├── auth/             # User authentication logic
│   │   │       │   └── UserAuthenticator.java
│   │   │       ├── dao/              # Database access objects (DAO)
│   │   │       │   ├── MySQLBookDAO.java
│   │   │       │   └── MySQLCategoryDAO.java
│   │   │       ├── model/            # Data models (Book, Category, User)
│   │   │       │   ├── Book.java
│   │   │       │   ├── Category.java
│   │   │       │   └── User.java
│   │   │       ├── util/             # Utility classes (e.g., ConnectionManager)
│   │   │       │   └── ConnectionManager.java
│   │   │       ├── view/             # User interface (Swing) classes
│   │   │       │   ├── LoginView.java
│   │   │       │   └── BookManagementView.java
│   │   │       └── Main.java         # Application entry point
│   │   └── resources/        # Application resources (if any)
│   └── test/
│       └── java/           # Test classes
├── pom.xml                 # Maven project configuration file
└── README.md               # This file
```


## Contributing

We welcome contributions! Please adhere to existing coding standards and create a separate branch for any features or bug fixes before submitting a Pull Request.

