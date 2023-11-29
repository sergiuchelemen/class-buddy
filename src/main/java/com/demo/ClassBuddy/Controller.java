package com.demo.ClassBuddy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class Controller {

    @GetMapping
    public String hello(){
        return "Hello World!";
    }
}
