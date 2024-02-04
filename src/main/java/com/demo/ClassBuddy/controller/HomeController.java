package com.demo.ClassBuddy.controller;

import com.demo.ClassBuddy.classroom.ClassroomService;
import com.demo.ClassBuddy.utility.JoinClassRequest;
import com.demo.ClassBuddy.utility.NewClassRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/")
public class HomeController {
    ClassroomService classroomService;

    @Autowired
    public HomeController(ClassroomService classroomService){
        this.classroomService = classroomService;
    }

    @GetMapping("/home")
    public String hello() {
        return "Welcome to home page!";
    }

    @GetMapping("/test")
    public String test() {
        return "Welcome to test page!";
    }

    @PostMapping("/create-class")
    public void createClass(@RequestBody NewClassRequest newClassRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        classroomService.createClass(newClassRequest, authentication.getName());
    }
    @PostMapping("/join-class")
    public void joinClass(@RequestBody JoinClassRequest joinClassRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        classroomService.joinClass(authentication.getName(), joinClassRequest.code());
    }

    @GetMapping("/get-classes")
    public String getClasses(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return classroomService.getClasses(authentication.getName());
    }
}
