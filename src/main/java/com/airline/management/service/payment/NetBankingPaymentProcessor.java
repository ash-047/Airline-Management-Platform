package com.airline.management.service.payment;

import com.airline.management.model.Payment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NetBankingPaymentProcessor implements PaymentProcessor {
    
    @Override
    public Payment process(Payment payment) {
        // Simulate net banking processing
        // In a real implementation, this would redirect to a bank's page
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());
        return payment;
    }

    @Override
    public Payment refund(Payment payment) {
        // Simulate net banking refund
        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        return payment;
    }
}
