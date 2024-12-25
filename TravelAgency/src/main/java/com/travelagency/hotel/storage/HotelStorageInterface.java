package com.travelagency.hotel.storage;

import com.travelagency.common.storage.Storage;
import com.travelagency.hotel.models.Hotel;
import java.util.List;
import java.util.Optional;

public interface HotelStorageInterface extends Storage<Hotel, String> {
    List<Hotel> findByLocation(String location);
    Optional<Hotel> findByName(String name);
    void saveAll(List<Hotel> hotels);
} 