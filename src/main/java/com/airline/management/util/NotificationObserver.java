package com.airline.management.util;

// Observer Pattern: Observer interface that gets notified of events
public interface NotificationObserver {
    void onBookingConfirmation(Long bookingId, String email);
    void onBookingCancellation(Long bookingId, String email);
    void onFlightStatusChange(Long flightId, String status);
    void onEnquiryResponse(Long enquiryId, String email);
}
