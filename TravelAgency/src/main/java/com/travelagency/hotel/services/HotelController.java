package com.travelagency.hotel.services;

import com.travelagency.hotel.models.Hotel;
import com.travelagency.hotel.storage.HotelStorageInterface;
import com.travelagency.common.strategy.SearchStrategy;
import com.travelagency.common.strategy.NameSearchStrategy;
import com.travelagency.common.strategy.LocationSearchStrategy;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class HotelController {
    private final HotelStorageInterface hotelStorage;
    private final Map<String, SearchStrategy<Hotel>> searchStrategies;
    
    public HotelController(
        HotelStorageInterface hotelStorage,
        NameSearchStrategy<Hotel> nameStrategy,
        LocationSearchStrategy<Hotel> locationStrategy
    ) {
        this.hotelStorage = hotelStorage;
        
        this.searchStrategies = new HashMap<>();
        searchStrategies.put("name", nameStrategy);
        searchStrategies.put("location", locationStrategy);
    }
    
    public Hotel searchHotelByName(String name) {
        List<Hotel> hotels = searchStrategies.get("name").search(getAllHotels(), name);
        return hotels.stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Hotel not found with name: " + name));
    }
    
    public List<Hotel> getAllHotels() {
        return hotelStorage.findAll();
    }

    public List<Hotel> searchHotelsByLocation(String location) {
        return searchStrategies.get("location").search(getAllHotels(), location);
    }
}