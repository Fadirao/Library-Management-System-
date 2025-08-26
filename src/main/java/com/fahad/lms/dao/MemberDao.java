package com.fahad.lms.dao;

import com.fahad.lms.model.Member;
import java.sql.SQLException;
import java.util.List;

public interface MemberDao {
    int create(Member m) throws SQLException;
    Member findById(int id) throws SQLException;
    List<Member> findAll() throws SQLException;
}
