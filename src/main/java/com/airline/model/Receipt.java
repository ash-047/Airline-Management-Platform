package com.airline.model;

// Simple class for now - would be expanded in a real application
public class Receipt {
    private String receiptId;
    private Payment payment;
    
    public Receipt() {
        // Implementation would be added in a real application
    }
    
    // Getters and setters
    public String getReceiptId() {
        return receiptId;
    }
    
    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }
    
    public Payment getPayment() {
        return payment;
    }
    
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}