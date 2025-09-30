package com.example.E_Ticket.service;

import java.math.BigDecimal;

public interface PricingService {
    BigDecimal getFinalPrice(Long eventId, Long ticketTypeId, Long seatZoneId);
}
