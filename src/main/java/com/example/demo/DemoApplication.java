package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@SpringBootApplication
@RestController
public class DemoApplication {

    // ❌ Hardcoded credentials (Sensitive data exposure)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root123";

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // ❌ SQL Injection vulnerability
    @GetMapping("/user")
    public String getUser(@RequestParam String username) {
        String result = "";
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();

            // Vulnerable query (string concatenation)
            String query = "SELECT * FROM users WHERE username = '" + username + "'";
            stmt.executeQuery(query);

            result = "User fetched successfully";
        } catch (Exception e) {
            // ❌ Information leakage
            return e.getMessage();
        }
        return result;
    }

    // ❌ No authentication / authorization
    @DeleteMapping("/deleteAllUsers")
    public String deleteAllUsers() {
        return "All users deleted!";
    }

    // ❌ Command Injection vulnerability
    @GetMapping("/run")
    public String runCommand(@RequestParam String cmd) throws Exception {
        Runtime.getRuntime().exec(cmd);
        return "Command executed";
    }

    // ❌ Debug endpoint exposing internal info
    @GetMapping("/env")
    public String exposeEnv() {
        return System.getenv().toString();
    }
}
