package com.airline.management.service;

import com.airline.management.model.Enquiry;
import com.airline.management.model.User;

import java.util.List;

public interface EnquiryService {
    Enquiry createEnquiry(Enquiry enquiry);
    Enquiry respondToEnquiry(Long enquiryId, String response);
    Enquiry findById(Long id);
    List<Enquiry> findByUser(User user);
    List<Enquiry> findAll();
    List<Enquiry> findPendingEnquiries();
}
