package com.goride.databaseconfiguration;

import com.goride.exceptions.UnableToReadException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseConfiguration {
    private static final String URL;
    private static final String DATABASE_USER;
    private static final String DATABASE_PASSWORD;
    static {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConfiguration.class.getClassLoader().getResourceAsStream("dbconfig.properties")) {
            properties.load(input);
            String database_name = properties.getProperty("database.name");
            DATABASE_USER= properties.getProperty("database.user");
            DATABASE_PASSWORD = properties.getProperty("database.password");
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Initial connection to create the database if not exists
            String initialURL = "jdbc:mysql://localhost:3306/?useUnicode=true&characterEncoding=UTF-8";
            try (Connection initialConnection = DriverManager.getConnection(initialURL, DATABASE_USER, DATABASE_PASSWORD)) {
                createDatabase(initialConnection, database_name);
            }
            URL = "jdbc:mysql://localhost:3306/" + database_name + "?useUnicode=true&characterEncoding=UTF-8";
            createTables();

        } catch (IOException | ClassNotFoundException | SQLException ex) {
            throw new UnableToReadException("Database load failed!.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, DATABASE_USER, DATABASE_PASSWORD);
    }

    private static void createDatabase(Connection initialConnection, String database_name) throws SQLException {
        String query = "CREATE DATABASE IF NOT EXISTS " + database_name;
        try(Statement statement = initialConnection.createStatement()){
            statement.executeUpdate(query);
        }
    }

    private static void createTables() throws SQLException {
        String[] table_names = new String[]{"Users", "Drivers", "Rides", "Locations", "Payments", "Ratings"};
        String query;

        try (Connection connection = getConnection()) {
            for (String table_name : table_names) {
                switch (table_name) {
                    case "Users":
                        query = "CREATE TABLE IF NOT EXISTS Users (" +
                                "user_id INT PRIMARY KEY AUTO_INCREMENT, " +
                                "username VARCHAR(255) NOT NULL, " +
                                "email VARCHAR(255) NOT NULL UNIQUE, " +
                                "phone_number BIGINT NOT NULL, " +
                                "password VARCHAR(255) NOT NULL, " +
                                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP )";
                        break;
                    case "Drivers":
                        query = "CREATE TABLE IF NOT EXISTS Drivers (" +
                                "driver_id INT PRIMARY KEY AUTO_INCREMENT, " +
                                "name VARCHAR(255) NOT NULL, " +
                                "email VARCHAR(255) NOT NULL UNIQUE, " +
                                "phone_number BIGINT NOT NULL, " +
                                "password VARCHAR(255) NOT NULL, " +
                                "car_model VARCHAR(255) NOT NULL, " +
                                "license_number VARCHAR(255) NOT NULL, " +
                                "status VARCHAR(50) NOT NULL, " +
                                "current_location VARCHAR(255), " +
                                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP )";
                        break;
                    case "Rides":
                        query = "CREATE TABLE IF NOT EXISTS Rides (" +
                                "ride_id INT PRIMARY KEY AUTO_INCREMENT, " +
                                "user_id INT NOT NULL, " +
                                "driver_id INT NOT NULL, " +
                                "pickup_location VARCHAR(255) NOT NULL, " +
                                "dropoff_location VARCHAR(255) NOT NULL, " +
                                "status VARCHAR(50) NOT NULL, " +
                                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                                "FOREIGN KEY (user_id) REFERENCES Users(user_id), " +
                                "FOREIGN KEY (driver_id) REFERENCES Drivers(driver_id) )";
                        break;
                    case "Locations":
                        query = "CREATE TABLE IF NOT EXISTS Locations (" +
                                "location_id INT PRIMARY KEY AUTO_INCREMENT, " +
                                "name VARCHAR(255) NOT NULL, " +
                                "latitude DECIMAL(9,6) NOT NULL, " +
                                "longitude DECIMAL(9,6) NOT NULL, " +
                                "city_name VARCHAR(255) NOT NULL, " +
                                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP )";
                        break;
                    case "Payments":
                        query = "CREATE TABLE IF NOT EXISTS Payments (" +
                                "payment_id INT PRIMARY KEY AUTO_INCREMENT, " +
                                "ride_id INT NOT NULL, " +
                                "user_id INT NOT NULL, " +
                                "driver_id INT NOT NULL, " +
                                "amount DECIMAL(10, 2) NOT NULL, " +
                                "payment_method VARCHAR(50) NOT NULL, " +
                                "payment_status VARCHAR(50) NOT NULL, " +
                                "transaction_id VARCHAR(255) UNIQUE, " +
                                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                                "FOREIGN KEY (ride_id) REFERENCES Rides(ride_id), " +
                                "FOREIGN KEY (user_id) REFERENCES Users(user_id), " +
                                "FOREIGN KEY (driver_id) REFERENCES Drivers(driver_id) )";
                        break;
                    case "Ratings":
                        query = "CREATE TABLE IF NOT EXISTS Ratings (" +
                                "rating_id INT PRIMARY KEY AUTO_INCREMENT, " +
                                "ride_id INT NOT NULL, " +
                                "user_id INT NOT NULL, " +
                                "driver_id INT NOT NULL, " +
                                "rating INT CHECK (rating >= 1 AND rating <= 5), " +
                                "comment TEXT, " +
                                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                                "FOREIGN KEY (ride_id) REFERENCES Rides(ride_id), " +
                                "FOREIGN KEY (user_id) REFERENCES Users(user_id), " +
                                "FOREIGN KEY (driver_id) REFERENCES Drivers(driver_id) )";
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected table name: " + table_name);
                }

                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.executeUpdate();
                }
            }
        }
    }



}
