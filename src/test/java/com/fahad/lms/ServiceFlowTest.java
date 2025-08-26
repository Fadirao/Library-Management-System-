package com.fahad.lms;

import com.fahad.lms.model.Book;
import com.fahad.lms.service.LibraryService;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceFlowTest extends BaseTest {

    @Test
    void borrowAndReturnRawFlow() throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("INSERT INTO authors(name) VALUES('A')");
            st.execute("INSERT INTO categories(name) VALUES('C')");
            st.execute("INSERT INTO books(title,author_id,category_id,total_copies,available_copies) VALUES('B',1,1,2,2)");
            st.execute("INSERT INTO members(name,email) VALUES('M','m@m.com')");
        }
        LibraryService svc = new LibraryService(conn);
        int loanId = svc.borrowBook(1, 1, 7); // falls back to raw flow in H2
        assertTrue(loanId > 0);
        double fine = svc.returnBook(loanId, 2.0);
        assertTrue(fine >= 0.0);
    }
}
