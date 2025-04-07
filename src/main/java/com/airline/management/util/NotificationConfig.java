package com.airline.management.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class NotificationConfig {
    
    private final NotificationManager notificationManager;
    private final EmailNotifier emailNotifier;
    
    @Autowired
    public NotificationConfig(NotificationManager notificationManager, EmailNotifier emailNotifier) {
        this.notificationManager = notificationManager;
        this.emailNotifier = emailNotifier;
    }
    
    @PostConstruct
    public void init() {
        // Register observers with the notification manager
        notificationManager.addObserver(emailNotifier);
    }
}
