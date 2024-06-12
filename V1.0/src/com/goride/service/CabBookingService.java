package com.goride.service;

import com.goride.dao.*;

import com.goride.models.*;

import com.goride.exceptions.UnableToReadException;
import com.goride.exceptions.UnableToWriteException;
import com.goride.exceptions.UserAlreadyFoundException;

import java.sql.SQLException;
import java.util.List;

public class CabBookingService {
    private final IUserDAO userDAOImpl = new UserDAOImpl();
    private final IDriverDAO driverDAOImpl = new DriverDAOImpl();
    private final IRideDAO rideDAOImpl = new RideDAOImpl();
    private final IPaymentDAO paymentDAOImpl = new PaymentDAOImpl();
    private final ILocationDAO locationDAOImpl = new LocationDAOImpl();
    private final IRatingHandlerDAO rateCabDAOImpl = new RatingHandlerDAOImpl();

    private static final String SqlErrorMessage = "Server Error. Please try again later.";
    private static final String mailInvalidErrorMessage = "Invalid mail format.";
    private static final String mailErrorMessage = "Mail Already Registered.";
    private static final String passwordInvalidFormatErrorMessage = "Invalid password format.";
    private static final String locationInvalidErrorMessage = "Invalid location(s).";

    public void changeCurrentLocation(int driverId, String location) {
        if(!validateLocation(location)) {
            throw new IllegalArgumentException(locationInvalidErrorMessage);
        }
        try {
            driverDAOImpl.changeCurrentLocation(driverId, location);
        } catch (SQLException ex) {
            throw new UnableToWriteException(SqlErrorMessage);
        }
    }

    // Register User
    public void registerUser(String username, String email, long phoneNumber, String password) {
        if(!Validator.isValidEmail(email)){
           throw new IllegalArgumentException(mailInvalidErrorMessage);
        }

        if(!Validator.isValidPassword(password)){
            throw new IllegalArgumentException(passwordInvalidFormatErrorMessage);
        }

        try {
            if (userDAOImpl.isRegistered(email)){
                throw new UserAlreadyFoundException(mailErrorMessage);
            }
            userDAOImpl.register(new User(username, email, phoneNumber, password));
        } catch (SQLException ex){
            throw new UnableToWriteException(SqlErrorMessage);
        }
    }
    // Register Driver
    public void registerDriver(String name, String mailID, String password , long phoneNumber,String carModel, String licenseNumber) {
        if (!Validator.isValidEmail(mailID)) {
            throw new IllegalArgumentException(mailInvalidErrorMessage);
        }

        if (!Validator.isValidPassword(password)) {
            throw new IllegalArgumentException(passwordInvalidFormatErrorMessage);
        }

        try {
            if (driverDAOImpl.isRegistered(mailID)) {
                throw new UserAlreadyFoundException(mailErrorMessage);
            }
            User driver = new Driver(name, mailID, password, phoneNumber, carModel, licenseNumber);
            driverDAOImpl.register(driver);
        } catch (SQLException ex){
            throw new UnableToWriteException(SqlErrorMessage);
        }
    }
    // User Login
    public User loginUser(String email, String password) {
        if(!Validator.isValidEmail(email)){
            throw new IllegalArgumentException(mailInvalidErrorMessage);
        }

        if(!Validator.isValidPassword(password)){
            throw new IllegalArgumentException(passwordInvalidFormatErrorMessage);
        }
        try {
            return userDAOImpl.login(email, password);
        } catch (SQLException ex){
            throw new UnableToReadException(SqlErrorMessage);
        }
    }

    public User loginDriver(String email, String password) {
        if (!Validator.isValidEmail(email)) {
            throw new IllegalArgumentException(mailInvalidErrorMessage);
        }

        if (!Validator.isValidPassword(password)){
            throw new IllegalArgumentException(passwordInvalidFormatErrorMessage);
        }
        try {
            return driverDAOImpl.login(email, password);
        } catch (SQLException ex){
            throw new UnableToReadException(SqlErrorMessage);
        }
    }

    // Notify Driver
    public void notifyDriver(int driverId) {
        Driver driver;
        try {
           driver = driverDAOImpl.getDriver(driverId);
        } catch (SQLException ex){
            throw new UnableToReadException(SqlErrorMessage);
        }
        if (driver != null) {
            System.out.println("Notification sent to driver: " + driver.getUsername());
            //send a notification via SMS, email, or another method
        }
    }

    // Book a Ride
    public int bookRide(User user, String pickupLocation, String dropoffLocation, int driverId) {
        int rideID;
        try{
            rideID = rideDAOImpl.createRide(user.getUserId(), driverId, pickupLocation, dropoffLocation);
            driverDAOImpl.updateDriverStatus(driverId, "BUSY");
        } catch (SQLException ex){
            throw new UnableToWriteException(SqlErrorMessage);
        }
        notifyDriver(driverId);
        return rideID;
    }

    // Complete Ride
    public void completeRide(int rideId, int driverId) {
        try {
            rideDAOImpl.updateRideStatus(rideId, "COMPLETED");
            driverDAOImpl.updateDriverStatus(driverId, "AVAILABLE");
        } catch (SQLException ex){
            throw new UnableToWriteException(SqlErrorMessage);
        }
    }

    // Process Payment
    public void processPayment(int rideId,int driverID, int userID,double amount) {
        try {
            paymentDAOImpl.createPayment(new Payment(rideId, driverID, userID, amount, "COMPLETED"));
        }catch (SQLException ex){
            throw new UnableToWriteException(SqlErrorMessage);
        }
    }

    // Get Available Cabs at a Location
    public List<Driver> getAvailableCabsAtLocation(String pickupLocation) {
        try {
            return driverDAOImpl.getAvailableDriversAtLocation(pickupLocation);
        } catch (SQLException ex){
            throw new UnableToReadException(SqlErrorMessage);
        }

    }
    // Validate Locations
    public boolean validateLocation(String location) {
        try {
            return locationDAOImpl.isValidLocation(location);
        } catch (SQLException ex){
            throw new UnableToReadException(SqlErrorMessage);
        }
    }
    // Calculate Distance
    public float calculateDistance(String pickupLocation, String dropoffLocation) {
        if (pickupLocation == null || dropoffLocation == null) {
            throw new IllegalArgumentException(locationInvalidErrorMessage);
        }
        try {
            return getDistanceBetweenLocations(pickupLocation, dropoffLocation);
        } catch (SQLException ex){
            throw new UnableToReadException(SqlErrorMessage);
        }

    }

    // Get Distance Between Locations
    public float getDistanceBetweenLocations(String pickupLocationName, String dropoffLocationName) throws SQLException {
        Location pickupLocation = locationDAOImpl.getLocationByName(pickupLocationName);
        Location dropoffLocation = locationDAOImpl.getLocationByName(dropoffLocationName);

        return haversine(pickupLocation.getLatitude().doubleValue(), pickupLocation.getLongitude().doubleValue(),
                dropoffLocation.getLatitude().doubleValue(), dropoffLocation.getLongitude().doubleValue());
    }
    // Haversine formula to calculate distance between two points in km
    private float haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (float) ((float) R * c); // Distance in km
    }

    // Calculate Fare
    public float calculateAmount(float distance) {
        return distance * 10; // Example fare calculation: 10 per km
    }

    // Get User Ride History
    public List<Ride> getUserRideHistory(int userId) {
        try {
            return rideDAOImpl.getUserRideHistory(userId);
        }catch (SQLException ex){
            throw new UnableToReadException(SqlErrorMessage);
        }
    }

    public Ride getCurrentRideForUser(int userId) {
        try {
            return rideDAOImpl.getCurrentRideForUser(userId);
        } catch (SQLException ex){
            throw new UnableToReadException(SqlErrorMessage);
        }
    }

    public List<Payment> getPaymentHistoryForUser(int userId) {
        try {
            return paymentDAOImpl.getPaymentHistoryForUser(userId);
        }catch (SQLException ex){
            throw new UnableToReadException(SqlErrorMessage);
        }
    }

    public Ride getCurrentRideForDriver(int driverId) {
        try {
            return rideDAOImpl.getCurrentRideForDriver(driverId);
        }catch (SQLException ex){
            throw new UnableToReadException(SqlErrorMessage);
        }
    }
    public boolean checkIsDriver(String email) {
        if(!Validator.isValidEmail(email)){
            throw new IllegalArgumentException(mailInvalidErrorMessage);
        }
        try {
            return driverDAOImpl.isRegistered(email);
        } catch (SQLException ex){
            throw new UnableToWriteException(SqlErrorMessage);
        }
    }

    public void changeRideStatus(int userId, String status) {
        try {
            Ride ride = rideDAOImpl.getCurrentRideForDriver(userId);
            if (status.equalsIgnoreCase("Completed")) completeRide(ride.getRideId(), ride.getDriverId());
            rideDAOImpl.updateRideStatus(ride.getRideId(), status);
            System.out.println("Ride completed successfully.");
        } catch (SQLException  ex){
            throw new UnableToWriteException(SqlErrorMessage);
        }
    }

    public Ride getRideCompletedForUser(int userId) {
        try {
            return rideDAOImpl.getCompletedRideForUser(userId);
        }catch (SQLException  ex){
            throw new UnableToReadException(SqlErrorMessage);
        }
    }

    public void setRating(RatingHandler ratingHandler) {
        try {
            rateCabDAOImpl.setRating(ratingHandler);
        } catch (SQLException  ex){
            throw new UnableToWriteException(SqlErrorMessage);
        }
    }

    public List<Payment> getPaymentHistoryForDriver(int driverId) {
        try {
            return paymentDAOImpl.getPaymentHistoryForDriver(driverId);
        } catch (SQLException ex){
            throw new UnableToReadException(SqlErrorMessage);
        }
    }

    public int getRating(int driverId) {
        try {
            return rateCabDAOImpl.getRating(driverId);
        }catch (SQLException  ex){
            throw new UnableToReadException(SqlErrorMessage);
        }
    }

    public void updateDriverCabDetails(int driverId, String newCabModel, String newLicenseNumber) {
        try{
            driverDAOImpl.changeCabDetails(driverId, newCabModel, newLicenseNumber);
        } catch (SQLException ex){
            throw new UnableToWriteException(SqlErrorMessage);
        }
    }

    public boolean isValidCity(String cityName) {
        try {
            return locationDAOImpl.isValidCity(cityName);
        } catch (SQLException ex){
            throw new UnableToReadException(SqlErrorMessage);
        }
    }

    public int calculateApproxTime(float distance, int speed) {
        return (int) distance/speed;
    }
}

