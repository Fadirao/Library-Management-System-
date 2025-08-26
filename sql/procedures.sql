USE lms;

DELIMITER $$
CREATE PROCEDURE borrow_book(IN p_book_id INT, IN p_member_id INT, IN p_days INT)
BEGIN
  DECLARE v_available INT;
  SELECT available_copies INTO v_available FROM books WHERE id = p_book_id FOR UPDATE;
  IF v_available IS NULL THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Book not found';
  END IF;
  IF v_available <= 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No copies available';
  END IF;
  INSERT INTO loans (book_id, member_id, borrowed_at, due_at)
  VALUES (p_book_id, p_member_id, NOW(), DATE_ADD(NOW(), INTERVAL p_days DAY));
  UPDATE books SET available_copies = available_copies - 1 WHERE id = p_book_id;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE return_book(IN p_loan_id INT, IN p_daily_fine DECIMAL(10,2))
BEGIN
  DECLARE v_book INT;
  DECLARE v_due DATETIME;
  DECLARE v_days_late INT;
  SELECT book_id, due_at INTO v_book, v_due FROM loans WHERE id = p_loan_id FOR UPDATE;
  IF v_book IS NULL THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Loan not found';
  END IF;
  SET v_days_late = GREATEST(DATEDIFF(NOW(), v_due), 0);
  UPDATE loans
    SET returned_at = NOW(),
        fine_amount = v_days_late * p_daily_fine
  WHERE id = p_loan_id;
  UPDATE books SET available_copies = available_copies + 1 WHERE id = v_book;
END $$
DELIMITER ;
