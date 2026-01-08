package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@SpringBootApplication
@RestController
public class DemoApplication {

    // Credentials should be loaded securely (e.g., environment variables)
    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // SQL Injection FIX: Use PreparedStatement
    @GetMapping("/user")
    public String getUser(@RequestParam String username) {
        String result = "";
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, username);
            stmt.executeQuery();
            result = "User fetched successfully";
        } catch (Exception e) {
            // Information leakage FIX: Do not expose internal errors
            return "An error occurred.";
        }
        return result;
    }

    // Authentication/Authorization FIX: Restrict access (dummy check for demonstration)
    @DeleteMapping("/deleteAllUsers")
    public String deleteAllUsers(@RequestHeader(value = "X-ADMIN", required = false) String adminHeader) {
        if (!"true".equals(adminHeader)) {
            return "Unauthorized";
        }
        return "All users deleted!";
    }

    // Command Injection FIX: Remove dangerous endpoint
    // @GetMapping("/run")
    // public String runCommand(@RequestParam String cmd) throws Exception {
    //     Runtime.getRuntime().exec(cmd);
    //     return "Command executed";
    // }

    // Debug endpoint FIX: Remove or restrict
    // @GetMapping("/env")
    // public String exposeEnv() {
    //     return System.getenv().toString();
    // }
}
