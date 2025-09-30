package com.example.E_Ticket.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name="inventory_config",
        uniqueConstraints = @UniqueConstraint(columnNames = "event_id"))
public class InventoryConfig {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name="event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private Integer holdTimeoutSec = 300;  // 5 phút

    @Column(nullable = false)
    private Boolean allowOverbook = false;
}