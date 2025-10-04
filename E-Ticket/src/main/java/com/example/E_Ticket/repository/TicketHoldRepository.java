package com.example.E_Ticket.repository;

import com.example.E_Ticket.entity.TicketHold;
import com.example.E_Ticket.entity.TicketHold.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
@Repository
public interface TicketHoldRepository extends JpaRepository<TicketHold, Long> {
    List<TicketHold> findByTicketType_IdAndStatusAndExpiresAtAfter(Long ticketTypeId, Status status, Instant now);
    List<TicketHold> findByStatusAndExpiresAtBefore(Status status, Instant time);
    Optional<TicketHold> findByIdAndStatus(Long id, Status status);

}