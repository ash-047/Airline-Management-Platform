package com.airline.management.service.impl;

import com.airline.management.model.Enquiry;
import com.airline.management.model.User;
import com.airline.management.repository.EnquiryRepository;
import com.airline.management.service.EnquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnquiryServiceImpl implements EnquiryService {

    private final EnquiryRepository enquiryRepository;

    @Autowired
    public EnquiryServiceImpl(EnquiryRepository enquiryRepository) {
        this.enquiryRepository = enquiryRepository;
    }

    @Override
    public Enquiry createEnquiry(Enquiry enquiry) {
        enquiry.setStatus(Enquiry.EnquiryStatus.PENDING);
        enquiry.setCreationDate(LocalDateTime.now());
        return enquiryRepository.save(enquiry);
    }

    @Override
    public Enquiry respondToEnquiry(Long enquiryId, String response) {
        Enquiry enquiry = findById(enquiryId);
        enquiry.setResponse(response);
        enquiry.setStatus(Enquiry.EnquiryStatus.RESOLVED);
        enquiry.setResponseDate(LocalDateTime.now());
        return enquiryRepository.save(enquiry);
    }

    @Override
    public Enquiry findById(Long id) {
        return enquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enquiry not found"));
    }

    @Override
    public List<Enquiry> findByUser(User user) {
        return enquiryRepository.findByUserOrderByCreationDateDesc(user);
    }

    @Override
    public List<Enquiry> findAll() {
        return enquiryRepository.findAll();
    }

    @Override
    public List<Enquiry> findPendingEnquiries() {
        return enquiryRepository.findByStatusOrderByCreationDateDesc(Enquiry.EnquiryStatus.PENDING);
    }
}
