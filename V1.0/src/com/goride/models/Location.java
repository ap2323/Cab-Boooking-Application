package com.goride.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Location {
    private int locationId;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String cityName;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Location(int locationId, String name, BigDecimal latitude, BigDecimal longitude, String cityName, Timestamp createdAt, Timestamp updatedAt) {
        this.locationId = locationId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cityName = cityName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public int getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public String getCityName() {
        return cityName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
}

