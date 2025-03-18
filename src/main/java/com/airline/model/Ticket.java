package com.airline.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
    private String ticketId;
    private Booking booking;
    private String passengerName;
    private String seatNumber;
    private LocalDateTime boardingTime;
    private String gate;
    private TicketStatus status;
    
    public Ticket() {
        this.ticketId = UUID.randomUUID().toString();
        this.status = TicketStatus.VALID;
    }
    
    public void displayTicket() {
        // Logic to display ticket information
        System.out.println("Ticket: " + ticketId);
        System.out.println("Passenger: " + passengerName);
        System.out.println("Flight: " + booking.getFlight().getFlightNumber());
        System.out.println("From: " + booking.getFlight().getSource().getCode());
        System.out.println("To: " + booking.getFlight().getDestination().getCode());
        System.out.println("Seat: " + seatNumber);
        System.out.println("Status: " + status);
    }
    
    public boolean cancelTicket() {
        if (status != TicketStatus.CANCELLED) {
            status = TicketStatus.CANCELLED;
            if (booking != null) {
                booking.cancelBooking();
            }
            return true;
        }
        return false;
    }
    
    // Getters and Setters
    public String getTicketId() {
        return ticketId;
    }
    
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
    
    public Booking getBooking() {
        return booking;
    }
    
    public void setBooking(Booking booking) {
        this.booking = booking;
    }
    
    public String getPassengerName() {
        return passengerName;
    }
    
    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }
    
    public String getSeatNumber() {
        return seatNumber;
    }
    
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    
    public LocalDateTime getBoardingTime() {
        return boardingTime;
    }
    
    public void setBoardingTime(LocalDateTime boardingTime) {
        this.boardingTime = boardingTime;
    }
    
    public String getGate() {
        return gate;
    }
    
    public void setGate(String gate) {
        this.gate = gate;
    }
    
    public TicketStatus getStatus() {
        return status;
    }
    
    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}