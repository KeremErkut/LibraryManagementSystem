package com.librarymanagementsystem.dao;

import com.librarymanagementsystem.model.Category;
import java.util.List;

/**
 * Interface for Data Access Operations related to Category objects.
 * Defines the contract for interacting with category data storage.
 */
public interface CategoryDAO {
    /**
     * Adds a new category to the database.
     * @param category The Category object to be added.
     * @return true if the category was successfully added, false otherwise.
     */
    boolean addCategory(Category category);

    /**
     * Updates an existing category in the database.
     * @param category The Category object with updated information (ID must match an existing category).
     * @return true if the category was successfully updated, false otherwise.
     */
    boolean updateCategory(Category category);

    /**
     * Deletes a category from the database by its ID.
     * @param categoryId The ID of the category to be deleted.
     * @return true if the category was successfully deleted, false otherwise.
     */
    boolean deleteCategory(int categoryId);

    /**
     * Retrieves all categories from the database.
     * @return A list of all categories.
     */
    List<Category> getAllCategories();

    /**
     * Retrieves a category by its unique ID.
     * @param categoryId The ID of the category to retrieve.
     * @return The Category object if found, null otherwise.
     */
    Category getCategoryById(int categoryId);

    /**
     * Retrieves a category by its name.
     * @param name The name of the category to retrieve.
     * @return The Category object if found, null otherwise.
     */
    Category getCategoryByName(String name);
}