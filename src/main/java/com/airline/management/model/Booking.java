package com.airline.management.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;
    
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Passenger> passengers = new ArrayList<>();
    
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;
    
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;
    
    @Column(name = "booking_date")
    private LocalDateTime bookingDate = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private SeatClass seatClass;
    
    @Column(name = "total_price")
    private double totalPrice;
    
    public enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED
    }
    
    public enum SeatClass {
        ECONOMY, BUSINESS, FIRST_CLASS
    }
}
