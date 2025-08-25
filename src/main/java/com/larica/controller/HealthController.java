package com.larica.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok().body(Map.of(
            "status", "OK",
            "timestamp", new Date(),
            "service", "Larica API",
            "version", "1.0.0"
        ));
    }
}