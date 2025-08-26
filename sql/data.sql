USE lms;

INSERT INTO authors (name) VALUES
('Haruki Murakami'),
('Isabel Allende'),
('George Orwell'),
('Jane Austen'),
('Yuval Noah Harari');

INSERT INTO categories (name) VALUES
('Fiction'), ('Non-Fiction'), ('Sci-Fi'), ('History'), ('Romance'), ('Philosophy');

INSERT INTO books (title, author_id, category_id, isbn, published_year, total_copies, available_copies, description) VALUES
('Kafka on the Shore', 1, 1, '9781400079278', 2002, 5, 5, 'A dreamlike coming-of-age tale.'),
('1984', 3, 3, '9780451524935', 1949, 8, 8, 'Dystopian surveillance state.'),
('Pride and Prejudice', 4, 5, '9780141439518', 1813, 6, 6, 'Classic romance novel.'),
('Sapiens', 5, 2, '9780062316097', 2011, 7, 7, 'Brief history of humankind.'),
('The House of the Spirits', 2, 1, '9780553383805', 1982, 4, 4, 'Family saga spanning generations.'),
('Animal Farm', 3, 3, '9780451526342', 1945, 5, 5, 'Satire on totalitarianism.'),
('Norwegian Wood', 1, 1, '9780375704024', 1987, 5, 5, 'Melancholic exploration of memory.'),
('Homo Deus', 5, 2, '9780062464316', 2015, 5, 5, 'A look into the future of humanity.');

INSERT INTO members (name, email) VALUES
('Ayesha Tariq', 'ayesha@example.com'),
('Ali Raza', 'ali@example.com'),
('Sara Khan', 'sara@example.com'),
('Bilal Ahmed', 'bilal@example.com');

-- Create a couple of loans (one overdue, one returned, one active)
-- Loan 1: Active (due in future)
INSERT INTO loans (book_id, member_id, borrowed_at, due_at) VALUES
(1, 1, '2024-01-05 10:00:00', '2024-01-19 10:00:00');

-- Loan 2: Overdue (due in past, not returned)
INSERT INTO loans (book_id, member_id, borrowed_at, due_at) VALUES
(2, 2, '2024-01-01 09:00:00', '2024-01-10 09:00:00');

-- Loan 3: Returned
INSERT INTO loans (book_id, member_id, borrowed_at, due_at, returned_at, fine_amount) VALUES
(3, 3, '2024-01-02 15:00:00', '2024-01-16 15:00:00', '2024-01-12 15:00:00', 0.00);
