package com.example.E_Ticket.service.impl;

import com.example.E_Ticket.entity.TicketType;
import com.example.E_Ticket.repository.TicketTypeRepository;
import com.example.E_Ticket.repository.ZonePriceRepository;
import com.example.E_Ticket.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {
    private final ZonePriceRepository zonePriceRepo;
    private final TicketTypeRepository ticketRepo;

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getFinalPrice(Long eventId, Long ticketTypeId, Long seatZoneId) {
        // Nếu chưa chọn zone ⇒ trả giá mặc định của ticket type
        if (seatZoneId == null) {
            return ticketRepo.findById(ticketTypeId)
                    .map(TicketType::getPrice)
                    .orElse(BigDecimal.ZERO);
        }

        return zonePriceRepo.findByEvent_IdAndTicketType_IdAndSeatZone_Id(eventId, ticketTypeId, seatZoneId)
                .map(zp -> zp.getPrice())
                .orElse(
                        ticketRepo.findById(ticketTypeId)
                                .map(TicketType::getPrice)
                                .orElse(BigDecimal.ZERO)
                );
    }
}
