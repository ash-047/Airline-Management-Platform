package com.airline.management.repository;

import com.airline.management.model.Booking;
import com.airline.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserOrderByBookingDateDesc(User user);
    List<Booking> findByFlightId(Long flightId);
}
