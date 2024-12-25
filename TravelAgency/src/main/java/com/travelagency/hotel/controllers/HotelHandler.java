package com.travelagency.hotel.controllers;

import com.travelagency.hotel.services.HotelController;
import com.travelagency.hotel.dtos.BookingRequest;
import com.travelagency.hotel.models.RoomType;
import com.travelagency.booking.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** endpoint for hotel-related activities **/
@RestController
@RequestMapping("/hotels")
public class HotelHandler {
    private final HotelController hotelController;
    private final BookingService bookingService;
    
    @Autowired
    public HotelHandler(HotelController hotelController, BookingService bookingService) {
        this.hotelController = hotelController;
        this.bookingService = bookingService;
    }
    
    @GetMapping("/search/name")
    public ResponseEntity<?> searchHotel(@RequestParam String name) {
        try {
            return ResponseEntity.ok(hotelController.searchHotelByName(name));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/location")
    public ResponseEntity<?> searchByLocation(@RequestParam String location) {
        return ResponseEntity.ok(hotelController.searchHotelsByLocation(location));
    }
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllHotels() {
        return ResponseEntity.ok(hotelController.getAllHotels());
    }
    
    @PostMapping("/{hotelName}/book")
    public ResponseEntity<?> bookRoom(
            @PathVariable String hotelName,
            @RequestParam RoomType roomType,
            @RequestBody BookingRequest request) {
        try {
            return ResponseEntity.ok(bookingService.bookHotelRoom(hotelName, roomType, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/user/{username}/bookings")
    public ResponseEntity<?> getUserBookings(@PathVariable String username) {
        try {
            return ResponseEntity.ok(bookingService.getUserHotelBookings(username));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 