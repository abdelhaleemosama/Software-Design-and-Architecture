package com.travelagency.user.services;

import com.travelagency.user.domain.User;
import com.travelagency.user.dto.LoginRequest;
import com.travelagency.user.dto.RegisterRequest;
import com.travelagency.user.storage.UserStorageInterface;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.travelagency.notification.services.NotificationController;

import java.util.Random;
import com.travelagency.notification.models.Notification;
import com.travelagency.notification.models.Email;
import com.travelagency.notification.models.CredentialsConfirmationNotification;

@Service
public class UserController {
    private final UserStorageInterface userStorage;
    private final PasswordEncoder passwordEncoder;
    private final NotificationController notificationController;

    public UserController(UserStorageInterface userStorage, PasswordEncoder passwordEncoder, NotificationController notificationController) {
        this.userStorage = userStorage;
        this.passwordEncoder = passwordEncoder;
        this.notificationController = notificationController;
    }
    
    public void registerUser(RegisterRequest request) {
        if (userStorage.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userStorage.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        userStorage.save(user);
        
        // Create and send registration notification
        Notification notification = new Notification(
            "unsent",
            new Random().nextInt(),
            new Email(),
            user
        );
        notification.createAndSend(new CredentialsConfirmationNotification());
    }

    public void login(LoginRequest request) {
        User user = userStorage.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
    }
}