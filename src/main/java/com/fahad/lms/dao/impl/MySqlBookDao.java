package com.fahad.lms.dao.impl;

import com.fahad.lms.dao.BookDao;
import com.fahad.lms.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlBookDao implements BookDao {
    private final Connection conn;
    public MySqlBookDao(Connection conn) { this.conn = conn; }

    @Override public int create(Book b) throws SQLException {
        String sql = "INSERT INTO books(title, author_id, category_id, isbn, published_year, total_copies, available_copies, description) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, b.title);
            ps.setInt(2, b.authorId);
            ps.setInt(3, b.categoryId);
            ps.setString(4, b.isbn);
            if (b.publishedYear==null) ps.setNull(5, Types.INTEGER); else ps.setInt(5, b.publishedYear);
            ps.setInt(6, b.totalCopies);
            ps.setInt(7, b.availableCopies);
            ps.setString(8, b.description);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); return rs.getInt(1); }
        }
    }

    @Override public Book findById(int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    @Override public List<Book> findAll() throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM books ORDER BY id DESC")) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Book> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        }
    }

    @Override public List<Book> search(String keyword) throws SQLException {
        String like = "%" + keyword + "%";
        String sql = "SELECT * FROM books WHERE title LIKE ? OR description LIKE ? ORDER BY title";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like); ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                List<Book> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        }
    }

    @Override public void updateAvailableCopies(int bookId, int delta) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE books SET available_copies = available_copies + ? WHERE id=?")) {
            ps.setInt(1, delta);
            ps.setInt(2, bookId);
            ps.executeUpdate();
        }
    }

    private Book map(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.id = rs.getInt("id");
        b.title = rs.getString("title");
        b.authorId = rs.getInt("author_id");
        b.categoryId = rs.getInt("category_id");
        b.isbn = rs.getString("isbn");
        int py = rs.getInt("published_year");
        b.publishedYear = rs.wasNull()? null: py;
        b.totalCopies = rs.getInt("total_copies");
        b.availableCopies = rs.getInt("available_copies");
        b.description = rs.getString("description");
        return b;
    }
}
