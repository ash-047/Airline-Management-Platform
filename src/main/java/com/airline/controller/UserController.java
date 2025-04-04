package com.airline.controller;

import com.airline.model.User;
import com.airline.model.Customer;
import com.airline.model.Admin;
import com.airline.util.DatabaseUtil;
import com.airline.util.AuthenticationService;

// import java.util.HashMap;
import java.util.Map;

public class UserController {
    private static UserController instance;
    private AuthenticationService authService;
    
    private UserController() {
        authService = new AuthenticationService();
    }
    
    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }
    
    public User authenticateUser(String username, String password) {
        return authService.authenticate(username, password);
    }
    
    public boolean registerUser(Map<String, String> userData) {
        try {
            String userId = DatabaseUtil.generateUniqueId();
            String username = userData.get("username");
            String password = userData.get("password");
            String email = userData.get("email");
            String phoneNumber = userData.get("phoneNumber");
            
            Customer newCustomer = new Customer(userId, username, password, email, phoneNumber);
            
            return authService.registerUser(newCustomer);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateUserProfile(User user, Map<String, String> updatedData) {
        try {
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
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Admin createSampleAdmin() {
        Admin admin = new Admin("admin1", "admin", "admin123", "admin@airline.com", "123456789", "System Admin");
        authService.registerAdmin(admin);
        return admin;
    }
}