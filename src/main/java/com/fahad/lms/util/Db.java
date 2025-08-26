package com.fahad.lms.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class Db {
    private static final HikariDataSource ds;
    static {
        HikariConfig cfg = new HikariConfig();
        String url = System.getenv().getOrDefault("JDBC_URL", "jdbc:mysql://127.0.0.1:3306/lms?useSSL=false&allowPublicKeyRetrieval=true");
        String user = System.getenv().getOrDefault("JDBC_USER", "root");
        String pass = System.getenv().getOrDefault("JDBC_PASS", "root");
        cfg.setJdbcUrl(url);
        cfg.setUsername(user);
        cfg.setPassword(pass);
        cfg.setMaximumPoolSize(5);
        ds = new HikariDataSource(cfg);
    }
    public static DataSource getDataSource() { return ds; }
}
