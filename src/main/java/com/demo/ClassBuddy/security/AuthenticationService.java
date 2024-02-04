package com.demo.ClassBuddy.security;

import com.demo.ClassBuddy.exception.UserAlreadyExistsException;
import com.demo.ClassBuddy.exception.UserNotFoundException;
import com.demo.ClassBuddy.user.User;
import com.demo.ClassBuddy.user.UserRepository;
import com.demo.ClassBuddy.utility.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtTokenService jwtTokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<RegisterResponse> register(RegisterRequest registerRequest) {
        Optional<User> seekedUser = userRepository.findByEmail(registerRequest.email());
        if (seekedUser.isPresent()) {
            throw new UserAlreadyExistsException("The email address is taken");
        }
        User user = new User(registerRequest.firstname(), registerRequest.lastname(), registerRequest.username(), registerRequest.email(), passwordEncoder.encode(registerRequest.password()), registerRequest.dateOfBirth());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse("User successfully created", ZonedDateTime.now(), user.getActualUsername()));
    }

    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.email(), authenticationRequest.password())
            );
            UserDetails user = userDetailsService.loadUserByUsername(authenticationRequest.email());
            String accessToken = jwtTokenService.generateAccessToken(user);
            String refreshToken = jwtTokenService.generateRefreshToken(user);
            return ResponseEntity.status(HttpStatus.OK).body(new AuthenticationResponse(
                    "User successfully logged in",
                    ZonedDateTime.now(),
                    accessToken,
                    refreshToken)
            );
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException("Invalid password");
        }
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtTokenService.extractEmail(refreshToken);

        if (userEmail != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (jwtTokenService.isTokenValid(refreshToken, userDetails)) {
                String accessToken = jwtTokenService.generateAccessToken(userDetails);
                new ObjectMapper().writeValue(response.getOutputStream(), new RefreshTokenResponse(accessToken));
            }
        }
    }
}
