package com.demo.ClassBuddy.service;

import com.demo.ClassBuddy.dto.UserDTO;
import com.demo.ClassBuddy.dto.UserDTOMapper;
import com.demo.ClassBuddy.exception.UserAlreadyExistsException;
import com.demo.ClassBuddy.exception.UserNotFoundException;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.repository.UserRepository;
import com.demo.ClassBuddy.utility.AuthenticationRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserDetailsService userDetailsService;

    @Mock
    JwtTokenService jwtTokenService;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserDTOMapper userDTOMapper;

    @InjectMocks
    AuthenticationService authenticationService;

    User user;
    UserDTO userDTO;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .username("john doe")
                .email("john@email.com")
                .password("1234")
                .dateOfBirth(LocalDate.of(1980, 9, 9))
                .build();

        userDTO = UserDTO.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .email("john@email.com")
                .build();
    }

    @Test
    public void testRegister() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(String.class))).thenReturn("encoded-password");
        when(userDTOMapper.apply(user)).thenReturn(userDTO);

        UserDTO result = authenticationService.register(user);

        assertEquals(result, userDTO);
        assertEquals("encoded-password", user.getPassword());
    }

    @Test
    public void testRegisterWithExistingUser() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> authenticationService.register(user));

        assertEquals("User already exists.", exception.getMessage());
    }

    @Test
    public void testAuthenticate() {
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
        when(jwtTokenService.generateAccessToken(user)).thenReturn("mockAccessToken");
        when(jwtTokenService.generateRefreshToken(user)).thenReturn("mockRefreshToken");
        when(authenticationManager.authenticate(any())).thenReturn(any());

        var authenticationRequest = new AuthenticationRequest(user.getEmail(), user.getPassword());
        Map<String, String> result = authenticationService.authenticate(authenticationRequest);

        assertEquals("mockAccessToken", result.get("accessToken"));
        assertEquals("mockRefreshToken", result.get("refreshToken"));
    }

    @Test
    public void testAuthenticateWithWrongEmail() {
        when(authenticationManager.authenticate(any())).thenReturn(any());
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenThrow(new UserNotFoundException("Invalid email."));

        var authenticationRequest = new AuthenticationRequest(user.getEmail(), user.getPassword());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> authenticationService.authenticate(authenticationRequest));

        assertEquals("Invalid email.", exception.getMessage());
    }

    @Test
    public void testAuthenticateWithWrongPassword() {
        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

        var authenticationRequest = new AuthenticationRequest(user.getEmail(), user.getPassword());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> authenticationService.authenticate(authenticationRequest));

        assertEquals("Invalid password.", exception.getMessage());
    }

    @Test
    public void testRefreshToken() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer mockToken");
        when(jwtTokenService.extractEmail(any(String.class))).thenReturn(user.getUsername());
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
        when(jwtTokenService.generateAccessToken(user)).thenReturn("mockAccessToken");

        String accessToken = authenticationService.refreshToken(request);

        assertEquals("mockAccessToken", accessToken);
    }
}