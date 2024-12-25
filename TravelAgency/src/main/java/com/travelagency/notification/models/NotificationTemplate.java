package com.travelagency.notification.models;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class NotificationTemplate {
    private String type;
    private String content;

    public NotificationTemplate(String content, String type) {
        this.content = content;
        this.type = type;
    }
}

// Concrete templates
@Component
class ResetCredentialsNotification extends NotificationTemplate {
    public ResetCredentialsNotification() {
        super("Your credentials have been reset. Please use the provided link to update your password.", "ResetCredentials");
    }
}

@Component
class EventPromotionNotification extends NotificationTemplate {
    public EventPromotionNotification() {
        super("Check out our latest events and promotions! Don't miss out!", "EventPromotion");
    }
}

@Component
class BookingCancellationNotification extends NotificationTemplate {
    public BookingCancellationNotification() {
        super("We regret to inform you that your booking has been canceled. Please contact support for assistance.", "BookingCancellation");
    }
} 