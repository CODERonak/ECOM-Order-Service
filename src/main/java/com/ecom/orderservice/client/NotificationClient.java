package com.ecom.orderservice.client;

import com.ecom.orderservice.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/api/notifications/send")
    String sendNotification(@RequestBody NotificationRequest req);
}
