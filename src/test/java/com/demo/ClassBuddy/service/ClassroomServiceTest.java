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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassroomServiceTest {
    @Mock
    private ClassroomRepository classroomRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClassroomDTOMapper classroomDTOMapper;
    @InjectMocks
    private ClassroomService classroomService;
    private Classroom mathClassroom, historyClassroom, chemistryClassroom;
    private ClassroomDTO mathClassroomDTO, historyClassroomDTO, chemistryClassroomDTO;
    private User user;

    @BeforeEach
    public void setup() {
        mathClassroom = Classroom.builder()
                .id(1L)
                .name("math101")
                .subject("mathematics")
                .students(new ArrayList<>())
                .build();

        historyClassroom = Classroom.builder()
                .id(2L)
                .name("history101")
                .subject("history")
                .build();

        chemistryClassroom = Classroom.builder()
                .id(3L)
                .name("chemistry101")
                .subject("chemistry")
                .build();

        user = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .username("john doe")
                .email("john@email.com")
                .password("1234")
                .dateOfBirth(LocalDate.of(1980, 9, 9))
                .ownedClassrooms(new ArrayList<>(List.of(mathClassroom, historyClassroom)))
                .enrolledClassrooms(new ArrayList<>(List.of(chemistryClassroom)))
                .build();

        mathClassroomDTO = ClassroomDTO.builder()
                .id(1L)
                .subject("mathematics")
                .name("math101")
                .build();

        historyClassroomDTO = ClassroomDTO.builder()
                .id(2L)
                .subject("history")
                .name("history101")
                .build();

        chemistryClassroomDTO = ClassroomDTO.builder()
                .id(3L)
                .subject("chemistry")
                .name("chemistry101")
                .build();
    }

    @Test
    public void createClassroom() {
        when(classroomRepository.findClassroomByName(mathClassroom.getName())).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(classroomRepository.save(mathClassroom)).thenReturn(mathClassroom);
        when(classroomDTOMapper.apply(mathClassroom)).thenReturn(mathClassroomDTO);

        ClassroomDTO returnedDTO = classroomService.createClassroom(mathClassroom, user);

        assertEquals(user, mathClassroom.getOwner());
        assertEquals(returnedDTO.id(), mathClassroom.getId());
        assertEquals(returnedDTO.name(), mathClassroom.getName());
        assertEquals(returnedDTO.subject(), mathClassroom.getSubject());
        assertNotNull(mathClassroom.getCode());
    }

    @Test
    public void createClassroomWithSameName() {
        when(classroomRepository.findClassroomByName(mathClassroom.getName())).thenReturn(Optional.of(mathClassroom));

        ClassroomAlreadyExistsException exception = assertThrows(ClassroomAlreadyExistsException.class, () -> classroomService.createClassroom(mathClassroom, user));

        assertEquals("A classroom with same name is already created.", exception.getMessage());
    }

    @Test
    public void createClassroomWithWrongUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> classroomService.createClassroom(mathClassroom, user));

        assertEquals("User not found. Please log in again.", exception.getMessage());
    }

    @Test
    public void getOwnedClassroom() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(classroomDTOMapper.apply(mathClassroom)).thenReturn(mathClassroomDTO);
        when(classroomDTOMapper.apply(historyClassroom)).thenReturn(historyClassroomDTO);

        List<ClassroomDTO> returnedValues = classroomService.getOwnedClassrooms(user);

        assertEquals(List.of(mathClassroomDTO, historyClassroomDTO), returnedValues);
    }

    @Test
    public void getOwnedClassroomWithNoClassrooms() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        user.setOwnedClassrooms(List.of());

        List<ClassroomDTO> returnedValues = classroomService.getOwnedClassrooms(user);

        assertEquals(List.of(), returnedValues);
    }

    @Test
    public void getOwnedClassroomsWithWrongUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> classroomService.getOwnedClassrooms(user));

        assertEquals("User not found. Please log in again.", exception.getMessage());
    }

    @Test
    public void getEnrolledClassrooms() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(classroomDTOMapper.apply(chemistryClassroom)).thenReturn(chemistryClassroomDTO);

        List<ClassroomDTO> returnedValues = classroomService.getEnrolledClassrooms(user);

        assertEquals(List.of(chemistryClassroomDTO), returnedValues);
    }

    @Test
    public void getEnrolledClassroomsWithNoClassrooms() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        user.setEnrolledClassrooms(List.of());

        List<ClassroomDTO> returnedValues = classroomService.getEnrolledClassrooms(user);

        assertEquals(List.of(), returnedValues);
    }

    @Test
    public void getOwnedClassroomWithWrongUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> classroomService.getEnrolledClassrooms(user));

        assertEquals("User not found. Please log in again.", exception.getMessage());
    }

    @Test
    public void joinClassroom() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(classroomRepository.findClassroomByCode("random-code")).thenReturn(Optional.of(mathClassroom));
        when(classroomDTOMapper.apply(mathClassroom)).thenReturn(mathClassroomDTO);

        ClassroomDTO returnedClassroom = classroomService.joinClassroom("random-code", user);

        assertEquals(mathClassroomDTO, returnedClassroom);
        assertEquals(List.of(chemistryClassroom, mathClassroom), user.getEnrolledClassrooms());
        assertEquals(List.of(user), mathClassroom.getStudents());
    }

    @Test
    public void joinClassroomWithWrongUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> classroomService.joinClassroom("random-code", user));

        assertEquals("User not found. Please log in again.", exception.getMessage());
    }

    @Test
    public void joinNonExistingClassroom() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(classroomRepository.findClassroomByCode("random-code")).thenReturn(Optional.empty());

        ClassroomNotFound exception = assertThrows(ClassroomNotFound.class, () -> classroomService.joinClassroom("random-code", user));

        assertEquals("The code provided is not related with any class.", exception.getMessage());
    }

    @Test
    public void joinInEnrolledClassroom() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(classroomRepository.findClassroomByCode("random-code")).thenReturn(Optional.of(mathClassroom));
        mathClassroom.setStudents(List.of(user));

        StudentAlreadyEnrolledException exception = assertThrows(StudentAlreadyEnrolledException.class, () -> classroomService.joinClassroom("random-code", user));

        assertEquals("You're already enrolled in the requested class.", exception.getMessage());
    }



    @Test
    public void getOwnedClassroomContent() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(classroomDTOMapper.apply(mathClassroom)).thenReturn(mathClassroomDTO);

        ClassroomDTO returnedClassroom = classroomService.getOwnedClassroomContent(user, "1");

        assertEquals(mathClassroomDTO, returnedClassroom);
    }

    @Test
    public void getNoOwnedClassroomContent() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ClassroomNotFound exception = assertThrows(ClassroomNotFound.class, () -> classroomService.getOwnedClassroomContent(user, "1"));

        assertEquals("The id provided is not related with any class.", exception.getMessage());
    }

    @Test
    public void getOwnedClassroomContentWithWrongUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> classroomService.getOwnedClassroomContent(user, "1"));

        assertEquals("User not found. Please log in again.", exception.getMessage());
    }


    @Test
    public void getEnrolledClassroomContent() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(classroomDTOMapper.apply(chemistryClassroom)).thenReturn(chemistryClassroomDTO);

        ClassroomDTO returnedClassroom = classroomService.getEnrolledClassroomsContent(user, "3");

        assertEquals(chemistryClassroomDTO, returnedClassroom);
    }

    @Test
    public void getNoEnrolledClassroomContent() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ClassroomNotFound exception = assertThrows(ClassroomNotFound.class, () -> classroomService.getEnrolledClassroomsContent(user, "1"));

        assertEquals("The id provided is not related with any class.", exception.getMessage());
    }

    @Test
    public void getEnrolledClassroomContentWithWrongUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> classroomService.getEnrolledClassroomsContent(user, "1"));

        assertEquals("User not found. Please log in again.", exception.getMessage());
    }
}