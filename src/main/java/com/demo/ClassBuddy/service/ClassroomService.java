package com.demo.ClassBuddy.service;

import com.demo.ClassBuddy.dto.ClassroomDTO;
import com.demo.ClassBuddy.dto.ClassroomDTOMapper;
import com.demo.ClassBuddy.exception.ClassroomAlreadyExistsException;
import com.demo.ClassBuddy.exception.ClassroomNotFound;
import com.demo.ClassBuddy.exception.StudentAlreadyEnrolledException;
import com.demo.ClassBuddy.exception.UserNotFoundException;
import com.demo.ClassBuddy.model.Classroom;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.repository.ClassroomRepository;
import com.demo.ClassBuddy.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;

    private final UserRepository userRepository;

    private final ClassroomDTOMapper classroomDTOMapper;

    @Transactional
    public ClassroomDTO createClassroom(Classroom classroom, User authenticatedUser) {
        Optional<Classroom> seekedClassroom = classroomRepository.findClassroomByName(classroom.getName());
        if (seekedClassroom.isPresent()) {
            throw new ClassroomAlreadyExistsException("A classroom with the same name was already created.");
        }

        User user = retrieveUserFromDatabase(authenticatedUser);
        String classroomCode = generateClassroomCode();
        classroom.setCode(classroomCode);
        classroom.setOwner(user);

        Classroom createdClassroom = classroomRepository.save(classroom);
        return classroomDTOMapper.apply(createdClassroom);
    }

    @Transactional
    public List<ClassroomDTO> getOwnedClassrooms(User authenticatedUser) {
        User user = retrieveUserFromDatabase(authenticatedUser);
        return user.getOwnedClassrooms()
                .stream()
                .map(classroomDTOMapper)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ClassroomDTO> getEnrolledClassrooms(User authenticatedUser) {
        User user = retrieveUserFromDatabase(authenticatedUser);
        return user.getEnrolledClassrooms()
                .stream()
                .map(classroomDTOMapper)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClassroomDTO joinClassroom(String classroomCode, User authenticatedUser) {
        User user = retrieveUserFromDatabase(authenticatedUser);
        Classroom classroom = classroomRepository.findClassroomByCode(classroomCode)
                .orElseThrow(() -> new ClassroomNotFound("The code provided is not related with any class."));

        if (classroom.getStudents().contains(user)) {
            throw new StudentAlreadyEnrolledException("You're already enrolled in the requested class.");
        }

        user.getEnrolledClassrooms().add(classroom);
        classroom.getStudents().add(user);
        return classroomDTOMapper.apply(classroom);
    }

    @Transactional
    public ClassroomDTO getOwnedClassroomContent(User authenticatedUser, String classroomId) {
        User user = retrieveUserFromDatabase(authenticatedUser);

        return user.getOwnedClassrooms().stream()
                .filter(classroom -> classroom.getId().equals(Long.valueOf(classroomId)))
                .findFirst()
                .map(classroomDTOMapper)
                .orElseThrow(() -> new ClassroomNotFound("The id provided is not related with any class."));
    }

    @Transactional
    public ClassroomDTO getEnrolledClassroomsContent(User authenticatedUser, String classroomId) {
        User user = retrieveUserFromDatabase(authenticatedUser);

        return user.getEnrolledClassrooms().stream()
                .filter(classroom -> classroom.getId().equals(Long.valueOf(classroomId)))
                .findFirst()
                .map(classroomDTOMapper)
                .orElseThrow(() -> new ClassroomNotFound("The id provided is not related with any class."));
    }

    private User retrieveUserFromDatabase(User authenticatedUser) {
        return userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found. Please log in again."));
    }

    private String generateClassroomCode() {
        return RandomStringUtils.randomAlphabetic(10);
    }
}
