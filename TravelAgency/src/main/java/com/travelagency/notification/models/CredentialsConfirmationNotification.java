package com.travelagency.notification.models;

import org.springframework.stereotype.Component;

@Component
public class CredentialsConfirmationNotification extends NotificationTemplate {
    public CredentialsConfirmationNotification() {
        super("Your credentials have been successfully confirmed. Welcome aboard!", "CredentialsConfirmation");
    }
}
