package com.airline.model;

public class Admin extends User {
    private String adminRole;
    
    public Admin(String userId, String username, String password, String email, String phoneNumber, String adminRole) {
        super(userId, username, password, email, phoneNumber);
        this.adminRole = adminRole;
    }
    
    public Admin() {
        super();
    }
    
    public boolean updateFlightInfo(Flight flight) {
        // Update flight information logic
        return true;
    }
    
    public Report generateReports() {
        return new Report();
    }
    
    public boolean provideFlightUpdates(Flight flight, String update) {
        return true;
    }
    
    public String getAdminRole() {
        return adminRole;
    }
    
    public void setAdminRole(String adminRole) {
        this.adminRole = adminRole;
    }
}