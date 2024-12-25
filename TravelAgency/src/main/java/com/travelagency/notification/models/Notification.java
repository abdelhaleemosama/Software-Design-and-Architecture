package com.travelagency.notification.models;

import com.travelagency.user.domain.User;
import com.travelagency.notification.services.NotificationController;
import lombok.Data;

@Data
public class Notification {
    private String status;
    private int notificationId;
    private Method messageType;
    private NotificationTemplate template;
    private User recipient;

    public Notification(String status, int notificationId, Method messageType, User recipient) {
        this.status = status;
        this.notificationId = notificationId;
        this.messageType = messageType;
        this.recipient = recipient;
    }

    public void createAndSend(NotificationTemplate template) {
        this.template = template;
        if (messageType != null && template != null) {
            NotificationController.addToQueue(this);
            status = "Pending";
        }
    }
} 