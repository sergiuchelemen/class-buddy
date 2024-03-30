package com.demo.ClassBuddy.repository;

import com.demo.ClassBuddy.model.Classroom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClassroomRepositoryTest {
    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.35");

    @Autowired
    ClassroomRepository classroomRepository;

    @BeforeEach
    public void setup() {
        Classroom classroom = Classroom.builder()
                .id(1L)
                .name("math101")
                .subject("mathematics")
                .code("random")
                .build();

        classroomRepository.save(classroom);
    }

    @Test
    public void testConnection() {
        assertTrue(mySQLContainer.isCreated());
        assertTrue(mySQLContainer.isRunning());
    }

    @Test
    public void testFindByCode() {
        Optional<Classroom> classroomOptional = classroomRepository.findClassroomByCode("random");

        assertTrue(classroomOptional.isPresent());
    }

    @Test
    public void testFindByCodeWithInvalidCode() {
        Optional<Classroom> classroomOptional = classroomRepository.findClassroomByCode("wrong-code");

        assertFalse(classroomOptional.isPresent());
    }
}