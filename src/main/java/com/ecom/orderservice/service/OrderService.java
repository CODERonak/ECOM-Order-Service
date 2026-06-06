package com.ecom.orderservice.service;

import org.springframework.stereotype.Service;

import com.ecom.orderservice.client.*;
import com.ecom.orderservice.dto.*;
import com.ecom.orderservice.exception.custom.*;
import com.ecom.orderservice.mapper.OrderMapper;
import com.ecom.orderservice.model.entity.Order;
import com.ecom.orderservice.model.enums.OrderStatus;
import com.ecom.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Service class handling core order processing logic.
 * Orchestrates stock deduction, data persistence, and notifications.
 * Connects with external microservice OpenFeign data clients.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final NotificationClient notificationClient;
    private final OrderMapper orderMapper;

    /**
     * Executes the orchestration flow required to place a new customer order.
     * Validates pricing schema patterns, reserves stock quantities, and triggers
     * notifications.
     * Throws InsufficientStockException if requested stock exceeds availability.
     */
    public OrderResponse placeOrder(String email, OrderRequest orderRequest) {

        UserInfoResponse user = userClient.getUserInfo(email);

        ProductResponse product = productClient.getProductById(orderRequest.getProductId());

        Boolean stockOk = productClient.deductStock(
                new StockDeductRequest(orderRequest.getProductId(), orderRequest.getQuantity()));
        if (!Boolean.TRUE.equals(stockOk)) {
            throw new InsufficientStockException("Insufficient stock for: " + product.name());
        }

        BigDecimal total = product.price()
                .multiply(BigDecimal.valueOf(orderRequest.getQuantity()));

        Order order = orderMapper.toEntity(orderRequest);

        order.setUserEmail(user.email());
        order.setProductName(product.name());
        order.setPricePerUnit(product.price());
        order.setTotalPrice(total);
        order.setStatus(OrderStatus.CONFIRMED);

        Order saved = orderRepository.save(order);

        String msg = orderConfirmationEmail(user, email, product, orderRequest, total, saved.getId());

        notificationClient.sendNotification(new NotificationRequest(
                user.email(), // toEmail
                email, // fromUser (The context)
                "Order Confirmed #" + saved.getId(),
                msg,
                "ORDER_CONFIRMED"));

        return orderMapper.toOrderResponse(saved);
    }

    /**
     * Fetches all registered system orders associated with a specific user profile.
     * Filters the relational database records matching the provided user email
     * string.
     * Returns an empty collection dataset if no entries exist.
     */
    public List<OrderResponse> getMyOrders(String email) {
        List<Order> orders = orderRepository.findByUserEmail(email);
        return orderMapper.toOrderResponseList(orders);
    }

    /**
     * Retrieves a targeted database order matching the target entity key
     * identifier.
     * Validates resource domain authorization criteria using security context
     * credentials.
     * Throws OrderNotFoundException or OrderAccessDeniedException depending on
     * verification errors.
     */
    public OrderResponse getOrderById(UUID orderId, String email) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        if (!order.getUserEmail().equals(email)) {
            throw new OrderAccessDeniedException("Access denied — not your order");
        }

        return orderMapper.toOrderResponse(order);
    }

    /**
     * Executes a cancellation workflow sequence against a targeted database order
     * entity.
     * Validates current processing status flags before mutating state conditions.
     * Dispatches transactional messaging elements out to external notification
     * services.
     */
    public OrderResponse cancelOrder(UUID orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        if (!order.getUserEmail().equals(email)) {
            throw new OrderAccessDeniedException("Access denied — not your order");
        }
        if (OrderStatus.CANCELLED.equals(order.getStatus())) {
            throw new OrderInvalidStateException("Order already cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);

        String msg = orderCancellationEmail(saved, orderId);

        notificationClient.sendNotification(new NotificationRequest(
                saved.getUserEmail(),
                email,
                "Order Cancelled #" + orderId,
                msg,
                "ORDER_CANCELLED"));

        return orderMapper.toOrderResponse(saved);
    }

    /**
     * Generates structured text message layouts utilizing Java text block features.
     * Evaluates fallback naming priorities based on missing profile information
     * flags.
     * Formats localized pricing structures to handle trailing monetary decimal
     * placements.
     */
    private String orderConfirmationEmail(UserInfoResponse user, String email,
            ProductResponse product, OrderRequest req,
            BigDecimal total, Object orderId) {
        String recipientName = (user.fullName() != null && !user.fullName().isBlank())
                ? user.fullName()
                : email;

        return String.format("""
                Hi %s,

                Thank you for your order! We are pleased to confirm that your transaction was successful.

                ORDER DETAILS:

                Order ID:      #%s
                Product Name:  %s
                Quantity:      %d units
                Total Amount:  $%.2f

                We are currently preparing your items for shipment. You will receive another
                notification with a tracking link as soon as your package leaves our warehouse.

                If you have any questions regarding your purchase, please reply directly to this email.

                Best regards,
                The Spring CO.
                """,
                recipientName,
                orderId.toString(),
                product.name(),
                req.getQuantity(),
                total);
    }

    /**
     * Generates a structured email layout for order cancellations.
     * Utilizes Java text blocks to maintain readable paragraphs and clear visual
     * hierarchy.
     * Standardizes dynamic text padding for transactional audit logs.
     */
    private String orderCancellationEmail(Order order, UUID orderId) {
        return String.format("""
                Hi,

                This email confirms that your order has been successfully cancelled as requested.
                A refund (if applicable) has been initiated to your original payment method.


                CANCELLED ORDER DETAILS:

                Order ID:      #%s
                Product Name:  %s
                Quantity:      %d units
                Refund Amount: $%.2f


                If you did not request this cancellation, or if you believe this was done in error,
                please contact our customer support team immediately.

                We hope to serve you better in the future.

                Best regards,
                The Spring CO.
                """,
                orderId.toString(),
                order.getProductName(),
                order.getQuantity(),
                order.getTotalPrice());
    }
}