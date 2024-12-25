package com.travelagency.notification.services;

import com.travelagency.notification.models.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NotificationController {
    private static Queue<Notification> notificationQueue = new LinkedList<>();
    private static Queue<Notification> sentNotificationList = new LinkedList<>();
    private static Queue<Notification> failedNotificationList = new LinkedList<>();
    private static Map<String, Integer> recipientNotificationCount = new HashMap<>();
    private static Map<String, Integer> templateUsageCount = new HashMap<>();

    public static void addToQueue(Notification notification) {
        notificationQueue.add(notification);
    }

    public static Queue<Notification> getNotifications(int userId) {
        Queue<Notification> userNotificationQueue = new LinkedList<>();
        for (Notification notification : sentNotificationList) {
            if (notification.getRecipient().getUserId() == userId) {
                userNotificationQueue.add(notification);
            }
        }
        return userNotificationQueue;
    }

    public static void processQueue() {
        while (!notificationQueue.isEmpty()) {
            Notification notification = notificationQueue.poll();
            try {
                notification.setStatus("Sent");
                sentNotificationList.add(notification);
                
                // Update recipient count
                String recipientId = notification.getMessageType() instanceof Email ? 
                    notification.getRecipient().getEmail() : 
                    notification.getRecipient().getPhone();
                recipientNotificationCount.merge(recipientId, 1, Integer::sum);
                
                // Update template usage count
                String templateType = notification.getTemplate().getType();
                templateUsageCount.merge(templateType, 1, Integer::sum);
                
            } catch (Exception e) {
                notification.setStatus("Failed");
                failedNotificationList.add(notification);
            }
        }
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("sentNotifications", sentNotificationList.size());
        stats.put("failedNotifications", failedNotificationList.size());
        
        // Most notified recipient
        String mostNotifiedRecipient = recipientNotificationCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("None");
        stats.put("mostNotifiedRecipient", mostNotifiedRecipient);
        stats.put("mostNotifiedRecipientCount", 
            recipientNotificationCount.getOrDefault(mostNotifiedRecipient, 0));
            
        // Most used template
        String mostUsedTemplate = templateUsageCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("None");
        stats.put("mostUsedTemplate", mostUsedTemplate);
        stats.put("mostUsedTemplateCount", 
            templateUsageCount.getOrDefault(mostUsedTemplate, 0));

        return stats;
    }
} 