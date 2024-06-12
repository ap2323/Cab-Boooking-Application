package com.goride.dao;

import com.goride.models.User;

import java.sql.SQLException;

public interface IUserDAO {
    void register(User user) throws SQLException;
    User login(String mail, String password) throws SQLException;
    boolean isRegistered(String email) throws SQLException;
}
