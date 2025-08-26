package com.fahad.lms.dao;

import com.fahad.lms.model.Book;
import java.sql.SQLException;
import java.util.List;

public interface BookDao {
    int create(Book b) throws SQLException;
    Book findById(int id) throws SQLException;
    List<Book> findAll() throws SQLException;
    List<Book> search(String keyword) throws SQLException;
    void updateAvailableCopies(int bookId, int delta) throws SQLException;
}
