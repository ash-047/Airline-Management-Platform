package com.airline.controller;

import com.airline.model.Customer;
import com.airline.model.Enquiry;
import com.airline.model.EnquiryStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnquiryController {
    private static EnquiryController instance;
    private List<Enquiry> enquiries;
    
    private EnquiryController() {
        enquiries = new ArrayList<>();
    }
    
    public static EnquiryController getInstance() {
        if (instance == null) {
            instance = new EnquiryController();
        }
        return instance;
    }
    
    public List<Enquiry> getAllEnquiries() {
        return new ArrayList<>(enquiries);
    }
    
    public List<Enquiry> getCustomerEnquiries(Customer customer) {
        List<Enquiry> customerEnquiries = new ArrayList<>();
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getCustomer().getUserId().equals(customer.getUserId())) {
                customerEnquiries.add(enquiry);
            }
        }
        return customerEnquiries;
    }
    
    public boolean submitEnquiry(Customer customer, String subject, String message) {
        if (customer == null || subject == null || message == null || subject.isEmpty() || message.isEmpty()) {
            return false;
        }
        
        Enquiry enquiry = new Enquiry();
        enquiry.setEnquiryId(UUID.randomUUID().toString().substring(0, 8));
        enquiry.setCustomer(customer);
        enquiry.setSubject(subject);
        enquiry.setMessage(message);
        enquiry.setSubmissionDate(LocalDateTime.now());
        enquiry.setStatus(EnquiryStatus.PENDING);
        
        enquiries.add(enquiry);
        return true;
    }
    
    public boolean respondToEnquiry(String enquiryId, String response) {
        if (enquiryId == null || response == null || response.isEmpty()) {
            return false;
        }
        
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getEnquiryId().equals(enquiryId)) {
                enquiry.setResponse(response);
                enquiry.setStatus(EnquiryStatus.RESOLVED);
                enquiry.setResponseDate(LocalDateTime.now());
                return true;
            }
        }
        
        return false;
    }
    
    public Enquiry getEnquiryById(String enquiryId) {
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getEnquiryId().equals(enquiryId)) {
                return enquiry;
            }
        }
        return null;
    }
}