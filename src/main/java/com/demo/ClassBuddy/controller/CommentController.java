package com.demo.ClassBuddy.controller;

import com.demo.ClassBuddy.model.Comment;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/announcements")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("{annId}/comments")
    public ResponseEntity<List<Comment>> listComments(@PathVariable Long annId) {
        return ResponseEntity.ok(commentService.getCommentsForAnnouncement(annId));
    }

    @PostMapping("{annId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long annId, @RequestBody Map<String, String> body, @AuthenticationPrincipal User user) {
        var saved = commentService.addComment(annId, body.get("message"), user);
        return ResponseEntity.ok(saved);
    }
}