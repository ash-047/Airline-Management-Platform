package com.airline.management.repository;

import com.airline.management.model.Enquiry;
import com.airline.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
    List<Enquiry> findByUserOrderByCreationDateDesc(User user);
    List<Enquiry> findByStatusOrderByCreationDateDesc(Enquiry.EnquiryStatus status);
}
