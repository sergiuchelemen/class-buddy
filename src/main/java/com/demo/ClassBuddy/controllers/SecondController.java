package com.demo.ClassBuddy.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/home")
public class SecondController {

    @GetMapping
    public String hello(){
        return "Welcome to home page!";
    }

}
