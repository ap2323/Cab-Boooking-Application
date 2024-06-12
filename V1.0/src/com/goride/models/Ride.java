package com.goride.models;

import java.sql.Timestamp;

public class Ride {
    private int rideId;
    private int userId;
    private int driverId;
    private String pickupLocation;
    private String dropoffLocation;
    private String rideStatus;
    private Timestamp createdAt;

    public Ride(int rideId, int userId, int driverId, String pickupLocation, String dropoffLocation, String rideStatus) {
        this.rideId = rideId;
        this.userId = userId;
        this.driverId = driverId;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.rideStatus = rideStatus;
    }
    // Getters and setters

    public int getRideId() {
        return rideId;
    }

    public int getUserId() {
        return userId;
    }

    public int getDriverId() {
        return driverId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public String getRideStatus() {
        return rideStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public void setRideStatus(String rideStatus) {
        this.rideStatus = rideStatus;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

