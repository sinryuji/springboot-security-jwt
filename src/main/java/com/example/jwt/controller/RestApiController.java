package com.example.jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    @GetMapping("/")
    public String home() {
        return "<h1>home</h1>";
    }

    @PostMapping("/")
    public String token() {
        return "<h1>token</h1>";
    }
}
