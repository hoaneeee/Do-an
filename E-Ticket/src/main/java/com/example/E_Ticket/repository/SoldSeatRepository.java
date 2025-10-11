package com.example.E_Ticket.repository;

import com.example.E_Ticket.entity.SoldSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SoldSeatRepository extends JpaRepository<SoldSeat, Long> {
    boolean existsByEvent_IdAndZone_IdAndSeatNo(Long eventId, Long zoneId, Integer seatNo);
    List<SoldSeat> findByEvent_Id(Long eventId);
    Optional<SoldSeat> findByEvent_IdAndZone_IdAndSeatNo(Long eventId, Long zoneId, Integer seatNo);
}
