package com.librarymanagementsystem.model;

/**
 * The class which represents the book category object class.
 * This class includes category ID, Category name for each category.
 */
public class Category {
    private int id;       // Category ID
    private String name;  // Category name

    /**
     * Constructor of Category class .
     * Initializes all attributes.
     *
     * @param id Unique ID for book.
     * @param name name of Category.
     */
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Category class constructor without ID
     * Used for cases where the ID will be assigned automatically when adding a new category
     *
     * @param name The name of category
     */
    public Category(String name) {
        this.name = name;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setter methods
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}