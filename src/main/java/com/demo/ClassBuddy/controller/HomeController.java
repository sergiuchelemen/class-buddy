package com.demo.ClassBuddy.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for testing purposes.
 */
@RestController
@RequestMapping(path = "/")
@CrossOrigin("*")
public class HomeController {

    @GetMapping("/home")
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to home page!");
        return response;
    }

    @GetMapping("/")
    public Map<String, String> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to test page!");
        return response;
    }
}
