package com.fahad.lms.model;

public class Book {
    public int id;
    public String title;
    public int authorId;
    public int categoryId;
    public String isbn;
    public Integer publishedYear;
    public int totalCopies;
    public int availableCopies;
    public String description;

    @Override public String toString() {
        return "Book{id=%d, title='%s', available=%d/%d}".formatted(id, title, availableCopies, totalCopies);
    }
}
