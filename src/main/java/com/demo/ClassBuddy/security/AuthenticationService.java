package com.demo.ClassBuddy.security;

import com.demo.ClassBuddy.exceptions.UserAlreadyExistsException;
import com.demo.ClassBuddy.exceptions.UserNotFoundException;
import com.demo.ClassBuddy.user.User;
import com.demo.ClassBuddy.user.UserRepository;
import com.demo.ClassBuddy.utilities.AuthenticationRequest;
import com.demo.ClassBuddy.utilities.AuthenticationResponse;
import com.demo.ClassBuddy.utilities.RegisterRequest;
import com.demo.ClassBuddy.utilities.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;

    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
    }

    public ResponseEntity<RegisterResponse> register(RegisterRequest registerRequest) {
        Optional<User> seekedUser = userRepository.findByEmail(registerRequest.email());
        if (seekedUser.isPresent()) {
            throw new UserAlreadyExistsException("The email address is taken");
        }
        User user = new User(registerRequest.email(), registerRequest.lastname(), registerRequest.username(), registerRequest.email(), passwordEncoder.encode(registerRequest.password()), registerRequest.dateOfBirth());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse("User successfully created", ZonedDateTime.now(), user.getActualUsername()));
    }

    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        UserDetails user = userDetailsService.loadUserByUsername(authenticationRequest.email());
        final String token = jwtTokenService.generateToken(user);
        return ResponseEntity.status(HttpStatus.OK).body(new AuthenticationResponse("User successfully logged in", ZonedDateTime.now(), token));
    }
}
