package com.travelagency.event.storage;

import com.travelagency.event.models.Event;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryEventStorage implements EventStorageInterface {
    private final Map<String, Event> events = new HashMap<>();
    
    @Override
    public Event save(Event event) {
        events.put(event.getName(), event);
        return event;
    }
    
    @Override
    public List<Event> findAll() {
        return new ArrayList<>(events.values());
    }
    
    @Override
    public Optional<Event> findById(String id) {
        return Optional.ofNullable(events.get(id));
    }
    
    @Override
    public Optional<Event> findByName(String name) {
        return Optional.ofNullable(events.get(name));
    }
    
    @Override
    public List<Event> findByLocation(String location) {
        return events.values().stream()
            .filter(event -> event.getLocation().equalsIgnoreCase(location))
            .collect(Collectors.toList());
    }
    
    @Override
    public void delete(String id) {
        events.remove(id);
    }
    
    @Override
    public boolean exists(String id) {
        return events.containsKey(id);
    }
} 