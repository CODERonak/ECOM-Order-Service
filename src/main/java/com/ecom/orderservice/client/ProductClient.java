package com.ecom.orderservice.client;

import com.ecom.orderservice.dto.ProductResponse;
import com.ecom.orderservice.dto.StockDeductRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductResponse getProductById(@PathVariable("id") String id);

    @PostMapping("/api/products/deduct-stock")
    Boolean deductStock(@RequestBody StockDeductRequest req);
}
