package com.demo.ClassBuddy.controller;


import com.demo.ClassBuddy.dto.UserDTO;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.service.AuthenticationService;
import com.demo.ClassBuddy.utility.AuthenticationRequest;
import com.demo.ClassBuddy.utility.LoginResponse;
import com.demo.ClassBuddy.utility.RefreshTokenResponse;
import com.demo.ClassBuddy.utility.RegisterResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * REST controller responsible for handling user registration, login, and JWT token refresh.
 */
@RestController
@RequestMapping(path = "/")
@AllArgsConstructor
@CrossOrigin("*")
public class SecurityController {

    private final AuthenticationService authenticationService;

    /**
     * Registers a new user in the system.
     *
     * @param user The details of the user to register.
     * @return A {@link RegisterResponse}.
     */
    @PostMapping(path = "/register")
    public RegisterResponse register(@RequestBody User user) {
        UserDTO registeredUser = authenticationService.register(user);
        return new RegisterResponse("User successfully created.", Timestamp.valueOf(LocalDateTime.now()), registeredUser);
    }

    /**
     * Authenticates a user and generates JWT access and refresh tokens.
     *
     * @param request The {@link AuthenticationRequest}.
     * @return A {@link LoginResponse}.
     */
    @PostMapping(path = "/login")
    public LoginResponse authenticate(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    /**
     * Refreshes an existing access token using a valid refresh token.
     *
     * @param request The HTTP request containing a valid refresh token.
     * @return A {@link RefreshTokenResponse}.
     */

    @PostMapping(path = "/refresh-token")
    public RefreshTokenResponse refreshToken(HttpServletRequest request) {
        String refreshToken = authenticationService.refreshToken(request);
        return new RefreshTokenResponse(refreshToken, Timestamp.valueOf(LocalDateTime.now()));
    }
}
