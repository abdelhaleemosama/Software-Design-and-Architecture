package com.travelagency.notification.models;

import org.springframework.stereotype.Component;

@Component
public class BookingConfirmationNotification extends NotificationTemplate {
    public BookingConfirmationNotification() {
        super("Your booking is confirmed. Details are included in this notification.", "BookingConfirmation");
    }
}
