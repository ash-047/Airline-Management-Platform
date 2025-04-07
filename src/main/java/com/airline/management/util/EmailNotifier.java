package com.airline.management.util;

import org.springframework.stereotype.Component;

// Observer Pattern: Concrete observer implementation for email notifications
@Component
public class EmailNotifier implements NotificationObserver {
    
    @Override
    public void onBookingConfirmation(Long bookingId, String email) {
        // In a real application, this would send an email
        System.out.println("Sending booking confirmation email to " + email + " for booking #" + bookingId);
    }
    
    @Override
    public void onBookingCancellation(Long bookingId, String email) {
        // In a real application, this would send an email
        System.out.println("Sending booking cancellation email to " + email + " for booking #" + bookingId);
    }
    
    @Override
    public void onFlightStatusChange(Long flightId, String status) {
        // In a real application, this would send emails to all affected passengers
        System.out.println("Sending flight status update email for flight #" + flightId + ": " + status);
    }
    
    @Override
    public void onEnquiryResponse(Long enquiryId, String email) {
        // In a real application, this would send an email
        System.out.println("Sending enquiry response notification to " + email + " for enquiry #" + enquiryId);
    }
}
