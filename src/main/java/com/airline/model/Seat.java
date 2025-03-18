package com.airline.model;

public class Seat {
    private String seatNumber;
    private SeatCategory category;
    private double price;
    private boolean isBooked;
    
    public Seat() {}
    
    public Seat(String seatNumber, SeatCategory category, double price) {
        this.seatNumber = seatNumber;
        this.category = category;
        this.price = price;
        this.isBooked = false;
    }
    
    public boolean book() {
        if (!isBooked) {
            isBooked = true;
            return true;
        }
        return false;
    }
    
    public boolean unbook() {
        if (isBooked) {
            isBooked = false;
            return true;
        }
        return false;
    }
    
    // Getters and Setters
    public String getSeatNumber() {
        return seatNumber;
    }
    
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    
    public SeatCategory getCategory() {
        return category;
    }
    
    public void setCategory(SeatCategory category) {
        this.category = category;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public boolean isBooked() {
        return isBooked;
    }
    
    public void setBooked(boolean booked) {
        isBooked = booked;
    }
    
    @Override
    public String toString() {
        return seatNumber + " (" + category + ")";
    }
}