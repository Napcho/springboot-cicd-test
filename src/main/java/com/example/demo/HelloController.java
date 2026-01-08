package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@RestController
public class HelloController {

    // ❌ Hardcoded database credentials (Sensitive Data Exposure)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password123";

    // ❌ No authentication / authorization
    @GetMapping("/hello")
    public String hello(@RequestParam(required = false) String name) {
        return "Hello " + name;
    }

    // ❌ SQL Injection vulnerability
    @GetMapping("/user")
    public String getUser(@RequestParam String username) {
        try {
            Connection connection =
                    DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement statement = connection.createStatement();

            // Vulnerable query
            String query = "SELECT * FROM users WHERE username = '" + username + "'";
            statement.executeQuery(query);

            return "User fetched successfully";
        } catch (Exception e) {
            // ❌ Information leakage
            return e.getMessage();
        }
    }

    // ❌ Command Injection vulnerability
    @GetMapping("/run")
    public String runCommand(@RequestParam String cmd) throws Exception {
        Runtime.getRuntime().exec(cmd);
        return "Command executed";
    }

    // ❌ Exposes environment variables (Sensitive info disclosure)
    @GetMapping("/env")
    public String exposeEnvironment() {
        return System.getenv().toString();
    }

    // ❌ Dangerous admin operation without protection
    @DeleteMapping("/deleteAll")
    public String deleteAll() {
        return "All data deleted!";
    }
}
