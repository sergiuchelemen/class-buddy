package com.demo.ClassBuddy.controller;

import com.demo.ClassBuddy.dto.ClassroomDTO;
import com.demo.ClassBuddy.model.Classroom;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.service.ClassroomService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller that handles classroom-related operations.
 */
@RestController
@RequestMapping(path = "/")
@AllArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    /**
     * Creates a new classroom.
     *
     * @param classroom The details of the classroom to create.
     * @param authenticatedUser The current authenticated user creating the classroom.
     * @return A DTO representing the newly created classroom.
     */
    @PostMapping(path = "/create-classroom")
    public ClassroomDTO createClassroom(@RequestBody Classroom classroom, @AuthenticationPrincipal User authenticatedUser) {
        return classroomService.createClassroom(classroom, authenticatedUser);
    }

    /**
     * Retrieves a list of classrooms owned by the authenticated user.
     *
     * @param authenticationPrincipal The authenticated user.
     * @return A list of owned classroom DTOs.
     */
    @GetMapping(path = "/owned-classrooms")
    public List<ClassroomDTO> getOwnedClassrooms(@AuthenticationPrincipal User authenticationPrincipal) {
        return classroomService.getOwnedClassrooms(authenticationPrincipal);
    }

    /**
     * Retrieves a list of classrooms in which the authenticated user is enrolled.
     *
     * @param authenticationPrincipal The authenticated user.
     * @return A list of enrolled classroom DTOs.
     */
    @GetMapping(path = "/enrolled-classrooms")
    public List<ClassroomDTO> getEnrolledClassrooms(@AuthenticationPrincipal User authenticationPrincipal) {
        return classroomService.getEnrolledClassrooms(authenticationPrincipal);
    }

    /**
     * Retrieves the detailed content of a specific classroom owned by the authenticated user.
     *
     * @param authenticationPrincipal The authenticated user.
     * @param id The id of the classroom to retrieve.
     * @return A DTO representing the classroom content.
     */
    @GetMapping(path = "/owned-classrooms/{id}")
    public ClassroomDTO getOwnedClassroomContent(@AuthenticationPrincipal User authenticationPrincipal, @PathVariable String id) {
        return classroomService.getOwnedClassroomContent(authenticationPrincipal, id);
    }

    /**
     * Retrieves the detailed content of a specific classroom in which the authenticated user is enrolled.
     *
     * @param authenticationPrincipal The authenticated user.
     * @param id The ID of the classroom to retrieve.
     * @return A DTO representing the classroom content.
     */
    @GetMapping(path = "/enrolled-classrooms/{id}")
    public ClassroomDTO getEnrolledClassroomContent(@AuthenticationPrincipal User authenticationPrincipal, @PathVariable String id) {
        return classroomService.getEnrolledClassroomsContent(authenticationPrincipal, id);
    }

    /**
     * Allows a user to join an existing classroom using its code.
     *
     * @param code A map containing a single entry with the key "classroomCode" representing the code to join.
     * @param authenticatedUser The authenticated user attempting to join.
     * @return A DTO representing the joined classroom.
     */
    @PostMapping(path = "/join-classroom")
    public ClassroomDTO joinClassroom(@RequestBody Map<String, String> code, @AuthenticationPrincipal User authenticatedUser) {
        String classroomCode = code.get("classroomCode");
        return classroomService.joinClassroom(classroomCode, authenticatedUser);
    }
}
