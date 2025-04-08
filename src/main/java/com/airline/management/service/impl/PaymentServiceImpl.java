package com.airline.management.service.impl;

import com.airline.management.model.Booking;
import com.airline.management.model.Payment;
import com.airline.management.repository.BookingRepository;
import com.airline.management.repository.PaymentRepository;
import com.airline.management.service.PaymentService;
import com.airline.management.service.factory.PaymentProcessorFactory;
import com.airline.management.service.payment.PaymentProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentProcessorFactory paymentProcessorFactory;

    @Autowired
    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            BookingRepository bookingRepository,
            PaymentProcessorFactory paymentProcessorFactory) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.paymentProcessorFactory = paymentProcessorFactory;
    }

    @Override
    public Payment processPayment(Payment payment) {
        PaymentProcessor processor = paymentProcessorFactory.getProcessor(payment.getPaymentMethod());
        Payment processedPayment = processor.process(payment);
        
        // Update booking status if payment is successful
        if (processedPayment.getStatus() == Payment.PaymentStatus.COMPLETED) {
            Booking booking = processedPayment.getBooking();
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
            bookingRepository.save(booking);
        }
        
        return paymentRepository.save(processedPayment);
    }

    @Override
    public Payment refundPayment(Long paymentId) {
        Payment payment = findById(paymentId);
        
        // Can only refund completed payments
        if (payment.getStatus() != Payment.PaymentStatus.COMPLETED) {
            throw new RuntimeException("Cannot refund a payment that is not completed");
        }
        
        PaymentProcessor processor = paymentProcessorFactory.getProcessor(payment.getPaymentMethod());
        Payment refundedPayment = processor.refund(payment);
        
        return paymentRepository.save(refundedPayment);
    }

    @Override
    public Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Override
    public Payment findByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking"));
    }
}
