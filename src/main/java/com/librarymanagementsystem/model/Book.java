package com.librarymanagementsystem.model;

/**
 * The class which represents the book object class
 * This class includes an ID, a Title, Category and Published Year for each book.
 */
public class Book {
    private String id;        // Book ID
    private String title;     // Book Title
    private String author;    // Book Author
    private int category;     // Book Category ID
    private int year;         // Book Year

    /**
     * The constructor of book class.
     * Initializes all attributes.
     *
     * @param id Unique ID for book.
     * @param title The tittle of book.
     * @param author The author of book.
     * @param category The categoryID which book belongs it.
     * @param year Publissh year of book.
     */
    public Book(String id, String title, String author, int category, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.year = year;
    }

    // Getter Methods
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getCategory() {
        return category;
    }

    public int getYear() {
        return year;
    }

    // Setter Methods (İsteğe bağlı, ancak güncelleme işlemleri için faydalı olabilir)
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category=" + category +
                ", year=" + year +
                '}';
    }
}