package com.ecom.orderservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.ecom.orderservice.model.enums.OrderStatus;

public record OrderResponse(
    UUID id,
    String userEmail,
    String productId,
    String productName,
    Integer quantity,
    BigDecimal pricePerUnit,
    BigDecimal totalPrice,
    OrderStatus status
) {}
