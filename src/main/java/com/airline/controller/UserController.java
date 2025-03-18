package com.airline.controller;

import com.airline.model.User;
import com.airline.model.Customer;
import com.airline.model.Admin;
import com.airline.util.DatabaseUtil;
import com.airline.util.AuthenticationService;

import java.util.HashMap;
import java.util.Map;

// Singleton pattern for UserController
public class UserController {
    private static UserController instance;
    private AuthenticationService authService;
    
    private UserController() {
        // Initialize authentication service
        authService = new AuthenticationService();
    }
    
    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }
    
    public User authenticateUser(String username, String password) {
        // In a real app, this would validate against a database
        return authService.authenticate(username, password);
    }
    
    public boolean registerUser(Map<String, String> userData) {
        try {
            // Extract user data
            String userId = DatabaseUtil.generateUniqueId();
            String username = userData.get("username");
            String password = userData.get("password");
            String email = userData.get("email");
            String phoneNumber = userData.get("phoneNumber");
            
            // Create a new customer account (for simplicity, all registrations create customers)
            Customer newCustomer = new Customer(userId, username, password, email, phoneNumber);
            
            // In a real app, this would save to a database
            return authService.registerUser(newCustomer);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateUserProfile(User user, Map<String, String> updatedData) {
        try {
            // Update user data
            if (updatedData.containsKey("username")) {
                user.setUsername(updatedData.get("username"));
            }
            if (updatedData.containsKey("email")) {
                user.setEmail(updatedData.get("email"));
            }
            if (updatedData.containsKey("phoneNumber")) {
                user.setPhoneNumber(updatedData.get("phoneNumber"));
            }
            if (updatedData.containsKey("password")) {
                user.setPassword(updatedData.get("password"));
            }
            
            // In a real app, this would update the database
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // For demo purposes, quickly create a sample admin account
    public Admin createSampleAdmin() {
        Admin admin = new Admin("admin1", "admin", "admin123", "admin@airline.com", "123456789", "System Admin");
        authService.registerAdmin(admin);
        return admin;
    }
}