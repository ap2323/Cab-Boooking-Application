package com.goride.models;

import java.sql.Timestamp;

public class Payment {
    private int paymentId;
    private int rideId;
    private int driverId;
    private int userId;
    private double amount;
    private String paymentStatus;
    private Timestamp createdAt;

    public Payment(int paymentId, int rideId, int driverId, int userId ,double amount, String paymentStatus) {
        this(rideId, driverId, userId, amount, paymentStatus);
        this.paymentId = paymentId;
    }

    public Payment(int rideId, int driverId, int userId, double amount, String paymentStatus){
        this.rideId = rideId;
        this.driverId = driverId;
        this.userId = userId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    // Getters and setters

    public int getPaymentId() {
        return paymentId;
    }

    public int getRideId() {
        return rideId;
    }

    public int getDriverId() {
        return driverId;
    }

    public int getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}
