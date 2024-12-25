package com.travelagency.user.storage;

import com.travelagency.common.storage.Storage;
import com.travelagency.hotel.models.Booking;
import com.travelagency.user.domain.User;
import java.util.Optional;

public interface UserStorageInterface extends Storage<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void addBookingToUser(String username, Booking booking);
} 