package com.airline.management.controller;

import com.airline.management.model.Flight;
import com.airline.management.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class AdminController {

    @Autowired
    private FlightService flightService;

    @PostMapping("/admin/flights/add")
    public String addFlight(
            @RequestParam String flightNumber,
            @RequestParam String source, 
            @RequestParam String destination,
            @RequestParam LocalDateTime departureTime,
            @RequestParam LocalDateTime arrivalTime,
            @RequestParam Flight.FlightStatus status,
            @RequestParam int economySeats,
            @RequestParam int businessSeats,
            @RequestParam int firstClassSeats,
            @RequestParam double economyPrice,
            @RequestParam double businessPrice,
            @RequestParam double firstClassPrice,
            RedirectAttributes redirectAttributes) {
        try {
            Flight flight = new Flight();
            flight.setFlightNumber(flightNumber);
            flight.setSource(source);
            flight.setDestination(destination);
            flight.setDepartureTime(departureTime);
            flight.setArrivalTime(arrivalTime);
            flight.setStatus(status);
            flight.setEconomySeats(economySeats);
            flight.setBusinessSeats(businessSeats);
            flight.setFirstClassSeats(firstClassSeats);
            flight.setEconomyPrice(economyPrice);
            flight.setBusinessPrice(businessPrice);
            flight.setFirstClassPrice(firstClassPrice);
            
            flightService.createFlight(flight);
            redirectAttributes.addFlashAttribute("success", "Flight added successfully");
            return "redirect:/admin/flights";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding flight: " + e.getMessage());
            return "redirect:/admin/flights";
        }
    }
}