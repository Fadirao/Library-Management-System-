package com.fahad.lms;

import org.junit.jupiter.api.*;
import java.sql.*;
import java.nio.file.*;
import java.util.stream.Collectors;

public class BaseTest {
    protected static Connection conn;

    @BeforeAll
    static void setup() throws Exception {
        Class.forName("org.h2.Driver");
        conn = DriverManager.getConnection("jdbc:h2:mem:lms;MODE=MySQL;DB_CLOSE_DELAY=-1", "sa", "");
        String sql = Files.lines(Paths.get("sql/h2/schema-h2.sql")).collect(Collectors.joining("\n"));
        try (Statement st = conn.createStatement()) { st.execute(sql); }
    }

    @AfterAll
    static void teardown() throws Exception {
        if (conn != null) conn.close();
    }
}
