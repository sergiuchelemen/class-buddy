package com.demo.ClassBuddy.service;

import com.demo.ClassBuddy.model.Comment;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.repository.AnnouncementRepository;
import com.demo.ClassBuddy.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AnnouncementRepository announcementRepository;

    public List<Comment> getCommentsForAnnouncement(Long announcementId) {
        return commentRepository.findByAnnouncementIdOrderByTimestampAsc(announcementId);
    }

    public Comment addComment(Long announcementId, String text, User user) {
        var announcementOpt = announcementRepository.findById(announcementId);
        if (announcementOpt.isEmpty()) {
            throw new RuntimeException("Announcement not found");
        }
        var comment = Comment.builder()
                .text(text)
                .commentedBy(user.getActualUsername())
                .timestamp(LocalDateTime.now())
                .announcement(announcementOpt.get())
                .build();
        return commentRepository.save(comment);
    }
}