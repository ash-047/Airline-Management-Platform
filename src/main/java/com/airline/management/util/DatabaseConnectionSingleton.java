package com.airline.management.util;

import javax.sql.DataSource;

// Singleton Pattern: Ensures a single instance of database connection
public class DatabaseConnectionSingleton {
    
    private static DatabaseConnectionSingleton instance;
    private DataSource dataSource;
    
    private DatabaseConnectionSingleton() {
        // Private constructor to prevent instantiation
    }
    
    public static synchronized DatabaseConnectionSingleton getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionSingleton();
        }
        return instance;
    }
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public DataSource getDataSource() {
        return dataSource;
    }
}
