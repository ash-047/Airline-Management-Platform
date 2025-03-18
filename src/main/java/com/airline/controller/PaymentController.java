package com.airline.controller;

import com.airline.model.*;
import com.airline.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;

// Uses Strategy pattern for different payment methods
public class PaymentController {
    private static PaymentController instance;
    private List<Payment> payments; // In a real app, would use a database
    
    private PaymentController() {
        payments = new ArrayList<>();
    }
    
    public static PaymentController getInstance() {
        if (instance == null) {
            instance = new PaymentController();
        }
        return instance;
    }
    
    public boolean processPayment(String bookingId, PaymentMethod paymentMethod) {
        // Find the booking
        BookingController bookingController = BookingController.getInstance();
        Booking booking = bookingController.getBookingDetails(bookingId);
        
        if (booking != null && booking.getPaymentStatus() == PaymentStatus.PENDING) {
            // Create a new payment
            Payment payment = new Payment(booking, booking.getTotalAmount(), paymentMethod);
            
            // Process the payment
            boolean success = payment.processPayment();
            
            if (success) {
                // Update booking status
                booking.setPaymentStatus(PaymentStatus.COMPLETED);
                booking.confirmBooking();
                
                // Store the payment
                payments.add(payment);
                
                return true;
            }
        }
        return false;
    }
    
    public boolean verifyPayment(String paymentId) {
        Payment payment = payments.stream()
                .filter(p -> p.getPaymentId().equals(paymentId))
                .findFirst()
                .orElse(null);
        
        return payment != null && payment.getStatus() == PaymentStatus.COMPLETED;
    }
    
    public boolean initiateRefund(String bookingId) {
        // Find the booking
        BookingController bookingController = BookingController.getInstance();
        Booking booking = bookingController.getBookingDetails(bookingId);
        
        if (booking != null) {
            // Find the associated payment
            Payment payment = payments.stream()
                    .filter(p -> p.getBooking().getBookingId().equals(bookingId))
                    .findFirst()
                    .orElse(null);
            
            if (payment != null && payment.getStatus() == PaymentStatus.COMPLETED) {
                return payment.refundPayment();
            }
        }
        return false;
    }
}