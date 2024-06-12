package com.goride.dao;

import com.goride.models.Ride;

import java.sql.SQLException;
import java.util.List;

public interface IRideDAO {
    int createRide(int userId, int driverId, String pickupLocation, String dropoffLocation) throws SQLException;
    void updateRideStatus(int rideId, String status) throws SQLException;
    List<Ride> getUserRideHistory(int userId) throws SQLException;
    Ride getCurrentRideForUser(int userId) throws SQLException;
    Ride getCurrentRideForDriver(int driverId) throws SQLException;
    Ride getCompletedRideForUser(int userId) throws SQLException;
}
