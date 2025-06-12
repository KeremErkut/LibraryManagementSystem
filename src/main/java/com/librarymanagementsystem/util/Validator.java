package com.librarymanagementsystem.util;

import java.time.Year;

/**
 * Utility class for validating various types of input data.
 * Provides static methods to check common validation rules.
 */
public class Validator {

    // Private constructor to prevent instantiation, as this is a utility class
    private Validator() {
        // No instantiation needed
    }

    /**
     * Checks if a given string is null or empty (after trimming whitespace).
     *
     * @param input The string to validate.
     * @return true if the string is null or empty, false otherwise.
     */
    public static boolean isNullOrEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    /**
     * Checks if a given string can be parsed as a valid integer.
     *
     * @param input The string to validate.
     * @return true if the string is a valid integer, false otherwise.
     */
    public static boolean isValidInteger(String input) {
        if (isNullOrEmpty(input)) {
            return false;
        }
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a given integer is positive (greater than 0).
     *
     * @param number The integer to validate.
     * @return true if the integer is positive, false otherwise.
     */
    public static boolean isPositiveInteger(int number) {
        return number > 0;
    }

    /**
     * Checks if a given string represents a valid year within a reasonable range (e.g., 1000 to current year).
     *
     * @param yearString The string representing the year.
     * @return true if the year string is valid, false otherwise.
     */
    public static boolean isValidYear(String yearString) {
        if (!isValidInteger(yearString)) {
            return false;
        }
        int year = Integer.parseInt(yearString);
        int currentYear = Year.now().getValue();
        return year >= 1000 && year <= currentYear; // Assuming books are not older than year 1000 and not in the future
    }

    /**
     * Checks if a book ID is valid.
     * (Specific validation rules for Book ID can be added here, e.g., format, length)
     * For now, just checks if it's not null or empty.
     *
     * @param id The book ID string.
     * @return true if the ID is considered valid, false otherwise.
     */
    public static boolean isValidBookId(String id) {
        // According to SDS: "Unique string (50 chars max) for book"
        // Here we'll check for not null/empty and max length.
        return !isNullOrEmpty(id) && id.length() <= 50;
    }

    /**
     * Checks if a book title is valid.
     * (According to SDS: "String (255 chars max), mandatory")
     *
     * @param title The book title string.
     * @return true if the title is valid, false otherwise.
     */
    public static boolean isValidTitle(String title) {
        return !isNullOrEmpty(title) && title.length() <= 255;
    }

    /**
     * Checks if a book author is valid.
     * (According to SDS: "String (255 chars max), mandatory")
     *
     * @param author The book author string.
     * @return true if the author is valid, false otherwise.
     */
    public static boolean isValidAuthor(String author) {
        return !isNullOrEmpty(author) && author.length() <= 255;
    }

    /**
     * Checks if a category name is valid.
     * (Simple check for null or empty for now, can be extended for length/format)
     *
     * @param categoryName The category name string.
     * @return true if the category name is valid, false otherwise.
     */
    public static boolean isValidCategoryName(String categoryName) {
        return !isNullOrEmpty(categoryName);
    }
}