package com.travelagency.hotel.storage;

import com.travelagency.hotel.models.Hotel;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryHotelStorage implements HotelStorageInterface {
    private final Map<String, Hotel> hotels = new HashMap<>();
    
    @Override
    public Hotel save(Hotel hotel) {
        hotels.put(hotel.getName(), hotel);
        return hotel;
    }
    
    @Override
    public void saveAll(List<Hotel> hotelList) {
        hotelList.forEach(hotel -> hotels.put(hotel.getName(), hotel));
    }
    
    @Override
    public List<Hotel> findAll() {
        return new ArrayList<>(hotels.values());
    }
    
    @Override
    public Optional<Hotel> findById(String id) {
        return Optional.ofNullable(hotels.get(id));
    }
    
    @Override
    public Optional<Hotel> findByName(String name) {
        return Optional.ofNullable(hotels.get(name));
    }
    
    @Override
    public List<Hotel> findByLocation(String location) {
        return hotels.values().stream()
            .filter(hotel -> hotel.getLocation().equalsIgnoreCase(location))
            .collect(Collectors.toList());
    }
    
    @Override
    public void delete(String id) {
        hotels.remove(id);
    }
    
    @Override
    public boolean exists(String id) {
        return hotels.containsKey(id);
    }
} 