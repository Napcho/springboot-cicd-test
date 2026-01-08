package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@RestController
public class HelloController {

    // FIXED: Removed hardcoded credentials
    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    // FIXED: Added simple authentication check
    @GetMapping("/hello")
    public String hello(@RequestParam(required = false) String name, @RequestHeader(value = "X-Auth-Token", required = false) String token) {
        if (token == null || !token.equals("securetoken")) {
            return "Unauthorized";
        }
        return "Hello " + name;
    }

    // FIXED: SQL Injection vulnerability
    @GetMapping("/user")
    public String getUser(@RequestParam String username, @RequestHeader(value = "X-Auth-Token", required = false) String token) {
        if (token == null || !token.equals("securetoken")) {
            return "Unauthorized";
        }
        try {
            Connection connection =
                    DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            statement.setString(1, username);
            statement.executeQuery();
            return "User fetched successfully";
        } catch (Exception e) {
            // FIXED: Do not leak internal error messages
            return "Internal server error";
        }
    }

    // FIXED: Command Injection vulnerability
    @GetMapping("/run")
    public String runCommand(@RequestParam String cmd, @RequestHeader(value = "X-Auth-Token", required = false) String token) {
        if (token == null || !token.equals("securetoken")) {
            return "Unauthorized";
        }
        // Do not execute arbitrary commands
        return "Command execution not allowed";
    }

    // FIXED: Exposes environment variables (Sensitive info disclosure)
    @GetMapping("/env")
    public String exposeEnvironment(@RequestHeader(value = "X-Auth-Token", required = false) String token) {
        if (token == null || !token.equals("securetoken")) {
            return "Unauthorized";
        }
        return "Not allowed";
    }

    // FIXED: Dangerous admin operation without protection
    @DeleteMapping("/deleteAll")
    public String deleteAll(@RequestHeader(value = "X-Auth-Token", required = false) String token) {
        if (token == null || !token.equals("securetoken")) {
            return "Unauthorized";
        }
        return "Operation not allowed";
    }
}
