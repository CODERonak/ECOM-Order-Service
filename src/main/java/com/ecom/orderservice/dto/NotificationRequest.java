package com.ecom.orderservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequest {
    private String toEmail;

    private String subject;

    private String message;

    private String type; 
}
