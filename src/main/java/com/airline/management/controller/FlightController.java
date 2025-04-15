package com.airline.management.controller;

import com.airline.management.model.Flight;
import com.airline.management.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class FlightController {
    private final FlightService flightService;
    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }
    @GetMapping("/user/search-flights")
    public String searchFlightsPage(Model model) {
        List<String> sources = flightService.findAllSources();
        List<String> destinations = flightService.findAllDestinations();
        model.addAttribute("sources", sources);
        model.addAttribute("destinations", destinations);
        return "user/search-flights";
    }
    @PostMapping("/user/search-flights")
    public String searchFlights(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        LocalDateTime dateTime = date.atStartOfDay();
        List<Flight> flights = flightService.findBySourceAndDestinationAndDate(source, destination, dateTime);
        model.addAttribute("flights", flights);
        model.addAttribute("source", source);
        model.addAttribute("destination", destination);
        model.addAttribute("date", date);
        List<String> sources = flightService.findAllSources();
        List<String> destinations = flightService.findAllDestinations();
        model.addAttribute("sources", sources);
        model.addAttribute("destinations", destinations);
        return "user/search-flights";
    }
    @GetMapping("/admin/flights")
    public String manageFlights(Model model) {
        List<Flight> flights = flightService.findAll();
        model.addAttribute("flights", flights);
        model.addAttribute("newFlight", new Flight());
        model.addAttribute("statuses", Flight.FlightStatus.values());
        return "admin/flights";
    }
    @PostMapping("/admin/flights/{id}/update-status")
    public String updateFlightStatus(
            @PathVariable Long id,
            @RequestParam Flight.FlightStatus status,
            RedirectAttributes redirectAttributes) {
        try {
            flightService.updateFlightStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Flight status updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating flight status: " + e.getMessage());
        }
        return "redirect:/admin/flights";
    }
    @GetMapping("/admin/flights/{id}/edit")
    public String editFlightForm(@PathVariable Long id, Model model) {
        try {
            Flight flight = flightService.findById(id);
            model.addAttribute("flight", flight);
            model.addAttribute("statuses", Flight.FlightStatus.values());
            return "admin/edit-flight";
        } catch (Exception e) {
            return "redirect:/admin/flights";
        }
    }
    @PostMapping("/admin/flights/{id}/update")
    public String updateFlight(
            @PathVariable Long id,
            @RequestParam String flightNumber,
            @RequestParam String source, 
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalTime,
            @RequestParam Flight.FlightStatus status,
            @RequestParam int economySeats,
            @RequestParam int businessSeats,
            @RequestParam int firstClassSeats,
            @RequestParam double economyPrice,
            @RequestParam double businessPrice,
            @RequestParam double firstClassPrice,
            RedirectAttributes redirectAttributes) {
        try {
            Flight flight = flightService.findById(id);
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
            flightService.updateFlight(flight);
            redirectAttributes.addFlashAttribute("success", "Flight updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating flight: " + e.getMessage());
        }
        return "redirect:/admin/flights";
    }
    @PostMapping("/admin/flights/{id}/delete")
    public String deleteFlight(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            flightService.deleteFlight(id);
            redirectAttributes.addFlashAttribute("success", "Flight deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting flight: " + e.getMessage());
        }
        return "redirect:/admin/flights";
    }
}
