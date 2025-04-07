package com.airline.management.config;

import com.airline.management.model.Flight;
import com.airline.management.model.User;
import com.airline.management.service.FlightService;
import com.airline.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;
    private final FlightService flightService;

    @Autowired
    public DatabaseInitializer(UserService userService, FlightService flightService) {
        this.userService = userService;
        this.flightService = flightService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Add admin user if it doesn't exist
        if (!userService.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setEmail("admin@airline.com");
            admin.setPhoneNumber("1234567890");
            admin.setRole(User.Role.ADMIN);
            userService.registerUser(admin);
        }
        
        // Add a regular user for testing
        if (!userService.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword("user123");
            user.setEmail("user@example.com");
            user.setPhoneNumber("0987654321");
            user.setRole(User.Role.CUSTOMER);
            userService.registerUser(user);
        }
        
        // Add some sample flights
        addSampleFlights();
    }
    
    private void addSampleFlights() {
        String[] sources = {"New York", "London", "Tokyo", "Dubai", "Paris"};
        String[] destinations = {"Los Angeles", "Singapore", "Sydney", "Mumbai", "Toronto"};
        
        for (int i = 0; i < 5; i++) {
            if (!flightService.existsByFlightNumber("FL" + (i + 100))) {
                Flight flight = new Flight();
                flight.setFlightNumber("FL" + (i + 100));
                flight.setSource(sources[i]);
                flight.setDestination(destinations[i]);
                
                // Set departure time to a future date
                LocalDateTime departure = LocalDateTime.now().plusDays(i + 1).withHour(10).withMinute(0);
                flight.setDepartureTime(departure);
                
                // Set arrival time based on a reasonable flight duration
                flight.setArrivalTime(departure.plusHours(5 + i));
                
                // Set default prices
                flight.setEconomyPrice(10000.0 + (i * 2000));
                flight.setBusinessPrice(25000.0 + (i * 5000));
                flight.setFirstClassPrice(50000.0 + (i * 10000));
                
                flightService.saveFlight(flight);
            }
        }
    }
}
