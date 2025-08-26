package com.fahad.lms.dao.impl;

import com.fahad.lms.dao.MemberDao;
import com.fahad.lms.model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlMemberDao implements MemberDao {
    private final Connection conn;
    public MySqlMemberDao(Connection conn) { this.conn = conn; }

    @Override public int create(Member m) throws SQLException {
        String sql = "INSERT INTO members(name, email) VALUES(?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.name); ps.setString(2, m.email);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); return rs.getInt(1); }
        }
    }

    @Override public Member findById(int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT id,name,email FROM members WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Member m = new Member();
                m.id = rs.getInt("id"); m.name = rs.getString("name"); m.email = rs.getString("email");
                return m;
            }
        }
    }

    @Override public List<Member> findAll() throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT id,name,email FROM members ORDER BY id DESC")) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Member> list = new ArrayList<>();
                while (rs.next()) {
                    Member m = new Member();
                    m.id = rs.getInt("id"); m.name = rs.getString("name"); m.email = rs.getString("email");
                    list.add(m);
                }
                return list;
            }
        }
    }
}
