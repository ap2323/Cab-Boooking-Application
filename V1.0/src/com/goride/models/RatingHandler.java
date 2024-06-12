package com.goride.models;

public class RatingHandler {
    private int ratingId;
    private int rideID;
    private int userId;
    private int driverId;
    private int ratingScore;
    private String comments;

    // Constructor with all fields
    public RatingHandler(int ratingId, int rideID , int userId, int driverId, int ratingScore, String comments) {
        this(userId, rideID, driverId, ratingScore, comments);
        this.ratingId = ratingId;
    }
    public RatingHandler(int userId, int rideID, int driverId, int ratingScore, String comments) {
        this.userId = userId;
        this.rideID = rideID;
        this.driverId = driverId;
        this.ratingScore = ratingScore;
        this.comments = comments;
    }

    // Getters and Setters
    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(int ratingScore) {
        if (ratingScore < 1 || ratingScore > 5) {
            throw new IllegalArgumentException("Rating score must be between 1 and 5.");
        }
        this.ratingScore = ratingScore;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getRideID() {
        return rideID;
    }
}
