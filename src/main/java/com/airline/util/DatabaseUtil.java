package com.airline.util;

import java.util.UUID;

public class DatabaseUtil {
    // This is a utility class that would normally handle database connections
    // For this demo, it just provides helper methods
    
    public static String generateUniqueId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}