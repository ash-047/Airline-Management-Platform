package com.airline.model;

import java.time.LocalDateTime;

public class Enquiry {
    private String enquiryId;
    private Customer customer;
    private String subject;
    private String message;
    private String response;
    private LocalDateTime submissionDate;
    private LocalDateTime responseDate;
    private EnquiryStatus status;
    
    public String getEnquiryId() {
        return enquiryId;
    }
    
    public void setEnquiryId(String enquiryId) {
        this.enquiryId = enquiryId;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }
    
    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }
    
    public LocalDateTime getResponseDate() {
        return responseDate;
    }
    
    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }
    
    public EnquiryStatus getStatus() {
        return status;
    }
    
    public void setStatus(EnquiryStatus status) {
        this.status = status;
    }
}