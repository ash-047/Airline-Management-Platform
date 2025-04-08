package com.airline.management.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "passengers")
public class Passenger {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private int age;
    
    @Column(name = "id_type")
    private String idType;
    
    @Column(name = "id_number")
    private String idNumber;
    
    @Column(name = "seat_number")
    private String seatNumber;
    
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
}
