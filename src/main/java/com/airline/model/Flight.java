package com.airline.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Flight {
    private String flightId;
    private String flightNumber;
    private Airport source;
    private Airport destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private FlightStatus status;
    private Map<SeatCategory, Integer> availableSeats;
    private Map<SeatCategory, Double> price;
    
    public Flight() {
        this.availableSeats = new HashMap<>();
        this.price = new HashMap<>();
        this.status = FlightStatus.SCHEDULED;
    }
    
    public Flight(String flightId, String flightNumber, Airport source, Airport destination, 
                  LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this();
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
    
    public int getAvailableSeats() {
        return availableSeats.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public void updateStatus(FlightStatus status) {
        this.status = status;
        // In a real app, would notify observers about the status change
    }
    
    // Getters and Setters
    public String getFlightId() {
        return flightId;
    }
    
    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }
    
    public String getFlightNumber() {
        return flightNumber;
    }
    
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    
    public Airport getSource() {
        return source;
    }
    
    public void setSource(Airport source) {
        this.source = source;
    }
    
    public Airport getDestination() {
        return destination;
    }
    
    public void setDestination(Airport destination) {
        this.destination = destination;
    }
    
    public LocalDateTime getDepartureTime() {
        return departureTime;
    }
    
    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }
    
    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
    
    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
    public FlightStatus getStatus() {
        return status;
    }
    
    public void setStatus(FlightStatus status) {
        this.status = status;
    }
    
    public Map<SeatCategory, Integer> getAvailableSeats(SeatCategory category) {
        return availableSeats;
    }
    
    public void setAvailableSeats(Map<SeatCategory, Integer> availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    public Map<SeatCategory, Double> getPrice() {
        return price;
    }
    
    public void setPrice(Map<SeatCategory, Double> price) {
        this.price = price;
    }
    
    @Override
    public String toString() {
        return flightNumber + ": " + source.getCode() + " to " + destination.getCode();
    }
}