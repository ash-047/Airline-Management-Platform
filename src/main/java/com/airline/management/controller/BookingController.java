package com.airline.management.controller;

import com.airline.management.model.Booking;
import com.airline.management.model.Flight;
import com.airline.management.model.Passenger;
import com.airline.management.model.Payment;
import com.airline.management.model.User;
import com.airline.management.service.BookingService;
import com.airline.management.service.FlightService;
import com.airline.management.service.PaymentService;
import com.airline.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final FlightService flightService;
    private final UserService userService;
    private final PaymentService paymentService;

    @Autowired
    public BookingController(
            BookingService bookingService,
            FlightService flightService,
            UserService userService,
            PaymentService paymentService) {
        this.bookingService = bookingService;
        this.flightService = flightService;
        this.userService = userService;
        this.paymentService = paymentService;
    }

    @GetMapping("/user/bookings")
    public String viewBookings(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName());
        List<Booking> bookings = bookingService.findByUser(user);
        model.addAttribute("bookings", bookings);
        return "user/bookings";
    }

    @GetMapping("/user/book/{flightId}")
    public String bookFlight(@PathVariable Long flightId, Model model) {
        Flight flight = flightService.findById(flightId);
        model.addAttribute("flight", flight);
        model.addAttribute("seatClasses", Booking.SeatClass.values());
        model.addAttribute("paymentMethods", Payment.PaymentMethod.values());
        return "user/book-flight";
    }

    @PostMapping("/user/book/complete")
    public String completeBooking(
            @RequestParam Long flightId,
            @RequestParam Booking.SeatClass seatClass,
            @RequestParam int passengerCount,
            @RequestParam(required = false) String[] passengerName,
            @RequestParam(required = false) Integer[] passengerAge,
            @RequestParam(required = false) String[] passengerIdType,
            @RequestParam(required = false) String[] passengerIdNumber,
            @RequestParam(required = false) String[] passengerSeatNumber,
            @RequestParam Payment.PaymentMethod paymentMethod,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) String cardHolder,
            @RequestParam(required = false) String expiryDate,
            @RequestParam(required = false) String cvv,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        
        try {
            User user = userService.findByUsername(authentication.getName());
            Flight flight = flightService.findById(flightId);
            
            // Calculate total price
            double totalPrice = bookingService.calculateTotalPrice(flightId, seatClass, passengerCount);
            
            // Create a new booking
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setFlight(flight);
            booking.setSeatClass(seatClass);
            booking.setTotalPrice(totalPrice);
            booking.setStatus(Booking.BookingStatus.PENDING);
            
            // Add passengers to booking
            List<Passenger> passengers = new ArrayList<>();
            for (int i = 0; i < passengerCount; i++) {
                if (passengerName != null && i < passengerName.length) {
                    Passenger passenger = new Passenger();
                    passenger.setName(passengerName[i]);
                    passenger.setAge(passengerAge[i]);
                    passenger.setIdType(passengerIdType[i]);
                    passenger.setIdNumber(passengerIdNumber[i]);
                    passenger.setSeatNumber(passengerSeatNumber[i]);
                    passenger.setBooking(booking);
                    passengers.add(passenger);
                }
            }
            booking.setPassengers(passengers);
            
            // Save the booking
            Booking savedBooking = bookingService.createBooking(booking);
            
            // Create a payment
            Payment payment = new Payment();
            payment.setBooking(savedBooking);
            payment.setAmount(totalPrice);
            payment.setPaymentMethod(paymentMethod);
            payment.setStatus(Payment.PaymentStatus.PENDING);
            
            // Set card details if using card payment methods
            if (paymentMethod == Payment.PaymentMethod.CREDIT_CARD || 
                    paymentMethod == Payment.PaymentMethod.DEBIT_CARD) {
                payment.setCardNumber(cardNumber);
                payment.setCardHolder(cardHolder);
                payment.setExpiryDate(expiryDate);
                payment.setCvv(cvv);
            }
            
            // Process the payment
            Payment processedPayment = paymentService.processPayment(payment);
            
            if (processedPayment.getStatus() == Payment.PaymentStatus.COMPLETED) {
                redirectAttributes.addFlashAttribute("success", "Booking completed successfully. Your ticket is confirmed.");
                return "redirect:/user/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "Payment failed");
                return "redirect:/user/bookings";
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Booking error: " + e.getMessage());
            return "redirect:/user/search-flights";
        }
    }

    @PostMapping("/user/bookings/{id}/cancel")
    public String cancelBooking(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        
        try {
            User user = userService.findByUsername(authentication.getName());
            Booking booking = bookingService.findById(id);
            
            // Make sure the booking belongs to the user
            if (!booking.getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "You can only cancel your own bookings");
                return "redirect:/user/bookings";
            }
            
            bookingService.cancelBooking(id);
            redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error cancelling booking: " + e.getMessage());
        }
        
        return "redirect:/user/bookings";
    }

    @GetMapping("/admin/bookings")
    public String viewAllBookings(Model model) {
        List<Booking> bookings = bookingService.findAll();
        model.addAttribute("bookings", bookings);
        return "admin/bookings";
    }
}
