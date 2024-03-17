package com.demo.ClassBuddy.service;

import com.demo.ClassBuddy.exception.UserAlreadyExistsException;
import com.demo.ClassBuddy.exception.UserNotFoundException;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.repository.UserRepository;
import com.demo.ClassBuddy.utility.AuthenticationRequest;
import com.demo.ClassBuddy.utility.LoginResponse;
import com.demo.ClassBuddy.utility.RefreshTokenResponse;
import com.demo.ClassBuddy.utility.RegisterResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
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
@Transactional
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;


    public RegisterResponse register(User user) {
        Optional<User> seekedUser = userRepository.findByEmail(user.getEmail());
        seekedUser.ifPresent(u -> {
            throw new UserAlreadyExistsException("User already exists.");
        });

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return new RegisterResponse("User successfully created", ZonedDateTime.now(), user.getActualUsername());
    }

    public LoginResponse authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.email(), authenticationRequest.password())
            );
            UserDetails user = userDetailsService.loadUserByUsername(authenticationRequest.email());
            String accessToken = jwtTokenService.generateAccessToken(user);
            String refreshToken = jwtTokenService.generateRefreshToken(user);
            return new LoginResponse(
                    "User successfully logged in",
                    ZonedDateTime.now(),
                    accessToken,
                    refreshToken
            );
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException("Invalid password");
        }
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String refreshToken = authHeader.substring(7);
        String userEmail = jwtTokenService.extractEmail(refreshToken);
        if (userEmail != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            String accessToken = jwtTokenService.generateAccessToken(userDetails);
            new ObjectMapper().writeValue(response.getOutputStream(), new RefreshTokenResponse(accessToken));
        }
    }
}
