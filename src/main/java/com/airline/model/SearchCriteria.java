package com.airline.model;

import java.time.LocalDate;

public class SearchCriteria {
    private String sourceCode;
    private String destinationCode;
    private LocalDate departureDate;
    private LocalDate returnDate; // For round trips
    private int passengerCount;
    private SeatCategory preferredClass;
    
    public SearchCriteria() {}
    
    public SearchCriteria(String sourceCode, String destinationCode, LocalDate departureDate, int passengerCount) {
        this.sourceCode = sourceCode;
        this.destinationCode = destinationCode;
        this.departureDate = departureDate;
        this.passengerCount = passengerCount;
        this.preferredClass = SeatCategory.ECONOMY; // Default
    }
    
    // Getters and Setters
    public String getSourceCode() {
        return sourceCode;
    }
    
    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
    
    public String getDestinationCode() {
        return destinationCode;
    }
    
    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }
    
    public LocalDate getDepartureDate() {
        return departureDate;
    }
    
    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    public int getPassengerCount() {
        return passengerCount;
    }
    
    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }
    
    public SeatCategory getPreferredClass() {
        return preferredClass;
    }
    
    public void setPreferredClass(SeatCategory preferredClass) {
        this.preferredClass = preferredClass;
    }
}