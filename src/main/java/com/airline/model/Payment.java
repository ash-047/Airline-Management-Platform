package com.airline.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {
    private String paymentId;
    private Booking booking;
    private double amount;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentDate;
    private PaymentStatus status;
    
    public Payment() {
        this.paymentId = UUID.randomUUID().toString();
        this.paymentDate = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }
    
    public Payment(Booking booking, double amount, PaymentMethod paymentMethod) {
        this();
        this.booking = booking;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }
    
    public boolean processPayment() {
        // Payment processing logic would go here
        // This is where you'd integrate with a payment gateway
        
        // For demo purposes, we'll just approve the payment
        this.status = PaymentStatus.COMPLETED;
        
        // Update the booking's payment status
        if (booking != null) {
            booking.setPaymentStatus(PaymentStatus.COMPLETED);
            booking.confirmBooking();
        }
        
        return true;
    }
    
    public boolean refundPayment() {
        if (status == PaymentStatus.COMPLETED) {
            status = PaymentStatus.REFUNDED;
            
            // Update the booking's payment status
            if (booking != null) {
                booking.setPaymentStatus(PaymentStatus.REFUNDED);
            }
            
            return true;
        }
        return false;
    }
    
    public Receipt generateReceipt() {
        // In a real app, would generate a receipt
        return new Receipt();
    }
    
    // Getters and Setters
    public String getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    
    public Booking getBooking() {
        return booking;
    }
    
    public void setBooking(Booking booking) {
        this.booking = booking;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public PaymentStatus getStatus() {
        return status;
    }
    
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}