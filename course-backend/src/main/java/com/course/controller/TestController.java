package com.course.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public ResponseEntity<Map<String, Object>> hello() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Spring Boot应用运行成功！");
        response.put("timestamp", LocalDateTime.now());
        response.put("path", "/api/test/hello");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "course-backend");
        response.put("timestamp", System.currentTimeMillis());
        response.put("database", "connected");
        return ResponseEntity.ok(response);
    }
}