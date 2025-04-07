package com.airline.management.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "flights")
public class Flight {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String flightNumber;
    
    @Column(nullable = false)
    private String source;
    
    @Column(nullable = false)
    private String destination;
    
    @Column(nullable = false)
    private LocalDateTime departureTime;
    
    @Column(nullable = false)
    private LocalDateTime arrivalTime;
    
    @Enumerated(EnumType.STRING)
    private FlightStatus status = FlightStatus.SCHEDULED;
    
    @Column(name = "economy_seats")
    private int economySeats = 100;
    
    @Column(name = "business_seats")
    private int businessSeats = 20;
    
    @Column(name = "first_class_seats")
    private int firstClassSeats = 10;
    
    @Column(name = "economy_price")
    private double economyPrice = 200.0;
    
    @Column(name = "business_price")
    private double businessPrice = 500.0;
    
    @Column(name = "first_class_price")
    private double firstClassPrice = 1000.0;
    
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();
    
    public enum FlightStatus {
        SCHEDULED, DELAYED, BOARDING, IN_AIR, LANDED, CANCELLED
    }
}

