USE lms;

DELIMITER $$
CREATE TRIGGER trg_loans_after_insert
AFTER INSERT ON loans FOR EACH ROW
BEGIN
  UPDATE books SET available_copies = available_copies - 1 WHERE id = NEW.book_id;
END $$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER trg_loans_after_update
AFTER UPDATE ON loans FOR EACH ROW
BEGIN
  IF NEW.returned_at IS NOT NULL AND OLD.returned_at IS NULL THEN
    UPDATE books SET available_copies = available_copies + 1 WHERE id = NEW.book_id;
  END IF;
END $$
DELIMITER ;
