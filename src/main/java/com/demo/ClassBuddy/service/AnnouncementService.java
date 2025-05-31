package com.demo.ClassBuddy.service;

import com.demo.ClassBuddy.exception.ClassroomNotFound;
import com.demo.ClassBuddy.model.Announcement;
import com.demo.ClassBuddy.model.Classroom;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.repository.AnnouncementRepository;
import com.demo.ClassBuddy.repository.ClassroomRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final ClassroomRepository classroomRepository;

    @Transactional
    public Announcement createAnnouncement(Long classroomId, String message, User authenticatedUser) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ClassroomNotFound("Classroom not found with id: " + classroomId));

        Announcement announcement = Announcement.builder()
                .message(message)
                .timestamp(LocalDateTime.now())
                .classroom(classroom)
                .createdBy(authenticatedUser.getActualUsername())
                .build();

        return announcementRepository.save(announcement);
    }

    @Transactional
    public List<Announcement> getAnnouncements(Long classroomId) {
        classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ClassroomNotFound("Classroom not found with id: " + classroomId));

        return new ArrayList<>(announcementRepository.findByClassroomOrderByTimestampDesc(classroomId));
    }
}
