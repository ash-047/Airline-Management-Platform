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
// import java.time.LocalTime;
import java.util.List;

@Controller
public class FlightController {

    private final FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    // @GetMapping("/user/search-flights")
    // public String searchFlightsPage(Model model) {
    //     model.addAttribute("sources", new String[]{"New York", "London", "Tokyo", "Dubai", "Paris"});
    //     model.addAttribute("destinations", new String[]{"Los Angeles", "Singapore", "Sydney", "Mumbai", "Toronto"});
    //     return "user/search-flights";
    // }


    // @PostMapping("/user/search-flights")
    // public String searchFlights(
    //         @RequestParam String source,
    //         @RequestParam String destination,
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
    //         Model model) {
        
    //     // Convert LocalDate to LocalDateTime (start of day)
    //     LocalDateTime dateTime = date.atStartOfDay();
        
    //     List<Flight> flights = flightService.findBySourceAndDestinationAndDate(source, destination, dateTime);
        
    //     model.addAttribute("flights", flights);
    //     model.addAttribute("source", source);
    //     model.addAttribute("destination", destination);
    //     model.addAttribute("date", date);
        
    //     // Keep the dropdown data
    //     model.addAttribute("sources", new String[]{"New York", "London", "Tokyo", "Dubai", "Paris"});
    //     model.addAttribute("destinations", new String[]{"Los Angeles", "Singapore", "Sydney", "Mumbai", "Toronto"});
        
    //     return "user/search-flights";
    // }

    @GetMapping("/user/search-flights")
public String showSearchFlights(Model model) {
    // Get all unique sources and destinations from the database
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
    
    // Convert LocalDate to LocalDateTime (start of day)
    LocalDateTime dateTime = date.atStartOfDay();
    
    List<Flight> flights = flightService.findBySourceAndDestinationAndDate(source, destination, dateTime);
    
    model.addAttribute("flights", flights);
    model.addAttribute("source", source);
    model.addAttribute("destination", destination);
    model.addAttribute("date", date);
    
    // Get all unique sources and destinations from the database
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
}
