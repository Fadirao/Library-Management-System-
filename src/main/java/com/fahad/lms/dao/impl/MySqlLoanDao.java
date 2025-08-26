package com.fahad.lms.dao.impl;

import com.fahad.lms.dao.LoanDao;
import com.fahad.lms.model.Loan;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MySqlLoanDao implements LoanDao {
    private final Connection conn;
    public MySqlLoanDao(Connection conn) { this.conn = conn; }

    // Fallback raw flow (used for tests/H2) — MySQL repo uses stored proc instead.
    @Override public int createLoanRaw(int bookId, int memberId, int days) throws SQLException {
        conn.setAutoCommit(false);
        try (PreparedStatement ps1 = conn.prepareStatement(
                "INSERT INTO loans(book_id,member_id,borrowed_at,due_at) VALUES(?,?,NOW(), DATE_ADD(NOW(), INTERVAL ? DAY))",
                Statement.RETURN_GENERATED_KEYS);
             PreparedStatement ps2 = conn.prepareStatement(
                "UPDATE books SET available_copies = available_copies - 1 WHERE id=? AND available_copies > 0")) {
            ps1.setInt(1, bookId); ps1.setInt(2, memberId); ps1.setInt(3, days);
            ps1.executeUpdate();
            try (ResultSet rs = ps1.getGeneratedKeys()) {
                rs.next();
                int id = rs.getInt(1);
                ps2.setInt(1, bookId);
                if (ps2.executeUpdate() == 0) throw new SQLException("No copies available");
                conn.commit();
                return id;
            }
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally { conn.setAutoCommit(true); }
    }

    @Override public List<Loan> overdue() throws SQLException {
        String sql = "SELECT * FROM loans WHERE returned_at IS NULL AND due_at < NOW() ORDER BY due_at ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Loan> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        }
    }

    @Override public double returnLoanRaw(int loanId, double dailyFine) throws SQLException {
        conn.setAutoCommit(false);
        try (PreparedStatement sel = conn.prepareStatement("SELECT book_id,due_at FROM loans WHERE id=? FOR UPDATE");
             PreparedStatement updL = conn.prepareStatement("UPDATE loans SET returned_at=NOW(), fine_amount=? WHERE id=?");
             PreparedStatement updB = conn.prepareStatement("UPDATE books SET available_copies = available_copies + 1 WHERE id=?")) {
            sel.setInt(1, loanId);
            try (ResultSet rs = sel.executeQuery()) {
                if (!rs.next()) throw new SQLException("Loan not found");
                int bookId = rs.getInt(1);
                Timestamp due = rs.getTimestamp(2);
                long daysLate = Math.max(0, (System.currentTimeMillis() - due.getTime()) / (1000*60*60*24));
                double fine = daysLate * dailyFine;
                updL.setDouble(1, fine); updL.setInt(2, loanId); updL.executeUpdate();
                updB.setInt(1, bookId); updB.executeUpdate();
                conn.commit();
                return fine;
            }
        } catch (SQLException ex) {
            conn.rollback(); throw ex;
        } finally { conn.setAutoCommit(true); }
    }

    private Loan map(ResultSet rs) throws SQLException {
        Loan l = new Loan();
        l.id = rs.getInt("id");
        l.bookId = rs.getInt("book_id");
        l.memberId = rs.getInt("member_id");
        Timestamp b = rs.getTimestamp("borrowed_at");
        Timestamp d = rs.getTimestamp("due_at");
        Timestamp r = rs.getTimestamp("returned_at");
        l.borrowedAt = b==null? null : b.toLocalDateTime();
        l.dueAt = d==null? null : d.toLocalDateTime();
        l.returnedAt = r==null? null : r.toLocalDateTime();
        l.fineAmount = rs.getDouble("fine_amount");
        return l;
    }
}
