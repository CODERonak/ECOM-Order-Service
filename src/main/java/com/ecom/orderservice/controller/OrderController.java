package com.ecom.orderservice.controller;

import com.ecom.orderservice.dto.OrderRequest;
import com.ecom.orderservice.dto.OrderResponse;
import com.ecom.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

/**
 * Exposes external REST API routing endpoints for managing business ordering
 * requests.
 * Secures routing parameters by wrapping client parameters inside security
 * Principal objects.
 * Integrates OpenAPI specifications mapping response statuses back to standard
 * documentation.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Service", description = "Endpoints for managing user orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Accepts structural inbound data requests to initiate processing rules for
     * orders.
     * Leverages Spring web validation parameters to process context data
     * structures.
     * Returns an explicit HTTP status code indicating a resource creation success.
     */
    @PostMapping
    @Operation(summary = "Place an order", description = "Deducts stock, and sends confirmation.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order placed successfully"),
            @ApiResponse(responseCode = "409", description = "Insufficient stock"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<OrderResponse> placeOrder(Principal principal, @RequestBody @Valid OrderRequest request) {
        var order = orderService.placeOrder(principal.getName(), request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    /**
     * Resolves collection payloads mapping back to specific verified accounts.
     * Resolves identification metadata internally from parsing the security Context
     * layer.
     * Emits a regular response payload wrapper containing complete lists of
     * entities.
     */
    @GetMapping("/my-orders")
    @Operation(summary = "Get my orders", description = "Retrieves all orders placed by the current user.")
    @ApiResponse(responseCode = "200", description = "List of orders returned")
    public ResponseEntity<List<OrderResponse>> getMyOrders(Principal principal) {
        return ResponseEntity.ok(orderService.getMyOrders(principal.getName()));
    }

    /**
     * Exposes query pathways for fetching individual records via primary key paths.
     * Protects database records by checking request validation credentials against
     * parameters.
     * Maps global routing exceptions seamlessly back onto configured controller
     * advice layers.
     */
    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID", description = "Fetches a specific order.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID orderId, Principal principal) {
        return ResponseEntity.ok(orderService.getOrderById(orderId, principal.getName()));
    }

    /**
     * Authorizes inbound mutation commands aimed toward updating individual
     * transaction records.
     * Evaluates access parameters to prevent credential hijacking across user
     * boundaries.
     * Dispatches cancellation notices across inter-service networks upon successful
     * completion.
     */
    @PostMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel an order", description = "Cancels a confirmed order.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "400", description = "Order already cancelled")
    })
    public ResponseEntity<OrderResponse> cancelOrder(Principal principal, @PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId, principal.getName()));
    }
}