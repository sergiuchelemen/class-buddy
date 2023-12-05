package com.demo.ClassBuddy.security;

import com.demo.ClassBuddy.user.User;
import com.demo.ClassBuddy.user.UserRepository;
import com.demo.ClassBuddy.utilities.AuthenticationRequest;
import com.demo.ClassBuddy.utilities.AuthenticationResponse;
import com.demo.ClassBuddy.utilities.RegisterRequest;
import com.demo.ClassBuddy.utilities.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/")
public class SecurityController {
    private final AuthenticationService authenticationService;

    @Autowired
    public SecurityController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }
    @PostMapping(path = "/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request
    ){
        return authenticationService.register(request);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return authenticationService.authenticate(request);
    }

    @GetMapping(path = "/register")
    public String register(){
        return "Welcome to register page!";
    }

    @GetMapping(path = "/login")
    public String login(){
        return "Welcome to login page!";
    }

    @GetMapping(path = "/")
    public String root(){
        return "Welcome to root page!";
    }
}
