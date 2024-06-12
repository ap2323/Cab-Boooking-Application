package com.goride.dao;

import com.goride.models.Location;

import java.sql.SQLException;

public interface ILocationDAO {
    Location getLocationByName(String locationName) throws SQLException;
    boolean isValidLocation(String location) throws SQLException;

    boolean isValidCity(String cityName) throws SQLException;
}
