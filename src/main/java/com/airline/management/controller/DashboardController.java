package com.airline.management.controller;

import com.airline.management.model.Booking;
import com.airline.management.model.Enquiry;
import com.airline.management.model.Flight;
import com.airline.management.model.User;
import com.airline.management.service.BookingService;
import com.airline.management.service.EnquiryService;
import com.airline.management.service.FlightService;
import com.airline.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final UserService userService;
    private final BookingService bookingService;
    private final FlightService flightService;
    private final EnquiryService enquiryService;

    @Autowired
    public DashboardController(
            UserService userService, 
            BookingService bookingService, 
            FlightService flightService, 
            EnquiryService enquiryService) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.flightService = flightService;
        this.enquiryService = enquiryService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        // Redirect to appropriate dashboard based on user role
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/user/dashboard";
        }
    }

    @GetMapping("/user/dashboard")
    public String userDashboard(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName());
        
        // Get recent bookings for the dashboard
        List<Booking> recentBookings = bookingService.findByUser(user);
        
        // Get user's enquiries
        List<Enquiry> enquiries = enquiryService.findByUser(user);
        
        model.addAttribute("user", user);
        model.addAttribute("recentBookings", recentBookings);
        model.addAttribute("enquiries", enquiries);
        
        return "user/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        // Get all flights for management
        List<Flight> flights = flightService.findAll();
        
        // Get pending enquiries that need responses
        List<Enquiry> pendingEnquiries = enquiryService.findPendingEnquiries();
        
        model.addAttribute("flights", flights);
        model.addAttribute("pendingEnquiries", pendingEnquiries);
        
        return "admin/dashboard";
    }
    
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }
}
