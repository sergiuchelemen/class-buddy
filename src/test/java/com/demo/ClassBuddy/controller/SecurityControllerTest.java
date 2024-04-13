package com.demo.ClassBuddy.controller;


import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.repository.UserRepository;
import com.demo.ClassBuddy.service.JwtTokenService;
import com.demo.ClassBuddy.utility.AuthenticationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Container
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.35");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .username("johndoe@email.com")
                .email("johndoe@email.com")
                .password("1234")
                .dateOfBirth(LocalDate.of(1980, 9, 9))
                .build();

        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @Order(1)
    public void testRegister() throws Exception {
        String request = objectMapper.writeValueAsString(user);

        userRepository.delete(user);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User successfully created."))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.newUser.id").value(1))
                .andExpect(jsonPath("$.newUser.firstname").value("John"))
                .andExpect(jsonPath("$.newUser.lastname").value("Doe"))
                .andExpect(jsonPath("$.newUser.email").value("johndoe@email.com"));
    }

    @Test
    @Order(2)
    public void testRegisterWithExistingUser() throws Exception {
        String request = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.httpStatus").value("CONFLICT"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("User already exists."));

    }

    @Test
    @Order(3)
    public void testAuthenticate() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("johndoe@email.com", "1234");
        String request = objectMapper.writeValueAsString(authenticationRequest);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @Order(4)
    public void testAuthenticateWithWrongEmail() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("wrongEmail", "1234");
        String request = objectMapper.writeValueAsString(authenticationRequest);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.httpStatus").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Invalid email."));
    }

    @Test
    @Order(5)
    public void testAuthenticateWithWrongPassword() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("johndoe@email.com", "wrongPassword");
        String request = objectMapper.writeValueAsString(authenticationRequest);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.httpStatus").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Invalid password."));
    }

    @Test
    @Order(6)
    public void testRefreshToken() throws Exception {
        mockMvc.perform(post("/refresh-token")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + generateToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @Order(7)
    public void testRefreshTokenUnauthorized() throws Exception {
        mockMvc.perform(post("/refresh-token")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "dummyToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.httpStatus").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Token is missing, invalid or expired."));
    }

    private String generateToken() {
        return jwtTokenService.generateAccessToken(user);
    }
}
