package com.airline.management.repository;

import com.airline.management.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findBySourceAndDestinationAndDepartureTimeGreaterThanAndStatusNot(
            String source, String destination, LocalDateTime date, Flight.FlightStatus status);
    
    boolean existsByFlightNumber(String flightNumber);

    @Query("SELECT DISTINCT f.source FROM Flight f WHERE f.status != 'CANCELLED'")
    List<String> findDistinctSources();
    
    @Query("SELECT DISTINCT f.destination FROM Flight f WHERE f.status != 'CANCELLED'")
    List<String> findDistinctDestinations();
}
