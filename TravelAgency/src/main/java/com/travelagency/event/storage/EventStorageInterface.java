package com.travelagency.event.storage;

import com.travelagency.common.storage.Storage;
import com.travelagency.event.models.Event;
import java.util.List;
import java.util.Optional;

public interface EventStorageInterface extends Storage<Event, String> {
    List<Event> findByLocation(String location);
    Optional<Event> findByName(String name);
} 