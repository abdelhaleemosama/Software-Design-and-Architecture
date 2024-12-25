package com.travelagency.user.storage;

import com.travelagency.user.domain.User;
import com.travelagency.hotel.models.Booking;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorageInterface {
    private final Map<String, User> users = new HashMap<>();
    
    @Override
    public User save(User user) {
        users.put(user.getUsername(), user);
        return user;
    }
    
    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
    
    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }
    
    @Override
    public void delete(String id) {
        users.remove(id);
    }
    
    @Override
    public boolean exists(String id) {
        return users.containsKey(id);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return users.values().stream()
            .anyMatch(user -> user.getEmail().equals(email));
    }
    
    @Override
    public void addBookingToUser(String username, Booking booking) {
        User user = users.get(username);
        if (user != null) {
            user.getBookings().add(booking);
            save(user);
        }
    }
} 