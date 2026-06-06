package com.ecom.orderservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ecom.orderservice.dto.OrderRequest;
import com.ecom.orderservice.dto.OrderResponse;
import com.ecom.orderservice.model.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    /**
     * Converts an OrderRequest to an Order entity.
     * We explicitly tell MapStruct to ignore target properties that do not exist
     * on the request DTO to prevent compilation failures.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userEmail", ignore = true)
    @Mapping(target = "productName", ignore = true)
    @Mapping(target = "pricePerUnit", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toEntity(OrderRequest orderRequest);

    /**
     * Converts a database Order entity into a clean OrderResponse record.
     * Since fields match exactly by name and types map perfectly (UUID, String,
     * BigDecimal, Enum),
     * MapStruct handles this automatically without extra configuration.
     */
    OrderResponse toOrderResponse(Order order);

    /**
     * Maps a list of database Order entities into a list of OrderResponse records.
     */
    List<OrderResponse> toOrderResponseList(List<Order> orders);

}
