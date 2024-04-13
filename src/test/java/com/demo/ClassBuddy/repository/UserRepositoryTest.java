package com.demo.ClassBuddy.repository;

import com.demo.ClassBuddy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Container
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.35");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        User user = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .username("john doe")
                .email("john@email.com")
                .password("1234")
                .dateOfBirth(LocalDate.of(1980, 9, 9))
                .build();

        userRepository.save(user);
    }

    @Test
    public void testConnection() {
        assertTrue(mySQLContainer.isCreated());
        assertTrue(mySQLContainer.isRunning());
    }

    @Test
    public void testFindByEmail() {
        Optional<User> userOptional = userRepository.findByEmail("john@email.com");

        assertTrue(userOptional.isPresent());
    }

    @Test
    public void testFindByEmailWithInvalidEmail() {
        Optional<User> userOptional = userRepository.findByEmail("wrong@email.com");

        assertFalse(userOptional.isPresent());
    }
}