package com.demo.contactapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

    private final DataSource dataSource;
    private final RedisConnectionFactory redisConnectionFactory;

    @Value("${spring.application.name:java-contact-api}")
    private String appName;

    @Value("${app.env:development}")
    private String appEnv;

    public HealthController(DataSource dataSource, RedisConnectionFactory redisConnectionFactory) {
        this.dataSource = dataSource;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        boolean mysqlConnected = checkMySQL();
        boolean redisConnected = checkRedis();

        Map<String, Object> mysql = new LinkedHashMap<>();
        mysql.put("type", "MySQL");
        mysql.put("connected", mysqlConnected);

        Map<String, Object> redis = new LinkedHashMap<>();
        redis.put("type", "Redis");
        redis.put("connected", redisConnected);

        String status = (mysqlConnected && redisConnected) ? "ok" : "degraded";

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", status);
        response.put("app", appName);
        response.put("language", "Java");
        response.put("framework", "Spring Boot");
        response.put("database", mysql);
        response.put("cache", redis);

        return ResponseEntity.ok(response);
    }

    private boolean checkMySQL() {
        try (Connection conn = dataSource.getConnection()) {
            return conn.isValid(2); // 2 second timeout
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkRedis() {
        try {
            redisConnectionFactory.getConnection().ping();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
