# SkyServe - Airline Management Platform

## Overview

SkyServe is a comprehensive web-based airline management platform designed to streamline flight operations for airlines and provide customers with an intuitive interface for flight-related 
activities. This project implements a full-stack solution that handles the complete booking 
lifecycle - from flight search to ticket generation. 

The system is built using the Model-View-Controller (MVC) architecture and incorporates various design patterns to ensure maintainability, extensibility, and robustness. It features distinct interfaces for customers and administrators, with customers able to search flights, make bookings, select seats, and manage their reservations, while administrators can manage flights, monitor bookings, and handle customer inquiries. 

Our implementation emphasizes a seamless user journey through the booking process, secure 
payment handling with multiple payment options, and a responsive design that works across 
devices. The modular architecture allows for independent development and testing of 
components, making the system easier to maintain and extend with new features in the future

## Features

- **User Registration & Login:** Secure authentication for customers and admins.
- **Flight Search & Booking:** Search available flights, view details, and book tickets.
- **Multi-step Booking:** Step-by-step booking process including passenger info and payment.
- **Booking Management:** View, cancel, and check status of bookings.
- **Payment Processing:** Supports credit/debit card and net banking (demo).
- **Customer Enquiries:** Submit and track support enquiries.
- **Admin Dashboard:** Manage flights, bookings, and respond to enquiries.
- **Notifications:** Observer pattern for booking and enquiry notifications.

## Setup

1. **Clone the repository:**
   ```sh
   git clone https://github.com/ash-047/Airline-Management-Platform
   cd Airline-Management-Platform
   ```

2. **Configure the database:**
   - Edit [`src/main/resources/application.properties`](src/main/resources/application.properties) for your MySQL credentials.

3. **Build the project:**
   ```sh
   mvn clean install
   ```

4. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```

5. **Access the app:**
   - Open [http://localhost:8080](http://localhost:8080) in your browser.

## Default Users

- **Admin:**  
  Username: `admin`  
  Password: `admin123`

- **Customer:**  
  Username: `user`  
  Password: `user123`

## Project Structure

- `src/main/java/com/airline/management/` - Java source code
- `src/main/resources/templates/` - Thymeleaf HTML templates
- `src/main/resources/application.properties` - Configuration
