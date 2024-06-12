# CAB Booking Application

## Table of Contents

* [About V 1.0](#about-v-10)
* [Features](#features)
* [Technologies Used](#technologies-used)
* [Setup](#setup)
* [Database Configuration](#database-configuration)
* [Usage](#usage)
* [Contributing](#contributing)

## About Version 1.0
Welcome to the CAB Booking Application! This application allows users to book cabs, track rides, and rate drivers. This README provides an overview of the project, its setup, and its functionalities.

## Features
* **User Registration and Login:** Users can register and log in to the application.
* **Driver Registration and Login:** Drivers can register and log in to the application.
* **Cab Booking:** Users can book cabs by providing pickup and drop-off locations.
* **Ride Tracking:** Users can track their ride status.
* **Driver Rating:** Users can rate drivers after completing their rides.
* **Distance and Time Calculation:** The application calculates the approximate distance and time for a ride.
* **SMS Notification:** Send SMS notifications to drivers when a ride is booked(not implemented).
* **Database Management:** The application uses MySQL for database management.

## Technologies Used
* **Java:** Core programming language used for application development.
* **JDBC:** Java Database Connectivity for database interaction.
* **MySQL:** Database management system.

## Setup
## Prerequisites
* Java Development Kit (JDK) 8 or higher
* MySQL

# Installation
## Clone the repository:
```bash
git clone https://github.com/yourusername/cab-booking-application.git
cd cab-booking-application
```
## Database Configuration
Update the dbconfig.properties file with your MySQL credentials.
```properties
database.name=cab_booking
database.user=root
database.password=yourpassword
```
# Database Configuration
The DatabaseConfiguration class sets up the database and creates the necessary tables. Ensure you have provided correct MySQL credentials in the dbconfig.properties file.
# Tables
* Users
* Drivers
* Rides
* Locations
* Payments
* Ratings

## Usage
* User Registration.
* Run the application.
* Follow the prompts to register a new user or driver.
  
  ![Screenshot from 2024-06-12 10-30-08](https://github.com/ap2323/Cab-Boooking-Application/assets/91046006/544a9377-a89c-4e95-89f6-191931feb923)

* Log in using your credentials.
  
  ![Screenshot from 2024-06-12 10-31-17](https://github.com/ap2323/Cab-Boooking-Application/assets/91046006/362edf82-a973-41e3-8358-6a2ec4387da5)

* Log in as a user.
  
  ![Screenshot from 2024-06-12 10-32-05](https://github.com/ap2323/Cab-Boooking-Application/assets/91046006/0b444059-e9cd-475b-9046-dbce70f641f2)

* Book a cab.
  Provide the pickup and drop-off locations.
  The application calculates the distance and approximate travel time.
  Confirm the booking.
  Receive SMS notification for booking confirmation(not implemented).

  ![Screenshot from 2024-06-12 10-33-12](https://github.com/ap2323/Cab-Boooking-Application/assets/91046006/b8a98f2f-11ab-4b5c-a096-61bfb6653a41)

* After completing a ride, log in as a user.
* Rating a Driver. Provide a rating and comment for the driver.
  
  ![Screenshot from 2024-06-12 10-35-29](https://github.com/ap2323/Cab-Boooking-Application/assets/91046006/a5993884-c5c5-409a-be16-f486ae0c0541)

# Contributing
We welcome contributions from the community! If you find a bug or want to add a feature, please open an issue or submit a pull request.

## Steps to Contribute
* Fork the repository.
* Create a new branch.
* Make your changes.
* Submit a pull request.

      
# TRUST YOUR CRAZY IDEAS!.   
      
      
      
