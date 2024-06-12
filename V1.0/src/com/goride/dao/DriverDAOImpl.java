package com.goride.dao;

import com.goride.databaseconfiguration.DatabaseConfiguration;
import com.goride.models.Driver;
import com.goride.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverDAOImpl implements IDriverDAO{
    // Login Driver
    public User login(String email, String password) throws SQLException {

        try(Connection connection = DatabaseConfiguration.getConnection()) {

            String query = "SELECT * FROM Drivers WHERE email = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, email);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        User driver = new Driver(
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                resultSet.getLong("phone_number"),
                                resultSet.getString("car_model"),
                                resultSet.getString("license_number")
                        );
                        driver.setUserId(resultSet.getInt("driver_id"));
                        return driver;
                    }
                }
            }
        }
        return null;
    }

    // Register Driver
    public void register(User driver) throws SQLException {
        Driver driver1 = (Driver) driver;
        try( Connection conn = DatabaseConfiguration.getConnection()) {
            // Register as user first

            String userQuery = "INSERT INTO Users (username, email, phone_number, password, created_at, updated_at) VALUES (?, ?, ?, ?, NOW(), NOW())";
            try (PreparedStatement userStmt = conn.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, driver1.getUsername());
                userStmt.setString(2, driver1.getEmail());
                userStmt.setLong(3, driver1.getPhoneNumber());
                userStmt.setString(4, driver1.getPassword());
                userStmt.executeUpdate();

                ResultSet generatedKeys = userStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    // Register as driver
                    String driverQuery = "INSERT INTO Drivers (driver_id, name, email, phone_number, password, car_model, license_number, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, 'AVAILABLE', NOW(), NOW())";
                    try (PreparedStatement driverStmt = conn.prepareStatement(driverQuery)) {
                        driverStmt.setInt(1, userId);
                        driverStmt.setString(2, driver1.getUsername());
                        driverStmt.setString(3, driver1.getEmail());
                        driverStmt.setLong(4, driver1.getPhoneNumber());
                        driverStmt.setString(5, driver1.getPassword());
                        driverStmt.setString(6, driver1.getCarModel());
                        driverStmt.setString(7, driver1.getLicenseNumber());
                        driverStmt.executeUpdate();
                    }
                }
            }
        }
    }
    // Get Available Drivers at a Specific Location
    public List<Driver> getAvailableDriversAtLocation(String pickupLocation) throws SQLException {
        List<Driver> drivers = new ArrayList<>();

        String query = "SELECT d.driver_id, d.name, d.email, d.password, d.phone_number, d.car_model, d.license_number, SUM(r.rating) AS sum_rating " +
                "FROM Drivers d " +
                "LEFT JOIN Ratings r ON d.driver_id = r.driver_id " +
                "WHERE d.status = 'AVAILABLE' AND d.current_location= ?" +
                "GROUP BY d.driver_id, d.name, d.email, d.password, d.phone_number, d.car_model, d.license_number " +
                "ORDER BY sum_rating DESC";

        try( Connection conn = DatabaseConfiguration.getConnection()) {

             PreparedStatement stmt = conn.prepareStatement(query);
             stmt.setString(1, pickupLocation);
             ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Driver driver = new Driver(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getLong("phone_number"),
                        rs.getString("car_model"),
                        rs.getString("license_number")
                );
                driver.setDriverId(rs.getInt("driver_id"));
                drivers.add(driver);
            }
        }
        return drivers;
    }
    // Get Driver by ID
    public Driver getDriver(int driverId) throws SQLException {
        String query = "SELECT * FROM Drivers WHERE driver_id = ?";
        try (Connection conn = DatabaseConfiguration.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, driverId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Driver(
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getLong("phone_number"),
                            rs.getString("car_model"),
                            rs.getString("license_number")
                    );
                }
            }
        }
        return null;
    }

    // Update Driver Status
    public void updateDriverStatus(int driverId, String status) throws SQLException {
        String query = "UPDATE Drivers SET status = ? WHERE driver_id = ?";
        try (Connection conn = DatabaseConfiguration.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, status);
            stmt.setInt(2, driverId);
            stmt.executeUpdate();
        }
    }

    // Change Driver's Current Location
    public void changeCurrentLocation(int driverId, String newLocation) throws SQLException{
        String query = "UPDATE Drivers SET current_location = ?, updated_at = NOW() WHERE driver_id = ?";
        try( Connection conn = DatabaseConfiguration.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newLocation);
            stmt.setInt(2, driverId);
            stmt.executeUpdate();
            System.out.println("Driver's location updated successfully.");
        }
    }

    public boolean isRegistered(String mailID) throws SQLException{
        String query = "SELECT COUNT(*) FROM Drivers WHERE email = ?";

        try (Connection conn = DatabaseConfiguration.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, mailID);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
        return false;
    }

    public void changeCabDetails(int driverId, String newCabModel, String newLicenseNumber) throws SQLException{
        String query = "UPDATE Drivers SET car_model = ?, license_number = ? WHERE driver_id = ?";

        try (Connection conn = DatabaseConfiguration.getConnection()){
             PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newCabModel);
            stmt.setString(2, newLicenseNumber);
            stmt.setInt(3, driverId);

            stmt.executeUpdate();
        }
    }
}
