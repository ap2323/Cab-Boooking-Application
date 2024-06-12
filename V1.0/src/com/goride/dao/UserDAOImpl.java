package com.goride.dao;

import com.goride.databaseconfiguration.DatabaseConfiguration;
import com.goride.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements IUserDAO {

    // Register User
    public void register(User user) throws SQLException{
        String query = "INSERT INTO Users (username, email, phone_number, password, created_at, updated_at) VALUES (?, ?, ?, ?, NOW(), NOW())";
        try(Connection conn = DatabaseConfiguration.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setLong(3, user.getPhoneNumber());
            stmt.setString(4, user.getPassword());
            stmt.executeUpdate();

        }
    }
    public User login(String email, String password) throws SQLException {
        String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
        try(Connection conn = DatabaseConfiguration.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getLong("phone_number"),
                        rs.getString("password")
                );
            }
        }
        return null;
    }

    public boolean isRegistered(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM Users WHERE email = ?";

        try (Connection conn = DatabaseConfiguration.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
        return false;
    }
}
