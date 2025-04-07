package com.airline.management.controller;

import com.airline.management.model.Enquiry;
import com.airline.management.model.User;
import com.airline.management.service.EnquiryService;
import com.airline.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EnquiryController {

    private final EnquiryService enquiryService;
    private final UserService userService;

    @Autowired
    public EnquiryController(EnquiryService enquiryService, UserService userService) {
        this.enquiryService = enquiryService;
        this.userService = userService;
    }

    @GetMapping("/user/enquiries")
    public String viewEnquiries(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName());
        List<Enquiry> enquiries = enquiryService.findByUser(user);
        
        model.addAttribute("enquiries", enquiries);
        model.addAttribute("newEnquiry", new Enquiry());
        return "user/enquiries";
    }

    @PostMapping("/user/enquiries/submit")
    public String submitEnquiry(
            @ModelAttribute Enquiry enquiry,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        
        try {
            User user = userService.findByUsername(authentication.getName());
            enquiry.setUser(user);
            enquiryService.createEnquiry(enquiry);
            redirectAttributes.addFlashAttribute("success", "Enquiry submitted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error submitting enquiry: " + e.getMessage());
        }
        
        return "redirect:/user/enquiries";
    }

    @GetMapping("/admin/enquiries")
    public String viewAllEnquiries(Model model) {
        List<Enquiry> enquiries = enquiryService.findAll();
        model.addAttribute("enquiries", enquiries);
        return "admin/enquiries";
    }

    @GetMapping("/admin/enquiries/{id}")
    public String viewEnquiry(@PathVariable Long id, Model model) {
        Enquiry enquiry = enquiryService.findById(id);
        model.addAttribute("enquiry", enquiry);
        return "admin/enquiry-detail";
    }

    @PostMapping("/admin/enquiries/{id}/respond")
    public String respondToEnquiry(
            @PathVariable Long id,
            @RequestParam String response,
            RedirectAttributes redirectAttributes) {
        
        try {
            enquiryService.respondToEnquiry(id, response);
            redirectAttributes.addFlashAttribute("success", "Response sent successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error sending response: " + e.getMessage());
        }
        
        return "redirect:/admin/enquiries";
    }
}
