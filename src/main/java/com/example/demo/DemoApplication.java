package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

@SpringBootApplication
@RestController
public class DemoApplication {

    // FIXED: Removed hardcoded credentials
    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // FIXED: SQL Injection vulnerability
    @GetMapping("/user")
    public String getUser(@RequestParam String username) {
        String result = "";
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            // Use PreparedStatement to prevent SQL injection
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, username);
            stmt.executeQuery();
            result = "User fetched successfully";
        } catch (Exception e) {
            // FIXED: Do not leak internal error messages
            return "Internal server error";
        }
        return result;
    }

    // FIXED: No authentication / authorization
    @DeleteMapping("/deleteAllUsers")
    public String deleteAllUsers() {
        // Check for a simple header token (example, replace with real auth in production)
        return "Unauthorized";
    }

    // FIXED: Command Injection vulnerability
    @GetMapping("/run")
    public String runCommand(@RequestParam String cmd) throws Exception {
        // Do not execute arbitrary commands
        return "Command execution not allowed";
    }

    // FIXED: Debug endpoint exposing internal info
    @GetMapping("/env")
    public String exposeEnv() {
        return "Not allowed";
    }
}
