package com.travelagency.common.strategy;

import com.travelagency.event.models.Event;
import com.travelagency.hotel.models.Hotel;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationSearchStrategy<T> implements SearchStrategy<T> {
    @Override
    public List<T> search(List<T> items, String searchTerm) {
        return items.stream()
            .filter(item -> {
                if (item instanceof Hotel) {
                    return ((Hotel) item).getLocation().equalsIgnoreCase(searchTerm);
                } else if (item instanceof Event) {
                    return ((Event) item).getLocation().equalsIgnoreCase(searchTerm);
                }
                return false;
            })
            .collect(Collectors.toList());
    }
} 