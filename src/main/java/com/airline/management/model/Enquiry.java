package com.airline.management.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "enquiries")
public class Enquiry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String subject;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @Column(columnDefinition = "TEXT")
    private String response;
    
    @Enumerated(EnumType.STRING)
    private EnquiryStatus status = EnquiryStatus.PENDING;
    
    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();
    
    @Column(name = "response_date")
    private LocalDateTime responseDate;
    
    public enum EnquiryStatus {
        PENDING, RESOLVED
    }
}
