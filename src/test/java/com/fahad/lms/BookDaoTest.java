package com.fahad.lms;

import com.fahad.lms.dao.impl.MySqlBookDao;
import com.fahad.lms.model.Book;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class BookDaoTest extends BaseTest {

    @Test
    void createAndFetchBook() throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("INSERT INTO authors(name) VALUES('Test Author')");
            st.execute("INSERT INTO categories(name) VALUES('Test Cat')");
        }
        Book b = new Book();
        b.title = "Test Title"; b.authorId=1; b.categoryId=1; b.isbn="X1"; b.publishedYear=2020; b.totalCopies=3; b.availableCopies=3; b.description="Desc";
        MySqlBookDao dao = new MySqlBookDao(conn);
        int id = dao.create(b);
        Book got = dao.findById(id);
        assertNotNull(got);
        assertEquals("Test Title", got.title);
    }
}
