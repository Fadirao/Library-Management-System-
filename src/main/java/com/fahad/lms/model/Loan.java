package com.fahad.lms.model;

import java.time.LocalDateTime;

public class Loan {
    public int id;
    public int bookId;
    public int memberId;
    public LocalDateTime borrowedAt;
    public LocalDateTime dueAt;
    public LocalDateTime returnedAt;
    public double fineAmount;
    @Override public String toString() {
        return "Loan{id=%d, bookId=%d, memberId=%d, due=%s, returned=%s, fine=%.2f}"
                .formatted(id, bookId, memberId, dueAt, returnedAt, fineAmount);
    }
}
