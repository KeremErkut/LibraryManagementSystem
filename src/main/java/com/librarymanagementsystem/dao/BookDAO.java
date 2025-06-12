package com.librarymanagementsystem.dao;

import com.librarymanagementsystem.model.Book;
import java.util.List;

/**
 * Interface for Data Access Operations related to Book objects.
 * Defines the contract for interacting with book data storage.
 */
public interface BookDAO {
    /**
     * Retrieves all books from the database.
     * @return A list of all books.
     */
    List<Book> getAllBooks();

    /**
     * Adds a new book to the database.
     * @param book The Book object to be added.
     * @return true if the book was successfully added, false otherwise.
     */
    boolean addBook(Book book);

    /**
     * Updates an existing book in the database.
     * @param book The Book object with updated information (ID must match an existing book).
     * @return true if the book was successfully updated, false otherwise.
     */
    boolean updateBook(Book book);

    /**
     * Deletes a book from the database by its ID.
     * @param bookId The ID of the book to be deleted.
     * @return true if the book was successfully deleted, false otherwise.
     */
    boolean deleteBook(String bookId); // SDS specifies bookId as String

    /**
     * Retrieves a book by its unique ID.
     * @param bookId The ID of the book to retrieve.
     * @return The Book object if found, null otherwise.
     */
    Book getBookById(String bookId); // Changed to String as per SDS (Book ID is string)

    /**
     * Retrieves a list of books belonging to a specific category.
     * @param categoryId The ID of the category.
     * @return A list of books in the specified category.
     */
    List<Book> getBooksByCategory(int categoryId);

    /**
     * Searches for books by their title.
     * @param title The title (or part of it) to search for.
     * @return A list of books matching the title.
     */
    List<Book> searchBooksByTitle(String title);

    /**
     * Performs an advanced search for books based on title, author, category, and year range.
     * According to SDS: Search Title, Search Author, Search Category, Search Year.
     *
     * @param title The title to search for (can be partial).
     * @param author The author to search for (can be partial).
     * @param categoryId The ID of the category to filter by (0 or negative if not used).
     * @param minYear The minimum publication year (0 or negative if not used).
     * @param maxYear The maximum publication year (0 or negative if not used).
     * @return A list of books matching the advanced search criteria.
     */
    List<Book> advancedSearch(String title, String author, int categoryId, int minYear, int maxYear);
}