USE lms;

CREATE OR REPLACE VIEW v_overdue_loans AS
SELECT l.id AS loan_id, b.title, m.name AS member_name, l.due_at
FROM loans l
JOIN books b ON b.id = l.book_id
JOIN members m ON m.id = l.member_id
WHERE l.returned_at IS NULL AND l.due_at < NOW();

CREATE OR REPLACE VIEW v_member_activity AS
SELECT m.id AS member_id, m.name, COUNT(l.id) AS total_loans
FROM members m
LEFT JOIN loans l ON l.member_id = m.id
GROUP BY m.id, m.name
ORDER BY total_loans DESC;
