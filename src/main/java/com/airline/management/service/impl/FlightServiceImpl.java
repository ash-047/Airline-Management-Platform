package com.airline.management.service.impl;

import com.airline.management.model.Booking;
import com.airline.management.model.Flight;
import com.airline.management.repository.BookingRepository;
import com.airline.management.repository.FlightRepository;
import com.airline.management.service.FlightService;
import com.airline.management.util.NotificationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final NotificationManager notificationManager;

    @Autowired
    public FlightServiceImpl(FlightRepository flightRepository, 
                            BookingRepository bookingRepository,
                            NotificationManager notificationManager) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.notificationManager = notificationManager;
    }

    @Override
    public Flight saveFlight(Flight flight) {
        if (flightRepository.existsByFlightNumber(flight.getFlightNumber())) {
            throw new RuntimeException("Flight number already exists");
        }
        return flightRepository.save(flight);
    }

    @Override
    public Flight updateFlight(Flight flight) {
        Flight existingFlight = flightRepository.findById(flight.getId())
                .orElseThrow(() -> new RuntimeException("Flight not found"));
        
        if (!existingFlight.getFlightNumber().equals(flight.getFlightNumber()) 
                && flightRepository.existsByFlightNumber(flight.getFlightNumber())) {
            throw new RuntimeException("Flight number already exists");
        }
        
        return flightRepository.save(flight);
    }

    @Override
    public Flight findById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found"));
    }

    @Override
    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    @Override
    public List<Flight> findBySourceAndDestinationAndDate(String source, String destination, LocalDateTime date) {
        return flightRepository.findBySourceAndDestinationAndDepartureTimeGreaterThanAndStatusNot(
                source, destination, date, Flight.FlightStatus.CANCELLED);
    }

    @Override
    public void updateFlightStatus(Long flightId, Flight.FlightStatus status) {
        Flight flight = findById(flightId);
        Flight.FlightStatus oldStatus = flight.getStatus();
        flight.setStatus(status);
        flightRepository.save(flight);
        if (status == Flight.FlightStatus.CANCELLED) {
            List<Booking> affectedBookings = bookingRepository.findByFlightIdAndStatusNot(
                flightId, Booking.BookingStatus.CANCELLED);
            for (Booking booking : affectedBookings) {
                booking.setStatus(Booking.BookingStatus.CANCELLED);
                bookingRepository.save(booking);
                if (booking.getUser() != null && booking.getUser().getEmail() != null) {
                    notificationManager.notifyBookingCancellation(booking.getId(), booking.getUser().getEmail());
                }
            }
        } else if (oldStatus != status) {
            List<Booking> activeBookings = bookingRepository.findByFlightIdAndStatusNot(
                flightId, Booking.BookingStatus.CANCELLED);
            for (Booking booking : activeBookings) {
                if (booking.getUser() != null && booking.getUser().getEmail() != null) {
                    notificationManager.notifyFlightStatusChange(flightId, status.toString());
                }
            }
        }
    }

    @Override
    public boolean existsByFlightNumber(String flightNumber) {
        return flightRepository.existsByFlightNumber(flightNumber);
    }

    @Override
    public Flight createFlight(Flight flight) {
        // Validate the flight data
        if (flight.getSource().equals(flight.getDestination())) {
            throw new IllegalArgumentException("Source and destination cannot be the same");
        }
        
        if (flight.getDepartureTime().isAfter(flight.getArrivalTime())) {
            throw new IllegalArgumentException("Departure time cannot be after arrival time");
        }
        
        return flightRepository.save(flight);
    }

    @Override
    public List<String> findAllSources() {
        return flightRepository.findDistinctSources();
    }

    @Override
    public List<String> findAllDestinations() {
        return flightRepository.findDistinctDestinations();
    }

    @Override
    public void deleteFlight(Long id) {
        Flight flight = findById(id);
        if (flight.getBookings() != null && !flight.getBookings().isEmpty()) {
            List<Booking> activeBookings = flight.getBookings().stream()
                    .filter(b -> b.getStatus() != Booking.BookingStatus.CANCELLED)
                    .collect(Collectors.toList());
            if (!activeBookings.isEmpty()) {
                for (Booking booking : activeBookings) {
                    booking.setStatus(Booking.BookingStatus.CANCELLED);
                    bookingRepository.save(booking);
                    if (booking.getUser() != null && booking.getUser().getEmail() != null) {
                        notificationManager.notifyBookingCancellation(booking.getId(), booking.getUser().getEmail());
                    }
                }
            }
        }
        flightRepository.deleteById(id);
    }
}