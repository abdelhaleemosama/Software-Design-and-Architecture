package com.travelagency.notification.models;

import com.travelagency.user.domain.User;

import java.util.List;
import java.util.Random;

public class BulkNotification {
    private List<User> recipients;

    public BulkNotification(List<User> recipients) {
        this.recipients = recipients;
    }

    public void sendAll(NotificationTemplate template, Method messageType) {
        Random random = new Random();
        for (User recipient : recipients) {
            Notification notification = new Notification("unsent", random.nextInt(), messageType, recipient);
            notification.createAndSend(template);
        }
    }
} 