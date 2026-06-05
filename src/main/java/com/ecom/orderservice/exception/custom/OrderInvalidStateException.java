package com.ecom.orderservice.exception.custom;

public class OrderInvalidStateException extends RuntimeException {
    public OrderInvalidStateException(String message) {
        super(message);
    }
}
