package com.ecom.orderservice.dto;
import lombok.Data;

@Data
public class OrderRequest {
    private String productId;  
    
    private Integer quantity;
}
