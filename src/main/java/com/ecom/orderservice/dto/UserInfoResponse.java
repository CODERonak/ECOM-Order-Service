package com.ecom.orderservice.dto;

public record UserInfoResponse(
    String email,
    String fullName,
    String phone) {
}
