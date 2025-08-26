package com.fahad.lms.model;

public class Member {
    public int id;
    public String name;
    public String email;
    @Override public String toString() { return "Member{id=%d, name='%s'}".formatted(id, name); }
}
