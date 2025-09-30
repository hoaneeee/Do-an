package com.example.E_Ticket.repository;

import com.example.E_Ticket.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface RefundRepository extends JpaRepository<Refund, Long> {
    List<Refund> findByOrder_Id(Long orderId);
}