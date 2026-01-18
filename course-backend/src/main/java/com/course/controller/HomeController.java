package com.course.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Course Selection System");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "欢迎使用课程选课系统API");
        response.put("endpoints", Map.of(
                "test", "/api/test/hello",
                "health", "/api/test/health",
                "register", "POST /api/auth/register",
                "login", "POST /api/auth/login",
                "current_user", "GET /api/auth/me",
                "validate_token", "POST /api/auth/validate"
        ));
        return ResponseEntity.ok(response);
    }
}