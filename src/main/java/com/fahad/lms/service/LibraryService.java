package com.fahad.lms.service;

import com.fahad.lms.dao.BookDao;
import com.fahad.lms.dao.LoanDao;
import com.fahad.lms.dao.MemberDao;
import com.fahad.lms.dao.impl.MySqlBookDao;
import com.fahad.lms.dao.impl.MySqlLoanDao;
import com.fahad.lms.dao.impl.MySqlMemberDao;
import com.fahad.lms.model.Book;
import com.fahad.lms.model.Loan;
import com.fahad.lms.model.Member;

import java.sql.*;
import java.util.List;

public class LibraryService {
    private final Connection conn;
    private final BookDao bookDao;
    private final MemberDao memberDao;
    private final LoanDao loanDao;

    public LibraryService(Connection conn) {
        this.conn = conn;
        this.bookDao = new MySqlBookDao(conn);
        this.memberDao = new MySqlMemberDao(conn);
        this.loanDao = new MySqlLoanDao(conn);
    }

    public List<Book> listBooks() throws SQLException { return bookDao.findAll(); }
    public List<Book> searchBooks(String kw) throws SQLException { return bookDao.search(kw); }
    public int registerMember(String name, String email) throws SQLException {
        Member m = new Member(); m.name=name; m.email=email; return memberDao.create(m);
    }

    // Prefer stored procedures on MySQL; fall back to raw when unavailable (e.g., H2 tests).
    public int borrowBook(int bookId, int memberId, int days) throws SQLException {
        try (CallableStatement cs = conn.prepareCall("{call borrow_book(?,?,?)}")) {
            cs.setInt(1, bookId); cs.setInt(2, memberId); cs.setInt(3, days);
            cs.execute();
        } catch (SQLException ex) {
            // fallback path (for H2 or when procs not installed)
            return loanDao.createLoanRaw(bookId, memberId, days);
        }
        // fetch last insert id
        try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM loans ORDER BY id DESC LIMIT 1");
             ResultSet rs = ps.executeQuery()) {
            rs.next(); return rs.getInt(1);
        }
    }

    public double returnBook(int loanId, double dailyFine) throws SQLException {
        try (CallableStatement cs = conn.prepareCall("{call return_book(?,?)}")) {
            cs.setInt(1, loanId); cs.setDouble(2, dailyFine);
            cs.execute();
            try (PreparedStatement ps = conn.prepareStatement("SELECT fine_amount FROM loans WHERE id=?")) {
                ps.setInt(1, loanId);
                try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getDouble(1); }
            }
        } catch (SQLException ex) {
            return loanDao.returnLoanRaw(loanId, dailyFine);
        }
    }

    public List<Loan> listOverdue() throws SQLException { return loanDao.overdue(); }
}
