package com.demo.ClassBuddy.repository;

import com.demo.ClassBuddy.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findAllByHomeworkId(Long homeworkId);
}
