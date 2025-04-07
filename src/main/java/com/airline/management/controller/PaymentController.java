package com.airline.management.controller;

import com.airline.management.model.Booking;
import com.airline.management.model.Payment;
import com.airline.management.model.User;
import com.airline.management.service.BookingService;
import com.airline.management.service.PaymentService;
import com.airline.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PaymentController {

    private final PaymentService paymentService;
    private final BookingService bookingService;
    private final UserService userService;

    @Autowired
    public PaymentController(
            PaymentService paymentService,
            BookingService bookingService,
            UserService userService) {
        this.paymentService = paymentService;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @PostMapping("/user/payment/process")
    public String processPayment(
            @RequestParam Long bookingId,
            @RequestParam Payment.PaymentMethod paymentMethod,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) String cardHolder,
            @RequestParam(required = false) String expiryDate,
            @RequestParam(required = false) String cvv,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        
        try {
            User user = userService.findByUsername(authentication.getName());
            Booking booking = bookingService.findById(bookingId);
            
            // Verify that the booking belongs to the authenticated user
            if (!booking.getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "You can only make payments for your own bookings");
                return "redirect:/user/dashboard";
            }
            
            // Create a new payment
            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setAmount(booking.getTotalPrice());
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
                redirectAttributes.addFlashAttribute("success", "Payment processed successfully");
                return "redirect:/user/bookings/" + bookingId + "/ticket";
            } else {
                redirectAttributes.addFlashAttribute("error", "Payment failed");
                return "redirect:/user/bookings";
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Payment error: " + e.getMessage());
            return "redirect:/user/bookings";
        }
    }

    @GetMapping("/user/bookings/{id}/ticket")
    public String viewTicket(@PathVariable Long id, Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(authentication.getName());
            Booking booking = bookingService.findById(id);
            
            // Verify that the booking belongs to the authenticated user
            if (!booking.getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "You can only view your own bookings");
                return "redirect:/user/dashboard";
            }
            
            // Check if booking is confirmed
            if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
                redirectAttributes.addFlashAttribute("error", "No ticket available for this booking");
                return "redirect:/user/bookings";
            }
            
            model.addAttribute("booking", booking);
            return "user/ticket";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error retrieving ticket: " + e.getMessage());
            return "redirect:/user/bookings";
        }
    }
}
