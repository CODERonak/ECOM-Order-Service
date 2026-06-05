package com.ecom.orderservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDeductRequest {

    private String productId; 

    private Integer quantity;
}