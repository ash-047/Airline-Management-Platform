package com.airline.management.service;

import com.airline.management.model.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightService {
    Flight saveFlight(Flight flight);
    Flight updateFlight(Flight flight);
    Flight findById(Long id);
    List<Flight> findAll();
    List<Flight> findBySourceAndDestinationAndDate(String source, String destination, LocalDateTime date);

    List<String> findAllSources();
    List<String> findAllDestinations();

    void updateFlightStatus(Long flightId, Flight.FlightStatus status);
    boolean existsByFlightNumber(String flightNumber);

    /**
     * Creates a new flight in the system
     * @param flight The flight to create
     * @return The saved flight with assigned ID
     */
    Flight createFlight(Flight flight);

    /**
     * Deletes a flight by its id
     * @param id The id of the flight to delete
     */
    void deleteFlight(Long id);
}
