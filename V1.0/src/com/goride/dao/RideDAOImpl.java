package com.goride.dao;

import com.goride.databaseconfiguration.DatabaseConfiguration;
import com.goride.models.Ride;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RideDAOImpl implements IRideDAO{
    // Create Ride
    public int createRide(int userId, int driverId, String pickupLocation, String dropoffLocation) throws SQLException{
        String query = "INSERT INTO Rides (user_id, driver_id, pickup_location, dropoff_location, status, created_at) VALUES (?, ?, ?, ?, ?, NOW())";
        try(Connection conn = DatabaseConfiguration.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, userId);
            stmt.setInt(2, driverId);
            stmt.setString(3, pickupLocation);
            stmt.setString(4, dropoffLocation);
            stmt.setString(5, "BOOKED");
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        }
        return -1;
    }

    // Update Ride Status
    public void updateRideStatus(int rideId, String status) throws SQLException{
        String query = "UPDATE Rides SET status = ? WHERE ride_id = ?";
        try (Connection conn = DatabaseConfiguration.getConnection()){
             PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, status);
            stmt.setInt(2, rideId);
            stmt.executeUpdate();
        }
    }
    public List<Ride> getUserRideHistory(int userId) throws SQLException{
        List<Ride> rides = new ArrayList<>();

        try(Connection connection = DatabaseConfiguration.getConnection()) {
            String query = "SELECT * FROM Rides WHERE user_id = ? AND status = 'COMPLETED'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                rides.add(mapRide(resultSet));
            }
        }
        return rides;
    }

    public Ride getCurrentRideForUser(int userId) throws SQLException{

        try (Connection connection = DatabaseConfiguration.getConnection()){

            String query = "SELECT * FROM Rides WHERE user_id = ? AND status = 'BOOKED' OR status = 'ONGOING'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapRide(resultSet);
            }
        }
        return null;
    }
    public Ride getCurrentRideForDriver(int driverId)throws SQLException {

        try(Connection connection = DatabaseConfiguration.getConnection()) {

            String query = "SELECT * FROM Rides WHERE driver_id = ? AND status = 'BOOKED'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, driverId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return mapRide(resultSet);
            }
        }
        return null;
    }

    private Ride mapRide(ResultSet resultSet) throws SQLException {
        Ride ride = new Ride(
                resultSet.getInt("ride_id"),
                resultSet.getInt("user_id"),
                resultSet.getInt("driver_id"),
                resultSet.getString("pickup_location"),
                resultSet.getString("dropoff_location"),
                resultSet.getString("status")
        );
        ride.setCreatedAt(resultSet.getTimestamp("created_at"));
        return ride;
    }

    public Ride getCompletedRideForUser(int userId) throws SQLException{
        try(Connection connection = DatabaseConfiguration.getConnection()) {

            String query = "SELECT * FROM Rides WHERE user_id = ? AND status = 'COMPLETED' ORDER BY ride_id DESC";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapRide(resultSet);
            }
        }
        return null;
    }
}

