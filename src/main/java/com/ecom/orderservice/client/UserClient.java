package com.ecom.orderservice.client;

import com.ecom.orderservice.dto.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/auth/info/{email}")
    UserInfoResponse getUserInfo(@PathVariable("email") String email);

    @PostMapping("/api/auth/validate")
    Boolean validateToken(@RequestParam("token") String token);

    @GetMapping("/api/auth/email")
    String extractEmailFromToken(@RequestParam("token") String token);
}
