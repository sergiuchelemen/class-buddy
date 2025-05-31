package com.demo.ClassBuddy.controller;

import com.demo.ClassBuddy.dto.HomeworkDTO;
import com.demo.ClassBuddy.model.Homework;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.service.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/homework")
public class HomeworkController {

    private final HomeworkService homeworkService;

    @GetMapping("/{homeworkId}")
    public Homework get(@PathVariable Long homeworkId) {
        return homeworkService.get(homeworkId);
    }

    @GetMapping("/all/{classroomId}")
    public List<Homework> getAllHomework(@PathVariable Long classroomId) {
        return homeworkService.getAll(classroomId);
    }

    @PostMapping(path = "/{classroomId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Homework addHomework(@PathVariable Long classroomId, @ModelAttribute HomeworkDTO dto, @AuthenticationPrincipal User user) {
        return homeworkService.add(classroomId, dto, user);
    }
}
