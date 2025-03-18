package com.airline.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private List<Booking> bookingHistory;
    
    public Customer(String userId, String username, String password, String email, String phoneNumber) {
        super(userId, username, password, email, phoneNumber);
        this.bookingHistory = new ArrayList<>();
    }
    
    public Customer() {
        super();
        this.bookingHistory = new ArrayList<>();
    }
    
    public List<Flight> searchFlights(SearchCriteria criteria) {
        // In a real application, this would query a database
        // For now, we'll return an empty list
        return new ArrayList<>();
    }
    
    public Booking bookTicket(Flight flight) {
        // Create a new booking for this customer and flight
        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setCustomer(this);
        booking.setStatus(BookingStatus.PENDING);
        
        // In a real application, this would persist the booking
        this.bookingHistory.add(booking);
        
        return booking;
    }
    
    public boolean cancelTicket(Booking booking) {
        if (booking.getStatus() != BookingStatus.CANCELLED) {
            booking.setStatus(BookingStatus.CANCELLED);
            return true;
        }
        return false;
    }
    
    public Booking viewTicketDetails(String bookingId) {
        // Find and return booking by ID
        return bookingHistory.stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }
    
    // Getters and Setters
    public List<Booking> getBookingHistory() {
        return bookingHistory;
    }
    
    public void setBookingHistory(List<Booking> bookingHistory) {
        this.bookingHistory = bookingHistory;
    }
}