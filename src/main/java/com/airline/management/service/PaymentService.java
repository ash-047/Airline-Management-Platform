package com.airline.management.service;

import com.airline.management.model.Payment;

public interface PaymentService {
    Payment processPayment(Payment payment);
    Payment refundPayment(Long paymentId);
    Payment findById(Long id);
    Payment findByBookingId(Long bookingId);
}
