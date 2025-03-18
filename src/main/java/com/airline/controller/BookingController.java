package com.airline.controller;

import com.airline.model.*;
import com.airline.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// Uses Command pattern for booking operations
public class BookingController {
    private static BookingController instance;
    private List<Booking> bookings; // In a real app, would use a database
    
    private BookingController() {
        bookings = new ArrayList<>();
    }
    
    public static BookingController getInstance() {
        if (instance == null) {
            instance = new BookingController();
        }
        return instance;
    }
    
    public Booking createBooking(Customer customer, Flight flight) {
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setFlight(flight);
        booking.setStatus(BookingStatus.PENDING);
        
        // Add to our list of bookings
        bookings.add(booking);
        
        return booking;
    }
    
    public boolean selectSeats(String bookingId, List<String> seatNumbers) {
        Booking booking = getBookingDetails(bookingId);
        if (booking != null && booking.getStatus() == BookingStatus.PENDING) {
            // In a real app, would check if these seats are available
            List<Seat> seats = new ArrayList<>();
            
            SeatCategory category = SeatCategory.ECONOMY; // Default for simplicity
            double seatPrice = booking.getFlight().getPrice().get(category);
            
            for (String seatNumber : seatNumbers) {
                Seat seat = new Seat(seatNumber, category, seatPrice);
                seats.add(seat);
            }
            
            booking.setSeats(seats);
            booking.setTotalAmount(booking.calculateFare());
            return true;
        }
        return false;
    }
    
    public boolean confirmBooking(String bookingId) {
        Booking booking = getBookingDetails(bookingId);
        if (booking != null) {
            return booking.confirmBooking();
        }
        return false;
    }
    
    public boolean cancelBooking(String bookingId) {
        Booking booking = getBookingDetails(bookingId);
        if (booking != null) {
            return booking.cancelBooking();
        }
        return false;
    }
    
    public Booking getBookingDetails(String bookingId) {
        return bookings.stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }
    
    public List<Booking> getBookingHistory(Customer customer) {
        return bookings.stream()
                .filter(b -> b.getCustomer().getUserId().equals(customer.getUserId()))
                .collect(Collectors.toList());
    }
    
    public Ticket generateTicket(Booking booking) {
        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            return booking.generateTicket();
        }
        return null;
    }
}