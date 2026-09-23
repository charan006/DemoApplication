package com.example.demo.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${app.version:1.0.0}")
    private String version;

    @GetMapping("/api/hello")
    public Map<String, Object> hello() {
        return Map.of(
            "message", "Hello from Spring Boot!",
            "version", version,
            "timestamp", Instant.now().toString()
        );
    }

    @GetMapping("/api/info")
    public Map<String, Object> info() {
        return Map.of(
            "application", "spring-boot-jenkins-demo",
            "version", version,
            "environment", System.getenv().getOrDefault("APP_ENV", "local")
        );
    }
}
