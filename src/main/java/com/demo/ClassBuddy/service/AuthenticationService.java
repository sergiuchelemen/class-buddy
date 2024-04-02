package com.demo.ClassBuddy.service;

import com.demo.ClassBuddy.dto.UserDTO;
import com.demo.ClassBuddy.dto.UserDTOMapper;
import com.demo.ClassBuddy.exception.UserAlreadyExistsException;
import com.demo.ClassBuddy.exception.UserNotFoundException;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.repository.UserRepository;
import com.demo.ClassBuddy.utility.AuthenticationRequest;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.HashMap;
import java.util.Map;
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

    private final UserDTOMapper userDTOMapper;

    public UserDTO register(User user) {
        Optional<User> seekedUser = userRepository.findByEmail(user.getEmail());
        seekedUser.ifPresent(u -> {
            throw new UserAlreadyExistsException("User already exists.");
        });

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userDTOMapper.apply(user);
    }

    public Map<String, String> authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.email(), authenticationRequest.password())
            );
            UserDetails user = userDetailsService.loadUserByUsername(authenticationRequest.email());
            String accessToken = jwtTokenService.generateAccessToken(user);
            String refreshToken = jwtTokenService.generateRefreshToken(user);
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            return tokens;
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException("Invalid password.");
        }
    }

    public String refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken = authHeader.substring(7);
        String userEmail = jwtTokenService.extractEmail(refreshToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        return jwtTokenService.generateAccessToken(userDetails);
    }
}
