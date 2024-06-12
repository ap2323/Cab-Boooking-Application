package com.goride.dao;

import com.goride.models.Driver;

import java.sql.SQLException;
import java.util.List;

public interface IDriverDAO extends  IUserDAO {
    List<Driver> getAvailableDriversAtLocation(String pickupLocation) throws SQLException;
    Driver getDriver(int driverId) throws SQLException;
    void updateDriverStatus(int driverId, String status) throws SQLException;
    void changeCurrentLocation(int driverId, String newLocation) throws SQLException;
    void changeCabDetails(int driverId, String newCabModel, String newLicenseNumber) throws SQLException;
}
