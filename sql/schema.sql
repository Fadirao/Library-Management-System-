-- MySQL schema for Library Management System
CREATE DATABASE IF NOT EXISTS lms CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE lms;

CREATE TABLE IF NOT EXISTS authors (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS books (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  author_id INT NOT NULL,
  category_id INT NOT NULL,
  isbn VARCHAR(20) UNIQUE,
  published_year INT,
  total_copies INT NOT NULL DEFAULT 1,
  available_copies INT NOT NULL DEFAULT 1,
  description TEXT,
  FULLTEXT KEY ft_title_desc (title, description),
  INDEX idx_author (author_id),
  INDEX idx_category (category_id),
  FOREIGN KEY (author_id) REFERENCES authors(id),
  FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS members (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(120) NOT NULL UNIQUE,
  joined_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS loans (
  id INT AUTO_INCREMENT PRIMARY KEY,
  book_id INT NOT NULL,
  member_id INT NOT NULL,
  borrowed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  due_at DATETIME NOT NULL,
  returned_at DATETIME NULL,
  fine_amount DECIMAL(10,2) DEFAULT 0.00,
  INDEX idx_member (member_id),
  INDEX idx_book (book_id),
  FOREIGN KEY (book_id) REFERENCES books(id),
  FOREIGN KEY (member_id) REFERENCES members(id)
) ENGINE=InnoDB;
