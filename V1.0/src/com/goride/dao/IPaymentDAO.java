package com.goride.dao;

import com.goride.models.Payment;

import java.sql.SQLException;
import java.util.List;

public interface IPaymentDAO {
    void createPayment(Payment payment) throws SQLException;
    List<Payment> getPaymentHistoryForUser(int userId) throws SQLException;
    List<Payment> getPaymentHistoryForDriver(int driverId) throws SQLException;

}
