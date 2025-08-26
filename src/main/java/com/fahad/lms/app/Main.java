package com.fahad.lms.app;

import com.fahad.lms.service.LibraryService;
import com.fahad.lms.util.Db;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        try (Connection conn = Db.getDataSource().getConnection()) {
            LibraryService svc = new LibraryService(conn);
            Scanner sc = new Scanner(System.in);
            System.out.println("=== Library Management System (Java + MySQL) ===");
            while (true) {
                System.out.println("\n1) List books  2) Search  3) Register member  4) Borrow  5) Return  6) Overdue  0) Exit");
                System.out.print("Choose: ");
                String choice = sc.nextLine().trim();
                if (choice.equals("0")) break;
                switch (choice) {
                    case "1" -> svc.listBooks().forEach(System.out::println);
                    case "2" -> {
                        System.out.print("Keyword: ");
                        String kw = sc.nextLine();
                        svc.searchBooks(kw).forEach(System.out::println);
                    }
                    case "3" -> {
                        System.out.print("Member name: ");
                        String name = sc.nextLine();
                        System.out.print("Member email: ");
                        String email = sc.nextLine();
                        int id = svc.registerMember(name, email);
                        System.out.println("Registered member id=" + id);
                    }
                    case "4" -> {
                        System.out.print("Book id: ");
                        int bid = Integer.parseInt(sc.nextLine());
                        System.out.print("Member id: ");
                        int mid = Integer.parseInt(sc.nextLine());
                        System.out.print("Days to borrow (e.g., 14): ");
                        int days = Integer.parseInt(sc.nextLine());
                        int loanId = svc.borrowBook(bid, mid, days);
                        System.out.println("Loan created id=" + loanId);
                    }
                    case "5" -> {
                        System.out.print("Loan id: ");
                        int lid = Integer.parseInt(sc.nextLine());
                        System.out.print("Daily fine (e.g., 2.0): ");
                        double fine = Double.parseDouble(sc.nextLine());
                        double fee = svc.returnBook(lid, fine);
                        System.out.println("Returned. Fine charged=" + fee);
                    }
                    case "6" -> svc.listOverdue().forEach(System.out::println);
                    default -> System.out.println("Invalid");
                }
            }
            System.out.println("Bye.");
        }
    }
}
