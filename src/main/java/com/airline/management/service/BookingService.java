package com.airline.management.service;

import com.airline.management.model.Booking;
import com.airline.management.model.User;

import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking);
    Booking findById(Long id);
    List<Booking> findByUser(User user);
    List<Booking> findAll();
    Booking updateBookingStatus(Long bookingId, Booking.BookingStatus status);
    Booking cancelBooking(Long bookingId);
    double calculateTotalPrice(Long flightId, Booking.SeatClass seatClass, int passengerCount);
    List<Booking> findRecentBookingsByUser(User user);
}
