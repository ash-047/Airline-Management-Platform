package com.airline.controller;

import com.airline.model.*;
import com.airline.util.DatabaseUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.airline.model.BookingStatus;
import com.airline.model.FlightStatus;
import com.airline.model.PaymentStatus;
import com.airline.controller.BookingController;
import java.util.List;

// Uses Singleton pattern
public class FlightController {
    private static FlightController instance;
    private List<Flight> flights; // In a real app, would use a database
    
    private FlightController() {
        // Initialize with some sample flights for demo purposes
        flights = new ArrayList<>();
        loadSampleFlights();
    }
    
    public static FlightController getInstance() {
        if (instance == null) {
            instance = new FlightController();
        }
        return instance;
    }
    
    public List<Flight> searchFlights(String source, String destination, LocalDate date) {
        // Filter flights based on search criteria
        List<Flight> results = new ArrayList<>();
        
        for (Flight flight : flights) {
            if (flight.getSource().getCode().equalsIgnoreCase(source) &&
                flight.getDestination().getCode().equalsIgnoreCase(destination) &&
                flight.getDepartureTime().toLocalDate().equals(date) &&
                flight.getStatus() != FlightStatus.CANCELLED) {
                results.add(flight);
            }
        }
        
        return results;
    }
    
    public Flight getFlightDetails(String flightId) {
        return flights.stream()
                .filter(flight -> flight.getFlightId().equals(flightId))
                .findFirst()
                .orElse(null);
    }
    
        // Find the existing updateFlightStatus method and replace it with this version
    public boolean updateFlightStatus(String flightId, FlightStatus newStatus) {
        Flight flight = getFlightDetails(flightId);
        if (flight != null) {
            FlightStatus oldStatus = flight.getStatus();
            flight.setStatus(newStatus);
            
            // Notify customers about the status change
            if (oldStatus != newStatus) {
                notifyCustomersAboutStatusChange(flight, oldStatus, newStatus);
            }
            
            return true;
        }
        return false;
    }
    
    // Add this new private method after the updateFlightStatus method
    private void notifyCustomersAboutStatusChange(Flight flight, FlightStatus oldStatus, FlightStatus newStatus) {
        // Get all bookings for this flight
        BookingController bookingController = BookingController.getInstance();
        List<Booking> affectedBookings = bookingController.getBookingsForFlight(flight.getFlightId());
        
        // In a real app, this would send notifications/emails to customers
        for (Booking booking : affectedBookings) {
            System.out.println("Notifying customer " + booking.getCustomer().getUsername() + 
                              " about flight " + flight.getFlightNumber() + 
                              " status change from " + oldStatus + " to " + newStatus);
            
            // If flight is cancelled, process refunds
            if (newStatus == FlightStatus.CANCELLED && 
                booking.getStatus() != BookingStatus.CANCELLED && 
                booking.getPaymentStatus() == PaymentStatus.COMPLETED) {
                
                // Cancel booking and initiate refund
                bookingController.cancelBooking(booking.getBookingId());
                
                // Show alert to user if they're currently logged in (in a real app)
                System.out.println("Flight cancelled: Refund initiated for booking " + booking.getBookingId());
            }
        }
    }
    
    public Map<SeatCategory, Integer> getAvailableSeats(Flight flight) {
        // In a real app, would check database for available seats
        return flight.getAvailableSeats(null); // Using null as a workaround due to model changes
    }
    
    // For demo purposes, initialize with sample data
    private void loadSampleFlights() {
        // Create airports
        Airport jfk = new Airport("JFK", "John F. Kennedy International Airport", "New York", "USA");
        Airport lax = new Airport("LAX", "Los Angeles International Airport", "Los Angeles", "USA");
        Airport lhr = new Airport("LHR", "Heathrow Airport", "London", "UK");
        Airport cdg = new Airport("CDG", "Charles de Gaulle Airport", "Paris", "France");
        
        // Create flights
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDateTime dayAfter = LocalDateTime.now().plusDays(2);
        
        Flight flight1 = new Flight("F001", "AA101", jfk, lax, tomorrow.withHour(8), tomorrow.withHour(12));
        Flight flight2 = new Flight("F002", "BA202", lhr, jfk, tomorrow.withHour(10), tomorrow.withHour(14));
        Flight flight3 = new Flight("F003", "AF303", cdg, lhr, dayAfter.withHour(9), dayAfter.withHour(10));
        Flight flight4 = new Flight("F004", "AA404", lax, jfk, dayAfter.withHour(11), dayAfter.withHour(15));
        
        // Set available seats and prices
        Map<SeatCategory, Integer> seats1 = new HashMap<>();
        seats1.put(SeatCategory.ECONOMY, 100);
        seats1.put(SeatCategory.BUSINESS, 20);
        seats1.put(SeatCategory.FIRST_CLASS, 10);
        flight1.setAvailableSeats(seats1);
        
        Map<SeatCategory, Double> prices1 = new HashMap<>();
        prices1.put(SeatCategory.ECONOMY, 300.0);
        prices1.put(SeatCategory.BUSINESS, 800.0);
        prices1.put(SeatCategory.FIRST_CLASS, 1500.0);
        flight1.setPrice(prices1);
        
        // Set similar data for other flights
        flight2.setAvailableSeats(seats1);
        flight2.setPrice(prices1);
        flight3.setAvailableSeats(seats1);
        flight3.setPrice(prices1);
        flight4.setAvailableSeats(seats1);
        flight4.setPrice(prices1);
        
        // Add flights to list
        flights.add(flight1);
        flights.add(flight2);
        flights.add(flight3);
        flights.add(flight4);
    }
    
    // For admin to add a new flight
    public boolean addFlight(Flight flight) {
        if (flight != null && !flights.contains(flight)) {
            flights.add(flight);
            return true;
        }
        return false;
    }
    
    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights);
    }
}