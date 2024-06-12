package com.goride.dao;

import com.goride.databaseconfiguration.DatabaseConfiguration;
import com.goride.models.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationDAOImpl implements ILocationDAO{
    // Helper method to get Location by name
    public Location getLocationByName(String locationName) throws SQLException {
        String query = "SELECT * FROM Locations WHERE name = ?";
        try (Connection conn = DatabaseConfiguration.getConnection()){
             PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, locationName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Location(
                        rs.getInt("location_id"),
                        rs.getString("name"),
                        rs.getBigDecimal("latitude"),
                        rs.getBigDecimal("longitude"),
                        rs.getString("city_name"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
            }
        }
        return null;
    }
    // Check if Location is Valid
    public boolean isValidLocation(String location) throws SQLException{
        String query = "SELECT COUNT(*) FROM Locations WHERE name = ?";
        try (Connection conn = DatabaseConfiguration.getConnection()){
             PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, location);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // If count > 0, location exists
            }
        }
        return false; // Location not found
    }

    // Check if City is Valid
    public boolean isValidCity(String cityName) throws SQLException {
        String query = "SELECT COUNT(*) FROM Locations WHERE city_name = ?";
        try (Connection conn = DatabaseConfiguration.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, cityName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // If count > 0, location exists
            }
        }
        return false; // City not found
    }
}

