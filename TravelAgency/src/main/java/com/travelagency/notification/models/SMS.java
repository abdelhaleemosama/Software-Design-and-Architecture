package com.travelagency.notification.models;

import org.springframework.stereotype.Component;

@Component
public class SMS extends Method {
    @Override
    public void send(String message) {
        // Mock implementation
    }
}
