package com.airline.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Booking {
    private String bookingId;
    private Flight flight;
    private Customer customer;
    private List<Seat> seats;
    private LocalDateTime bookingDate;
    private BookingStatus status;
    private double totalAmount;
    private PaymentStatus paymentStatus;
    
    public Booking() {
        this.bookingId = UUID.randomUUID().toString();
        this.seats = new ArrayList<>();
        this.bookingDate = LocalDateTime.now();
        this.status = BookingStatus.PENDING;
        this.paymentStatus = PaymentStatus.PENDING;
    }
    
    public double calculateFare() {
        return seats.stream().mapToDouble(Seat::getPrice).sum();
    }
    
    public boolean confirmBooking() {
        if (status == BookingStatus.PENDING) {
            status = BookingStatus.CONFIRMED;
            return true;
        }
        return false;
    }
    
    public boolean cancelBooking() {
        if (status != BookingStatus.CANCELLED) {
            status = BookingStatus.CANCELLED;
            // Logic to free up seats would go here
            return true;
        }
        return false;
    }
    
    public Ticket generateTicket() {
        // Create a ticket for this booking
        Ticket ticket = new Ticket();
        ticket.setBooking(this);
        ticket.setStatus(TicketStatus.VALID);
        return ticket;
    }
    
    // Getters and Setters
    public String getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
    
    public Flight getFlight() {
        return flight;
    }
    
    public void setFlight(Flight flight) {
        this.flight = flight;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public List<Seat> getSeats() {
        return seats;
    }
    
    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
    
    public LocalDateTime getBookingDate() {
        return bookingDate;
    }
    
    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    public BookingStatus getStatus() {
        return status;
    }
    
    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}