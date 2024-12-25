package com.travelagency.event.services;

import com.travelagency.event.models.Event;
import com.travelagency.event.storage.EventStorageInterface;
import com.travelagency.common.strategy.SearchStrategy;
import com.travelagency.common.strategy.NameSearchStrategy;
import com.travelagency.common.strategy.LocationSearchStrategy;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class EventController {
    private final EventStorageInterface eventStorage;
    private final Map<String, SearchStrategy<Event>> searchStrategies;

    public EventController(
        EventStorageInterface eventStorage,
        NameSearchStrategy<Event> nameStrategy,
        LocationSearchStrategy<Event> locationStrategy
    ) {
        this.eventStorage = eventStorage;
        
        // Use HashMap instead of Map.of()
        this.searchStrategies = new HashMap<>();
        searchStrategies.put("name", nameStrategy);
        searchStrategies.put("location", locationStrategy);
    }

    public List<Event> getAllEvents() {
        return eventStorage.findAll();
    }

    public List<Event> searchEventsByLocation(String location) {
        return searchStrategies.get("location").search(getAllEvents(), location);
    }

    public Event searchEventsByName(String name) {
        List<Event> events = searchStrategies.get("name").search(getAllEvents(), name);
        return events.stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Event not found with name: " + name));
    }
} 