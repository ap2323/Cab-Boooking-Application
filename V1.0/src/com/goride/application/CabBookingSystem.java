package com.goride.application;

import com.goride.models.Driver;
import com.goride.exceptions.UnableToReadException;
import com.goride.exceptions.UnableToWriteException;
import com.goride.exceptions.UserAlreadyFoundException;
import com.goride.models.Payment;
import com.goride.models.RatingHandler;
import com.goride.models.Ride;
import com.goride.service.CabBookingService;
import com.goride.models.User;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CabBookingSystem {
    private static final CabBookingService cabBookingService = new CabBookingService();
    private static User loggedInUser = null;
    private static Driver loggedInDriver = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (loggedInUser == null && loggedInDriver == null) {
                showLoginMenu(scanner);
            } else if (loggedInUser != null) {
                showUserMenu(scanner);
            } else if (loggedInDriver != null) {
                showDriverMenu(scanner);
            }
        }
    }

    private static void showLoginMenu(Scanner scanner) {
        System.out.println("Welcome to the Cab Booking System");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.println("Enter Choice:");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                try {
                    handleRegistration(scanner);
                } catch (UnableToWriteException | IllegalArgumentException | UserAlreadyFoundException ex){
                    System.out.println(ex.getMessage());
                }
                break;
            case 2:
                try {
                    handleLogin(scanner);
                } catch (UnableToWriteException | IllegalArgumentException ex){
                    System.out.println(ex.getMessage());
                }
                break;
            case 3:
                System.out.println("Thank you for using the Cab Booking System. Goodbye!");
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void handleRegistration(Scanner scanner) {
        System.out.println("User or Driver(1/2):");
        System.out.println("Enter Choice:");
        int type = scanner.nextInt();
        scanner.nextLine();
        switch (type){
            case 1:
                handleUserRegistration(scanner);
                break;
            case 2:
                handleDriverRegistration(scanner);
                break;
            default:
                System.out.println("Invalid Type.");
        }
    }

    private static void handleUserRegistration(Scanner scanner) {
        System.out.println("Enter user name:");
        String username = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine().trim().toLowerCase();
        System.out.println("Enter phone number:");
        long phoneNumber;
        try {
            phoneNumber = scanner.nextLong();
        } catch (InputMismatchException ex){
            System.out.println("Phone number contains only numbers.");
            return;
        }
        scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        cabBookingService.registerUser(username, email, phoneNumber, password);
        System.out.println("Registered successfully.");
    }

    private static void handleDriverRegistration(Scanner scanner) {
        System.out.println("Enter driver name:");
        String driverName = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine().trim().toLowerCase();
        System.out.println("Enter phone number:");
        long phoneNumber;
        try {
            phoneNumber = scanner.nextLong();
        } catch (InputMismatchException ex){
            System.out.println("Phone number contains only numbers.");
            return;
        }
        scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine().trim();
        System.out.println("Enter car model:");
        String carModel = scanner.nextLine();
        System.out.println("Enter license number:");
        String licenseNumber = scanner.nextLine();

        cabBookingService.registerDriver(driverName, email,password,phoneNumber ,carModel, licenseNumber);
        System.out.println("Registered successfully.");
    }

    private static void handleLogin(Scanner scanner) {
        System.out.println("Enter email:");
        String email = scanner.nextLine().trim();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        // Attempt to login as a driver
        if(cabBookingService.checkIsDriver(email)) {
            loggedInDriver = (Driver) cabBookingService.loginDriver(email, password);
            if (loggedInDriver != null) {
                System.out.println("Driver login successful. Welcome " + loggedInDriver.getUsername() + "!");
                return;
            }
        } else {
            // If not logged in as a driver, attempt to login as a user
            loggedInUser = cabBookingService.loginUser(email, password);
            if (loggedInUser != null) {
                System.out.println("User login successful. Welcome " + loggedInUser.getUsername() + "!");
                return;
            }
        }
        // If neither driver nor user login is successful
        System.out.println("Login failed. Please check your email and password.");

    }


    private static void showUserMenu(Scanner scanner) {
        System.out.println("1. Book a Cab");
        System.out.println("2. Show Booking");
        System.out.println("3. Display History");
        System.out.println("4. Display Payment History");
        System.out.println("5. Notification");
        System.out.println("6. Logout");
        System.out.println("7. Exit");

        System.out.println("Enter Choice:");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                try {
                    handleBooking(scanner);
                } catch (UnableToWriteException | UnableToReadException ex){
                    System.out.println(ex.getMessage());
                }
                break;
            case 2:
                try {
                    handleShowBooking();
                }catch (UnableToReadException ex){
                    System.out.println(ex.getMessage());
                }
                break;
            case 3:
                try {
                    displayRideHistory();
                }catch (UnableToReadException ex){
                    System.out.println(ex.getMessage());
                }
                break;
            case 4:
                try {
                    displayPaymentHistoryForUser();
                } catch (UnableToReadException ex){
                    System.out.println(ex.getMessage());
                }
                break;
            case 5:
                try {
                    handleUserNotifications(scanner);
                } catch (UnableToReadException ex){
                    System.out.println(ex.getMessage());
                }
                break;
            case 6:
                loggedInUser = null;
                System.out.println("Logged out successfully.");
                break;
            case 7:
                System.out.println("Thank you for using Go Ride!...");
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void handleUserNotifications(Scanner scanner) {
        // show notifications for the logged-in user
        Ride userRide = cabBookingService.getRideCompletedForUser(loggedInUser.getUserId());
        if (userRide != null) {
            System.out.println("Notifications:");
            System.out.println("Driver ID:" + userRide.getDriverId() +", User ID: " + userRide.getUserId() + ", Pickup Location: " + userRide.getPickupLocation() + ", Drop off Location: " + userRide.getDropoffLocation());
            rating(scanner, userRide);
        } else {
            System.out.println("No notifications found.");
        }
    }

    private static void rating(Scanner scanner, Ride userRide) {
        System.out.println("How was the ride[1-5]:");
        int stars = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Provide feedback:");
        String feedback = scanner.nextLine().trim();
        if(stars >= 1 && stars <= 5 && !feedback.isEmpty()){
            RatingHandler ratingHandler = new RatingHandler(loggedInUser.getUserId(), userRide.getRideId(),userRide.getDriverId(), stars,feedback);
            cabBookingService.setRating(ratingHandler);
        }
        System.out.println("Thank you!.");
    }

    private static void showDriverMenu(Scanner scanner) {
        System.out.println("1. About me");
        System.out.println("2. Change to New Cab");
        System.out.println("3. Change current location");
        System.out.println("4. Notifications");
        System.out.println("5. Change Ride Status");
        System.out.println("6. Payment History");
        System.out.println("7. Logout");
        System.out.println("8. Exit");

        System.out.println("Enter Choice:");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                displayDriver();
                break;
            case 2:
                try {
                    changeCab(scanner);
                } catch (UnableToWriteException ex){
                    System.out.println(ex.getMessage());
                }
                break;
            case 3:
                try {
                    changeCurrentLocation(scanner);
                } catch (IllegalArgumentException | UnableToWriteException ex){
                    System.out.println(ex.getMessage());
                }
                break;
            case 4:
                try {
                    handleDriverNotifications();
                } catch (UnableToReadException ex){
                    System.out.println(ex.getMessage());
                }
                break;
            case 5:
                try {
                    changeRideStatus(scanner);
                } catch (UnableToWriteException ex){
                    System.out.println(ex.getMessage());
                }
                break;
            case 6:
                try {
                    displayPaymentHistoryForDriver();
                } catch (UnableToReadException ex){
                    System.out.println(ex.getMessage());
                }
                break;
            case 7:
                loggedInDriver = null;
                System.out.println("Logged out successfully.");
                break;
            case 8:
                System.out.println("Thank you for using GO RIDE!...");
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void changeCab(Scanner scanner) {
        System.out.println("Enter new cab model:");
        String newCabModel = scanner.nextLine();

        System.out.println("Enter new license number:");
        String newLicenseNumber = scanner.nextLine();

        // Validate the inputs
        if (newCabModel.isEmpty() || newLicenseNumber.isEmpty()) {
            System.out.println("Cab model and license number cannot be empty.");
            return;
        }

        // Update the driver's cab details in the database
        cabBookingService.updateDriverCabDetails(loggedInDriver.getDriverId(), newCabModel, newLicenseNumber);
        loggedInDriver.setCarModel(newCabModel);
        loggedInDriver.setLicenseNumber(newLicenseNumber);
        System.out.println("Cab details updated successfully.");
    }

    private static void displayDriver() {
        System.out.println("Details");
        System.out.println("Name:" + loggedInDriver.getUsername());
        System.out.println("Car Name:" + loggedInDriver.getCarModel());
        System.out.println("Rating:" + cabBookingService.getRating(loggedInDriver.getDriverId()));
    }

    private static void displayPaymentHistoryForDriver() {
        // display payment history for the logged-in driver
        List<Payment> paymentHistory = cabBookingService.getPaymentHistoryForDriver(loggedInDriver.getDriverId());
        if (!paymentHistory.isEmpty()) {
            System.out.println("Payment History:");
            for (Payment payment : paymentHistory) {
                System.out.println("Payment ID: " + payment.getPaymentId() + ", Ride ID: " + payment.getRideId() + ", Amount: " + payment.getAmount() + ", Status: " + payment.getPaymentStatus() + ", Date and Time: " + payment.getCreatedAt());
            }
        } else {
            System.out.println("No payment history found.");
        }
    }

    private static void changeRideStatus(Scanner scanner) {
            System.out.println("Enter Status:");
            String status = scanner.nextLine().trim().toUpperCase();
            if(status.equalsIgnoreCase("ongoing") || status.equalsIgnoreCase("completed")){
                cabBookingService.changeRideStatus(loggedInDriver.getDriverId(), status);

            } else {
                System.out.println("Invalid status. Status only be ongoing or completed.");
                changeRideStatus(scanner);
            }


    }

    private static void changeCurrentLocation(Scanner scanner) {
        System.out.println("Enter a Location:");
        String location = scanner.nextLine().trim().toLowerCase();

        cabBookingService.changeCurrentLocation(loggedInDriver.getDriverId(),location);
    }

    private static void handleBooking(Scanner scanner) {
        System.out.println("Enter City:");
        String cityName = scanner.nextLine().trim().toLowerCase();
        System.out.println("Enter Pickup Location:");
        String pickupLocation = scanner.nextLine().trim().toLowerCase();
        System.out.println("Enter Drop off Location:");
        String dropoffLocation = scanner.nextLine().trim().toLowerCase();

        if(!cabBookingService.isValidCity(cityName)){
            System.out.println("Invalid City!.");
            return;
        }

        if (!cabBookingService.validateLocation(pickupLocation) || !cabBookingService.validateLocation(dropoffLocation)) {
            System.out.println("Invalid locations entered. Please try again.");
            return;
        }

        List<Driver> availableCabs = cabBookingService.getAvailableCabsAtLocation(pickupLocation);
        if (availableCabs.isEmpty()) {
            System.out.println("No available cabs at the pickup location.");
            return;
        }

        System.out.println("Available Cabs:");
        for (Driver driver : availableCabs) {
            System.out.println("Driver ID: " + driver.getDriverId() + ", Name: " + driver.getUsername() + ", Car Model: " + driver.getCarModel() + ", Rating:" + cabBookingService.getRating(driver.getDriverId()));
        }

        System.out.println("Enter Driver ID to book:");
        int driverId = scanner.nextInt();

        float distance = cabBookingService.calculateDistance(pickupLocation, dropoffLocation);
        //int time = cabBookingService.calculateApproxTime(distance, 35);
        float amount = cabBookingService.calculateAmount(distance);

        System.out.println("Distance: " + distance + " km");
        System.out.println("Amount: " + amount);

        System.out.println("Confirm booking?[y/n]:");
        char confirm_booking = scanner.next().trim().charAt(0);

        if(confirm_booking == 'y' || confirm_booking == 'Y'){
           int rideID = cabBookingService.bookRide(loggedInUser, pickupLocation, dropoffLocation, driverId);
           System.out.println(rideID);
            cabBookingService.processPayment(rideID,driverId,loggedInUser.getUserId(),amount);
            System.out.println("Payment processed successfully.");
            System.out.println("Ride booked successfully with driver ID: " + driverId);
        } else {
            showUserMenu(scanner);
        }

    }

    private static void handleShowBooking() {

        Ride currentRide = cabBookingService.getCurrentRideForUser(loggedInUser.getUserId());
        if (currentRide != null) {
            System.out.println("Current Ride:");
            System.out.println("Ride ID: " + currentRide.getRideId());
            System.out.println("Driver ID: " + currentRide.getDriverId());
            System.out.println("Pickup Location: " + currentRide.getPickupLocation());
            System.out.println("Drop off Location: " + currentRide.getDropoffLocation());
            System.out.println("Status: " + currentRide.getRideStatus());
        } else {
            System.out.println("No current booking found.");
        }
    }

    private static void displayRideHistory() {
        // display ride history for the logged-in user
        List<Ride> rideHistory = cabBookingService.getUserRideHistory(loggedInUser.getUserId());
        if (!rideHistory.isEmpty()) {
            System.out.println("Ride History:");
            for (Ride ride : rideHistory) {
                System.out.println("Ride ID: " + ride.getRideId() + ", Pickup Location: " + ride.getPickupLocation() + ", Dropoff Location: " + ride.getDropoffLocation() + ", Status: " + ride.getRideStatus());
            }
        } else {
            System.out.println("No ride history found.");
        }
    }

    private static void displayPaymentHistoryForUser() {
        // display payment history for the logged-in user
        List<Payment> paymentHistory = cabBookingService.getPaymentHistoryForUser(loggedInUser.getUserId());
        if (!paymentHistory.isEmpty()) {
            System.out.println("Payment History:");
            for (Payment payment : paymentHistory) {
                System.out.println("Payment ID: " + payment.getPaymentId() + ", Ride ID: " + payment.getRideId() + ", Amount: " + payment.getAmount() + ", Status: " + payment.getPaymentStatus() + ", Date and Time: " + payment.getCreatedAt());
            }
        } else {
            System.out.println("No payment history found.");
        }
    }

    private static void handleDriverNotifications() {
        // show notifications for the logged-in driver
        Ride driverRide = cabBookingService.getCurrentRideForDriver(loggedInDriver.getDriverId());
        if (driverRide != null) {
            System.out.println("Notifications:");
            System.out.println("User ID: " + driverRide.getUserId() + ", Pickup Location: " + driverRide.getPickupLocation() + ", Drop off Location: " + driverRide.getDropoffLocation());

        } else {
            System.out.println("No notifications found.");
        }
    }
}

