package com.airline.management.service.impl;

import com.airline.management.model.Booking;
import com.airline.management.model.Flight;
import com.airline.management.model.User;
import com.airline.management.repository.BookingRepository;
import com.airline.management.service.BookingService;
import com.airline.management.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final FlightService flightService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, FlightService flightService) {
        this.bookingRepository = bookingRepository;
        this.flightService = flightService;
    }

    @Override
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public List<Booking> findByUser(User user) {
        return bookingRepository.findByUserOrderByBookingDateDesc(user);
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking updateBookingStatus(Long bookingId, Booking.BookingStatus status) {
        Booking booking = findById(bookingId);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking cancelBooking(Long bookingId) {
        Booking booking = findById(bookingId);
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        if (booking.getPayment() != null) {
            // Process refund based on business rules
        }
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> findRecentBookingsByUser(User user) {
        List<Booking> bookings = bookingRepository.findByUserOrderByBookingDateDesc(user);
        LocalDateTime now = LocalDateTime.now();
        return bookings.stream()
            .filter(b -> b.getFlight().getDepartureTime().isAfter(now.minusDays(1)))
            .limit(5)
            .collect(Collectors.toList());
    }

    @Override
    public double calculateTotalPrice(Long flightId, Booking.SeatClass seatClass, int passengerCount) {
        Flight flight = flightService.findById(flightId);
        double basePrice;
        
        switch (seatClass) {
            case ECONOMY:
                basePrice = flight.getEconomyPrice();
                break;
            case BUSINESS:
                basePrice = flight.getBusinessPrice();
                break;
            case FIRST_CLASS:
                basePrice = flight.getFirstClassPrice();
                break;
            default:
                throw new RuntimeException("Invalid seat class");
        }
        
        // add taxes - 10%
        double total = basePrice * passengerCount * 1.1;
        return Math.round(total * 100.0) / 100.0; // Round to 2 decimal places
    }
}
