package com.fahad.lms.dao;

import com.fahad.lms.model.Loan;
import java.sql.SQLException;
import java.util.List;

public interface LoanDao {
    int createLoanRaw(int bookId, int memberId, int days) throws SQLException; // non-proc fallback
    List<Loan> overdue() throws SQLException;
    double returnLoanRaw(int loanId, double dailyFine) throws SQLException; // non-proc fallback
}
