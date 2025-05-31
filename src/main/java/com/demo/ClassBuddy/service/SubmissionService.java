package com.demo.ClassBuddy.service;

import com.demo.ClassBuddy.model.Submission;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.repository.SubmissionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SubmissionService {

    private final FileStorageService fileStorageService;
    private final SubmissionRepository submissionRepository;

    public void submit(Long homeworkId, List<MultipartFile> files, User user) {
        files.forEach(file -> {
            Submission sub = new Submission();
            sub.setHomeworkId(homeworkId);
            sub.setSubmittedBy(user.getFirstname() + " " + user.getLastname());
            sub.setSubmittedAt(LocalDateTime.now());

            if (file != null) {
                String storedFilename = fileStorageService.store(file);
                sub.setFileName(storedFilename);
            }
            submissionRepository.save(sub);
        });
    }

    @Transactional
    public List<Submission> getAll(Long homeworkId) {
        return submissionRepository.findAllByHomeworkId(homeworkId);
    }

    public void submitGrade(int grade, Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Submission with ID " + submissionId + " not found."));

        submission.setGrade(grade);
        submissionRepository.save(submission);
    }
}
