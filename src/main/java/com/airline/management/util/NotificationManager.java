package com.airline.management.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// Observer Pattern: Subject that notifies registered observers about events
@Component
public class NotificationManager {
    
    private final List<NotificationObserver> observers = new ArrayList<>();
    
    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyBookingConfirmation(Long bookingId, String email) {
        for (NotificationObserver observer : observers) {
            observer.onBookingConfirmation(bookingId, email);
        }
    }
    
    public void notifyBookingCancellation(Long bookingId, String email) {
        for (NotificationObserver observer : observers) {
            observer.onBookingCancellation(bookingId, email);
        }
    }
    
    public void notifyFlightStatusChange(Long flightId, String status) {
        for (NotificationObserver observer : observers) {
            observer.onFlightStatusChange(flightId, status);
        }
    }
    
    public void notifyEnquiryResponse(Long enquiryId, String email) {
        for (NotificationObserver observer : observers) {
            observer.onEnquiryResponse(enquiryId, email);
        }
    }
}
