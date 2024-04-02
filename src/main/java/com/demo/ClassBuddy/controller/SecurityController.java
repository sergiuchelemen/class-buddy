package com.demo.ClassBuddy.controller;


import com.demo.ClassBuddy.dto.UserDTO;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.service.AuthenticationService;
import com.demo.ClassBuddy.utility.AuthenticationRequest;
import com.demo.ClassBuddy.utility.LoginResponse;
import com.demo.ClassBuddy.utility.RefreshTokenResponse;
import com.demo.ClassBuddy.utility.RegisterResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Map;

@RestController
@RequestMapping(path = "/")
@AllArgsConstructor
@CrossOrigin("*")
public class SecurityController {
    private final AuthenticationService authenticationService;

    @PostMapping(path = "/register")
    public RegisterResponse register(@RequestBody User user) {
        UserDTO registeredUser = authenticationService.register(user);
        return new RegisterResponse("User successfully created", ZonedDateTime.now(), registeredUser);
    }

    @PostMapping(path = "/login")
    public LoginResponse authenticate(@RequestBody AuthenticationRequest request) {
        Map<String, String> tokens = authenticationService.authenticate(request);
        return new LoginResponse(
                "User successfully logged in.",
                ZonedDateTime.now(),
                tokens.get("accessToken"),
                tokens.get("refreshToken")
        );
    }

    @PostMapping(path = "/refresh-token")
    public RefreshTokenResponse refreshToken(HttpServletRequest request) {
        String refreshToken = authenticationService.refreshToken(request);
        return new RefreshTokenResponse(refreshToken, Timestamp.valueOf(LocalDateTime.now()));
    }
}
