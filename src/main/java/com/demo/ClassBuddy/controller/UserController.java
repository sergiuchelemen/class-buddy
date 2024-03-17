package com.demo.ClassBuddy.controller;

import com.demo.ClassBuddy.dto.ClassroomDTO;
import com.demo.ClassBuddy.model.Classroom;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(path = "/create-classroom")
    public void createClassroom(@RequestBody Classroom classroom, @AuthenticationPrincipal User authenticatedUser) {
        userService.createClassroom(classroom, authenticatedUser);
    }

    @GetMapping(path = "/owned-classrooms")
    public List<ClassroomDTO> getOwnedClassrooms(@AuthenticationPrincipal User authenticationPrincipal) {
        return userService.getOwnedClassrooms(authenticationPrincipal);
    }

    @GetMapping(path = "/enrolled-classrooms")
    public List<ClassroomDTO> getEnrolledClassrooms(@AuthenticationPrincipal User authenticationPrincipal) {
        return userService.getEnrolledClassrooms(authenticationPrincipal);
    }

    @GetMapping(path = "/owned-classrooms/{id}")
    public ClassroomDTO getOwnedClassroomContent(@AuthenticationPrincipal User authenticationPrincipal, @PathVariable String id) {
        return userService.getOwnedClassroomContent(authenticationPrincipal, id);
    }

    @GetMapping(path = "/enrolled-classrooms/{id}")
    public ClassroomDTO getEnrolledClassroomContent(@AuthenticationPrincipal User authenticationPrincipal, @PathVariable String id) {
        return userService.getEnrolledClassroomsContent(authenticationPrincipal, id);
    }

    @PostMapping(path = "/join-classroom")
    public ClassroomDTO joinClassroom(@RequestBody Map<String, String> code, @AuthenticationPrincipal User authenticatedUser) {
        String classroomCode = code.get("classroomCode");
        return userService.joinClassroom(classroomCode, authenticatedUser);
    }
}
