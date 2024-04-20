package com.demo.ClassBuddy.controller;

import com.demo.ClassBuddy.model.Classroom;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.repository.ClassroomRepository;
import com.demo.ClassBuddy.repository.UserRepository;
import com.demo.ClassBuddy.service.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClassroomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    private User user1, user2;

    private Classroom classroom2;

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
        user1 = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .username("johndoe")
                .email("johndoe@email.com")
                .password("1234")
                .dateOfBirth(LocalDate.of(1980, 9, 9))
                .ownedClassrooms(new ArrayList<>())
                .enrolledClassrooms(new ArrayList<>())
                .build();

        user2 = User.builder()
                .id(2L)
                .firstname("Jane")
                .lastname("Smith")
                .username("janesmith")
                .email("janesmith@email.com")
                .password("5678")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .ownedClassrooms(new ArrayList<>())
                .enrolledClassrooms(new ArrayList<>())
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        Classroom classroom1 = Classroom.builder()
                .id(1L)
                .name("math101")
                .subject("mathematics")
                .code("random-code")
                .owner(user1)
                .students(new ArrayList<>())
                .build();

        classroom2 = Classroom.builder()
                .id(2L)
                .name("english101")
                .subject("english")
                .code("random-code")
                .owner(user2)
                .students(new ArrayList<>())
                .build();

        classroomRepository.save(classroom1);
        classroomRepository.save(classroom2);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testCreateClassroom() throws Exception {
        Classroom newClassroom = Classroom.builder()
                .name("chemistry101")
                .subject("chemistry")
                .build();

        String request = objectMapper.writeValueAsString(newClassroom);

        mockMvc.perform(post("/create-classroom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header("Authorization", "Bearer " + generateToken(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("chemistry101"))
                .andExpect(jsonPath("$.subject").value("chemistry"))
                .andExpect(jsonPath("$.code").isNotEmpty())
                .andExpect(jsonPath("$.owner").value("johndoe"));
    }

    @Test
    public void testCreateClassroomUnauthorized() throws Exception {
        Classroom newClassroom = Classroom.builder()
                .name("math101")
                .subject("mathematics")
                .build();

        String request = objectMapper.writeValueAsString(newClassroom);
        mockMvc.perform(post("/create-classroom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.httpStatus").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Token is missing, invalid or expired."));
    }

    @Test
    public void testGetOwnedClassrooms() throws Exception {
        mockMvc.perform(get("/owned-classrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + generateToken(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].name").value("math101"))
                .andExpect(jsonPath("$[0].subject").value("mathematics"))
                .andExpect(jsonPath("$[0].code").value("random-code"))
                .andExpect(jsonPath("$[0].owner").value("johndoe"));
    }

    @Test
    public void testGetOwnedClassroomsUnauthorized() throws Exception {
        mockMvc.perform(get("/owned-classrooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.httpStatus").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Token is missing, invalid or expired."));
    }

    @Test
    public void testGetEnrolledClassrooms() throws Exception {
        user1.getEnrolledClassrooms().add(classroom2);
        classroom2.getStudents().add(user1);
        userRepository.save(user1);
        classroomRepository.save(classroom2);

        mockMvc.perform(get("/enrolled-classrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + generateToken(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].name").value("english101"))
                .andExpect(jsonPath("$[0].subject").value("english"))
                .andExpect(jsonPath("$[0].code").value("random-code"))
                .andExpect(jsonPath("$[0].owner").value("janesmith"));
    }

    @Test
    public void testGetEnrolledClassroomsUnauthorized() throws Exception {
        mockMvc.perform(get("/enrolled-classrooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.httpStatus").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Token is missing, invalid or expired."));
    }

    @Test
    public void testJoinClassroom() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("classroomCode", "dummy-code");
        Classroom classroomToJoin = Classroom.builder()
                .name("history101")
                .subject("history")
                .owner(user2)
                .code("dummy-code")
                .build();

        classroomRepository.save(classroomToJoin);

        mockMvc.perform(post("/join-classroom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + generateToken(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("history101"))
                .andExpect(jsonPath("$.subject").value("history"))
                .andExpect(jsonPath("$.code").value("dummy-code"))
                .andExpect(jsonPath("$.owner").value("janesmith"));
    }

    @Test
    public void testJoinNonExistingClassroom() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("classroomCode", "wrong-code");

        mockMvc.perform(post("/join-classroom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + generateToken(user1)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("The code provided is not related with any class."));
    }

    @Test
    public void testJoinClassroomUnauthorized() throws Exception {
        mockMvc.perform(post("/join-classroom")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.httpStatus").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Token is missing, invalid or expired."));
    }

    @Test
    public void testGetOwnedClassroomContent() throws Exception {
        mockMvc.perform(get("/owned-classrooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + generateToken(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("math101"))
                .andExpect(jsonPath("$.subject").value("mathematics"))
                .andExpect(jsonPath("$.code").value("random-code"))
                .andExpect(jsonPath("$.owner").value("johndoe"));
    }

    @Test
    public void testGetOwnedClassroomContentWithWrongId() throws Exception {
        mockMvc.perform(get("/owned-classrooms/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + generateToken(user1)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("The id provided is not related with any class."));
    }

    @Test
    public void testGetOwnedClassroomContentUnauthorized() throws Exception {
        mockMvc.perform(get("/owned-classrooms/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.httpStatus").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Token is missing, invalid or expired."));
    }

    @Test
    public void testGetEnrolledClassroomContent() throws Exception {
        user1.getEnrolledClassrooms().add(classroom2);
        classroom2.getStudents().add(user1);
        userRepository.save(user1);
        classroomRepository.save(classroom2);

        mockMvc.perform(get("/enrolled-classrooms/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + generateToken(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("english101"))
                .andExpect(jsonPath("$.subject").value("english"))
                .andExpect(jsonPath("$.code").value("random-code"))
                .andExpect(jsonPath("$.owner").value("janesmith"));
    }

    @Test
    public void testGetEnrolledClassroomContentWithWrongId() throws Exception {
        mockMvc.perform(get("/enrolled-classrooms/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + generateToken(user1)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("The id provided is not related with any class."));
    }

    @Test
    public void testGetEnrolledClassroomContentUnauthorized() throws Exception {
        mockMvc.perform(get("/enrolled-classrooms/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.httpStatus").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Token is missing, invalid or expired."));
    }

    private String generateToken(User user) {
        return jwtTokenService.generateAccessToken(user);
    }
}