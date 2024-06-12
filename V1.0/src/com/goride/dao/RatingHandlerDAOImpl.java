package com.goride.dao;

import com.goride.databaseconfiguration.DatabaseConfiguration;
import com.goride.models.RatingHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingHandlerDAOImpl implements IRatingHandlerDAO {
    public void setRating(RatingHandler ratingHandler) throws SQLException{
        String query = "INSERT INTO Ratings (ride_id, user_id, driver_id, rating, comment,created_at, updated_at) VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
        try(Connection conn = DatabaseConfiguration.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, ratingHandler.getRideID());
            stmt.setInt(2, ratingHandler.getUserId());
            stmt.setInt(3, ratingHandler.getDriverId());
            stmt.setDouble(4, ratingHandler.getRatingScore());
            stmt.setString(5, ratingHandler.getComments());
            stmt.executeUpdate();
        }
    }

    public int getRating(int driverId) throws SQLException{
        String query = "SELECT SUM(rating) FROM Ratings WHERE driver_id=?";
        try (Connection conn = DatabaseConfiguration.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, driverId);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
        }
        return 0;
    }
}
