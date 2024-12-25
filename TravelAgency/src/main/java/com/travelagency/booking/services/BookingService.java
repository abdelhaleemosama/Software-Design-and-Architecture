package com.travelagency.booking.services;

import com.travelagency.event.models.Event;
import com.travelagency.event.models.Ticket;
import com.travelagency.event.storage.EventStorageInterface;
import com.travelagency.hotel.dtos.BookingRequest;
import com.travelagency.hotel.models.*;
import com.travelagency.hotel.storage.HotelStorageInterface;
import com.travelagency.user.storage.UserStorageInterface;
import com.travelagency.common.models.Invoice;
import com.travelagency.notification.services.NotificationController;
import com.travelagency.notification.models.Notification;
import com.travelagency.notification.models.SMS;
import com.travelagency.notification.models.BookingConfirmationNotification;
import com.travelagency.notification.models.Email;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.Random;

@Service
public class BookingService {
    private final EventStorageInterface eventStorage;
    private final HotelStorageInterface hotelStorage;
    private final UserStorageInterface userStorage;
    private final NotificationController notificationController;

    public BookingService(
        EventStorageInterface eventStorage,
        HotelStorageInterface hotelStorage,
        UserStorageInterface userStorage,
        NotificationController notificationController
    ) {
        this.eventStorage = eventStorage;
        this.hotelStorage = hotelStorage;
        this.userStorage = userStorage;
        this.notificationController = notificationController;
    }

    public Ticket bookEventTicket(String username, String eventName, int numberOfTickets) {
        // Verify user exists
        var user = userStorage.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Get event and verify tickets are available
        Event event = eventStorage.findByName(eventName)
            .orElseThrow(() -> new RuntimeException("Event not found with name: " + eventName));
            
        if (event.getAvailableTickets() < numberOfTickets) {
            throw new RuntimeException("Not enough tickets available");
        }

        // Calculate total price
        double totalPrice = event.getTicketPrice() * numberOfTickets;

        // Create invoice
        Invoice eventInvoice = new Invoice();
        eventInvoice.setInvoiceId(System.currentTimeMillis());
        eventInvoice.setAmount(totalPrice);
        eventInvoice.setDetails("Ticket purchase for " + event.getName());
        eventInvoice.setStatus("PAID");

        // Create ticket
        Ticket ticket = new Ticket();
        ticket.setTicketId(UUID.randomUUID().toString());
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setInvoice(eventInvoice);

        // Update available tickets
        event.setAvailableTickets(event.getAvailableTickets() - numberOfTickets);
        eventStorage.save(event);

        // Add ticket to user's tickets
        user.getTickets().add(ticket);
        userStorage.save(user);

        // Send notification
        Notification notification = new Notification(
            "unsent",
            new Random().nextInt(),
            new Email(),
            user
        );
        notification.createAndSend(new BookingConfirmationNotification());

        return ticket;
    }

    public Booking bookHotelRoom(String hotelName, RoomType roomType, BookingRequest request) {
        // Validate user exists
        var user = userStorage.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found: " + request.getUsername()));

        // Validate dates
        LocalDate now = LocalDate.now();
        if (request.getCheckIn().isBefore(now)) {
            throw new RuntimeException("Check-in date cannot be in the past");
        }
        if (request.getCheckIn().isAfter(request.getCheckOut())) {
            throw new RuntimeException("Check-in date must be before check-out date");
        }

        // Find hotel and available room
        Hotel hotel = hotelStorage.findByName(hotelName)
            .orElseThrow(() -> new RuntimeException("Hotel not found with name: " + hotelName));
            
        Room availableRoom = hotel.getRooms().stream()
            .filter(r -> r.getRoomType().equals(roomType) && r.getIsAvailable())
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No available rooms of type " + roomType));
            
        // Calculate price
        long numberOfNights = java.time.temporal.ChronoUnit.DAYS.between(
            request.getCheckIn(), 
            request.getCheckOut()
        );
        double totalPrice = availableRoom.getPrice() * numberOfNights;
        
        // Create invoice
        Invoice hotelInvoice = new Invoice();
        hotelInvoice.setAmount(totalPrice);
        hotelInvoice.setDetails("Booking for " + hotelName + " - " + roomType);
        hotelInvoice.setStatus("PENDING");
        
        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(availableRoom);
        booking.setInvoice(hotelInvoice);
        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        
        // Update room status
        availableRoom.setIsAvailable(false);
        availableRoom.setCurrentBooking(booking);
        
        // Add booking to user's booking list
        userStorage.addBookingToUser(user.getUsername(), booking);
        
        // Save updated hotel state
        hotelStorage.save(hotel);
        
        // Send booking confirmation notification
        Notification notification = new Notification(
            "unsent",
            new Random().nextInt(),
            new SMS(),
            user
        );
        notification.createAndSend(new BookingConfirmationNotification());

        // Get and show event recommendations for the area
        List<Event> localEvents = eventStorage.findByLocation(hotel.getLocation());
        if (!localEvents.isEmpty()) {
            System.out.println("\nRecommended events near " + hotel.getLocation() + ":");
            localEvents.forEach(event -> 
                System.out.println("- " + event.getName() + 
                    " on " + event.getDate() + 
                    ", Tickets available: " + event.getAvailableTickets() +
                    ", Price: $" + event.getTicketPrice())
            );
        }

        return booking;
    }

    public List<Booking> getUserHotelBookings(String username) {
        var user = userStorage.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return user.getBookings();
    }

    public List<Ticket> getUserEventTickets(String username) {
        var user = userStorage.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return user.getTickets();
    }
} 