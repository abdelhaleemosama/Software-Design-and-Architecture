package com.travelagency.event.controllers;

import com.travelagency.event.dtos.SearchRequest;
import com.travelagency.event.dtos.TicketBookingRequest;
import com.travelagency.event.services.EventController;
import com.travelagency.booking.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventHandler {
    private final EventController eventController;
    private final BookingService bookingService;

    @Autowired
    public EventHandler(EventController eventController, BookingService bookingService) {
        this.eventController = eventController;
        this.bookingService = bookingService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllEvents() {
        return ResponseEntity.ok(eventController.getAllEvents());
    }

    @GetMapping("/search/location")
    public ResponseEntity<?> searchByLocation(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(eventController.searchEventsByLocation(request.getSearchTerm()));
    }

    @GetMapping("/search/name")
    public ResponseEntity<?> searchByName(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(eventController.searchEventsByName(request.getSearchTerm()));
    }

    @PostMapping("/{eventName}/book")
    public ResponseEntity<?> bookTicket(
            @PathVariable String eventName,
            @RequestBody TicketBookingRequest request
    ) {
        try {
            return ResponseEntity.ok(bookingService.bookEventTicket(
                request.getUsername(), 
                eventName, 
                request.getNumberOfTickets()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{username}/tickets")
    public ResponseEntity<?> getUserTickets(@PathVariable String username) {
        try {
            return ResponseEntity.ok(bookingService.getUserEventTickets(username));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 