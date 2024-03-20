package com.demo.ClassBuddy.controller;


import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.service.AuthenticationService;
import com.demo.ClassBuddy.utility.AuthenticationRequest;
import com.demo.ClassBuddy.utility.LoginResponse;
import com.demo.ClassBuddy.utility.RegisterResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "/")
@AllArgsConstructor
@CrossOrigin("*")
public class SecurityController {
    private final AuthenticationService authenticationService;

    @PostMapping(path = "/register")
    public RegisterResponse register(@RequestBody User user) {
        return authenticationService.register(user);
    }

    @PostMapping(path = "/login")
    public LoginResponse authenticate(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping(path = "/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response,
            @AuthenticationPrincipal User authenticatedUser
    ) throws IOException {
        authenticationService.refreshToken(request, response, authenticatedUser);
    }
}
