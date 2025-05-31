package com.demo.ClassBuddy.repository;

import com.demo.ClassBuddy.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAnnouncementIdOrderByTimestampAsc(Long announcementId);
}