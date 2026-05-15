package com.demo.contactapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "${cors.allowed-origins}")
public class RootController {

    @Value("${spring.application.name:java-contact-api}")
    private String appName;

    @Value("${app.env:development}")
    private String appEnv;

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new LinkedHashMap<>();
        
        response.put("message", "Welcome to Java Contact API");
        response.put("app", appName);
        response.put("version", "1.0.0");
        response.put("environment", appEnv);
        response.put("language", "Java");
        response.put("framework", "Spring Boot 3.2");
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // Available endpoints
        Map<String, Object> endpoints = new LinkedHashMap<>();
        endpoints.put("health", "GET /health - Health check");
        endpoints.put("contacts", "GET /contacts - Get all contacts");
        endpoints.put("create_contact", "POST /contacts - Create a new contact");
        endpoints.put("get_contact", "GET /contacts/{id} - Get contact by ID");
        
        response.put("endpoints", endpoints);
        
        // API documentation
        Map<String, Object> docs = new LinkedHashMap<>();
        docs.put("description", "Simple Spring Boot Contact API for managing contacts");
        docs.put("database", "MySQL with JPA");
        docs.put("cache", "Redis");
        docs.put("cors_enabled", true);
        
        response.put("documentation", docs);
        
        return ResponseEntity.ok(response);
    }
}