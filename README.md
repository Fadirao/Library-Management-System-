# Library Management System (Java + MySQL)

A clean **Java OOP + MySQL** project featuring a layered architecture (DAO → Service → CLI),
transactional borrow/return flows, indexing for search performance, and database-side automation
(stored procedures, triggers, and views). Tests run on **H2 (MySQL mode)** for fast CI.

## Tech
- Java 17+, Maven, JDBC, HikariCP pool, SLF4J (Logback)
- MySQL 8 (Docker Compose included) + Adminer
- JUnit 5 (H2 in MySQL mode for tests)

## Run (MySQL via Docker)
```bash
docker compose up -d   # starts mysql:8 and adminer
# Apply schema + data
mysql -h 127.0.0.1 -P 3306 -u root -proot lms < sql/schema.sql
mysql -h 127.0.0.1 -P 3306 -u root -proot lms < sql/data.sql
mysql -h 127.0.0.1 -P 3306 -u root -proot lms < sql/procedures.sql
mysql -h 127.0.0.1 -P 3306 -u root -proot lms < sql/views.sql
mysql -h 127.0.0.1 -P 3306 -u root -proot lms < sql/triggers.sql

# Build & run CLI
mvn -q -DskipTests package
java -jar target/library-management-system-1.0.0.jar
```

Adminer UI: http://localhost:8080 (system: MySQL, server: db, user: root, pass: root, db: lms)

## CLI Demo (Main menu)
- List books / search by keyword
- Register member
- Borrow / Return book (calls DB stored procedures)
- Reports: Top borrowers, Overdue loans

## Tests
```bash
mvn -q -DskipITs test
```

## Structure
```
src/main/java/com/fahad/lms/
  app/Main.java                # CLI
  model/Book.java, Member.java, Author.java, Category.java, Loan.java
  dao/* interfaces + dao/impl/* JDBC implementations
  service/LibraryService.java  # orchestrates workflows
  util/Db.java                 # HikariCP DataSource

sql/
  schema.sql, data.sql, procedures.sql, views.sql, triggers.sql
  h2/schema-h2.sql             # simplified for tests

reports/
  overdue_loans.csv, author_counts.csv  # generated from sample data
```
