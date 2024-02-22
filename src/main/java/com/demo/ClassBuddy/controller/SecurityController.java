package com.demo.ClassBuddy.controller;


import com.demo.ClassBuddy.security.AuthenticationService;
import com.demo.ClassBuddy.utility.AuthenticationRequest;
import com.demo.ClassBuddy.utility.AuthenticationResponse;
import com.demo.ClassBuddy.utility.RegisterRequest;
import com.demo.ClassBuddy.utility.RegisterResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "/")
@CrossOrigin("*")
public class SecurityController {
    private final AuthenticationService authenticationService;

    @Autowired
    public SecurityController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return authenticationService.register(request);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return authenticationService.authenticate(request);
    }

    @PostMapping(path = "/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }
}
