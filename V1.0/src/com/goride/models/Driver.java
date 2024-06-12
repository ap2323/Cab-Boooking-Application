package com.goride.models;

import java.sql.Timestamp;

public class Driver extends User {
    private String carModel;
    private String licenseNumber;
    private String current_location;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructor to initialize all fields
    public Driver(String name, String email, String password , long phoneNumber,String carModel, String licenseNumber) {
        super(name, email, phoneNumber,password);
        this.carModel = carModel;
        this.licenseNumber = licenseNumber;
    }
    // Getters and setters
    public int getDriverId() {
        return super.getUserId();
    }

    public void setDriverId(int driverId) {
        super.setUserId(driverId);
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCurrent_location(String current_location) {
        this.current_location = current_location;
    }
}
