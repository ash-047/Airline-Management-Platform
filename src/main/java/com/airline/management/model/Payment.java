package com.airline.management.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "payments")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @Column(nullable = false)
    private double amount;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;
    
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    
    @Column(name = "card_number")
    private String cardNumber;
    
    @Column(name = "card_holder")
    private String cardHolder;
    
    @Column(name = "expiry_date")
    private String expiryDate;
    
    private String cvv;
    
    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, NET_BANKING
    }
    
    public enum PaymentStatus {
        PENDING, COMPLETED, REFUNDED, FAILED
    }
}
