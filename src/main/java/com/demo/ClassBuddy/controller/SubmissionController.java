package com.demo.ClassBuddy.controller;

import com.demo.ClassBuddy.model.Submission;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/submission")
@CrossOrigin("*")
public class SubmissionController {

    private final SubmissionService submissionService;

    @GetMapping("/{homeworkId}")
    public List<Submission> getAll(@PathVariable Long homeworkId) {
        return submissionService.getAll(homeworkId);
    }

    @PostMapping(path =  "/{homeworkId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void submit(@PathVariable Long homeworkId, @ModelAttribute List<MultipartFile> submissionFiles, @AuthenticationPrincipal User user) {
        submissionService.submit(homeworkId, submissionFiles, user);
    }

    @PostMapping("/grade")
    public void submitGrade(@RequestParam int grade, @RequestParam Long submissionId) {
        submissionService.submitGrade(grade, submissionId);
    }
}
