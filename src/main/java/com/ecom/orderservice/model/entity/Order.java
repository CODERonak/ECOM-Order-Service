package com.ecom.orderservice.model.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.ecom.orderservice.model.enums.OrderStatus;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String productId;

    private String productName;

    private Integer quantity;

    @Column(precision = 19, scale = 2)
    private BigDecimal pricePerUnit;

    @Column(precision = 19, scale = 2)
    private BigDecimal totalPrice;

    private OrderStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        updatedAt = Instant.now();

        if (status == null)
            status = OrderStatus.PENDING;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}
