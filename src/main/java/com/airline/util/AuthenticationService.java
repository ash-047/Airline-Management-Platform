package com.airline.util;

import com.airline.model.Admin;
import com.airline.model.Customer;
import com.airline.model.User;

import java.util.HashMap;
import java.util.Map;

// Singleton pattern
public class AuthenticationService {
    private static final Map<String, User> users = new HashMap<>();
    
    public AuthenticationService() {
        // Initialize with some demo users if map is empty
        if (users.isEmpty()) {
            // Create a default customer
            Customer customer = new Customer("C001", "user", "password", "user@example.com", "123-456-7890");
            users.put(customer.getUsername(), customer);
            
            // Create a default admin
            Admin admin = new Admin("A001", "admin", "admin123", "admin@airline.com", "987-654-3210", "System Admin");
            users.put(admin.getUsername(), admin);
        }
    }
    
    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    
    public boolean registerUser(Customer customer) {
        // Check if username already exists
        if (users.containsKey(customer.getUsername())) {
            return false;
        }
        
        users.put(customer.getUsername(), customer);
        return true;
    }
    
    public boolean registerAdmin(Admin admin) {
        // Check if username already exists
        if (users.containsKey(admin.getUsername())) {
            return false;
        }
        
        users.put(admin.getUsername(), admin);
        return true;
    }
}