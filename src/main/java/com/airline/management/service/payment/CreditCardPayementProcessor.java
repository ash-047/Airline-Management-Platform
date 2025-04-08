package com.airline.management.service.payment;

import com.airline.management.model.Payment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CreditCardPaymentProcessor implements PaymentProcessor {
    
    @Override
    public Payment process(Payment payment) {
        // Simulate credit card processing
        validateCreditCardDetails(payment);
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());
        return payment;
    }

    @Override
    public Payment refund(Payment payment) {
        // Simulate credit card refund
        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        return payment;
    }
    
    private void validateCreditCardDetails(Payment payment) {
        // Basic validation - in a real application, we'd have more robust validation
        if (payment.getCardNumber() == null || payment.getCardNumber().length() < 16) {
            throw new RuntimeException("Invalid card number");
        }
        
        if (payment.getCardHolder() == null || payment.getCardHolder().isEmpty()) {
            throw new RuntimeException("Card holder name is required");
        }
        
        if (payment.getExpiryDate() == null || !payment.getExpiryDate().matches("\\d{2}/\\d{2}")) {
            throw new RuntimeException("Invalid expiry date format (MM/YY)");
        }
        
        if (payment.getCvv() == null || payment.getCvv().length() < 3) {
            throw new RuntimeException("Invalid CVV");
        }
    }
}
