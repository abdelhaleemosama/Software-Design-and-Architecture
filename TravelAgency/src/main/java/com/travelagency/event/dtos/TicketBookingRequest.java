package com.travelagency.event.dtos;

import lombok.Data;

@Data
public class TicketBookingRequest {
    private String username;
    private int numberOfTickets;
} 