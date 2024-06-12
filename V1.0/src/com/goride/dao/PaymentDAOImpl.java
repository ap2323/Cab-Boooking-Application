package com.goride.dao;

import com.goride.databaseconfiguration.DatabaseConfiguration;
import com.goride.models.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PaymentDAOImpl implements IPaymentDAO{
    // Create Payment
    public void createPayment(Payment payment) throws SQLException {
        String query = "INSERT INTO Payments (ride_id, user_id, driver_id, amount, payment_method, payment_status, transaction_id ,created_at) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        try(Connection conn = DatabaseConfiguration.getConnection()){

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, payment.getRideId());
            stmt.setInt(2, payment.getUserId());
            stmt.setInt(3, payment.getDriverId());
            stmt.setDouble(4, payment.getAmount());
            stmt.setString(5, "ONLINE");
            stmt.setString(6, payment.getPaymentStatus());
            stmt.setString(7, generateTransactionID());
            stmt.executeUpdate();
        }
    }

    public List<Payment> getPaymentHistoryForUser(int userId) throws SQLException{
        List<Payment> payments = new ArrayList<>();

        try(Connection connection = DatabaseConfiguration.getConnection()) {

            String query = "SELECT * FROM Payments p INNER JOIN Rides r ON p.ride_id = r.ride_id WHERE r.user_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                payments.add(mapPayment(resultSet));
            }
        }
        return payments;
    }

    private Payment mapPayment(ResultSet resultSet) throws SQLException {
        Payment payment = new Payment(
                resultSet.getInt("payment_id"),
                resultSet.getInt("ride_id"),
                resultSet.getInt("driver_id"),
                resultSet.getInt("user_id"),
                Double.parseDouble(String.valueOf(resultSet.getBigDecimal("amount"))),
                resultSet.getString("payment_status")
        );
        payment.setCreatedAt(resultSet.getTimestamp("created_at"));
        return payment;
    }

    private static String generateTransactionID() {
        String prefix = "transact@";
        String suffix = generateRandomAlphanumeric(); // Generate a random 8-character alphanumeric string
        return prefix + suffix;
    }

    private static String generateRandomAlphanumeric() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }

    public List<Payment> getPaymentHistoryForDriver(int driverId) throws SQLException{

        List<Payment> payments = new ArrayList<>();

        try (Connection connection = DatabaseConfiguration.getConnection()){
            String query = "SELECT * FROM Payments p INNER JOIN Rides r ON p.ride_id = r.ride_id WHERE r.driver_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, driverId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                payments.add(mapPayment(resultSet));
            }
        }
        return payments;
    }
}

