package com.ecom.orderservice.dto;

import java.math.BigDecimal;

public record ProductResponse(
    String id,
    String name,
    String description,
    BigDecimal price,
    Integer stockQuantity,
    String category,
    String status,
    String brand) {
}
