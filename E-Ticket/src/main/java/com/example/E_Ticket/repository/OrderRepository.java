package com.example.E_Ticket.repository;

import com.example.E_Ticket.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderByCode (String code);
    List<Order> findByStatusAndCreatedAtBetween(Order.Status status , Instant from, Instant to);
    Page<Order> findAllByStatus(String status, Pageable p);
}
