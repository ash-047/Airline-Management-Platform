package com.airline.management.service.payment;

import com.airline.management.model.Payment;

// Strategy Pattern: Interface for different payment processing strategies
public interface PaymentProcessor {
    Payment process(Payment payment);
    Payment refund(Payment payment);
}
