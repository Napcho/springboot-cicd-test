package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
//new changes
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "CI/CD Test Application is running!";
    }
}
