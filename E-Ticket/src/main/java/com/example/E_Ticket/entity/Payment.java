package com.example.E_Ticket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments",
        indexes = @Index(name = "idx_payments_order", columnList = "order_id"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /** MOMO|BANK|COD (mock) */
    @Column(nullable = false, length = 20)
    private String provider;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    /** INIT|SUCCESS|FAILED|REFUNDED */
    @Column(nullable = false, length = 20)
    private String status;

    /** ma giao dich cong thanh toan */
    @Column(length = 64)
    private String txnRef; /** ma giao dich*/
    private Instant paidAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
