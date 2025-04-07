package com.airline.management.service;

import com.airline.management.model.User;

public interface UserService {
    User registerUser(User user);
    User findByUsername(String username);
    User findById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
