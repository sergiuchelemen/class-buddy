package com.demo.ClassBuddy.controller;

import com.demo.ClassBuddy.model.Announcement;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.service.AnnouncementService;
import com.demo.ClassBuddy.service.ClassroomService;
import lombok.AllArgsConstructor;
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
@CrossOrigin("*")
@AllArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping(path = "{classroomId}")
    public Announcement createAnnouncement(@PathVariable Long classroomId,
                                           @AuthenticationPrincipal User authenticatedUser,
                                           @RequestBody Map<String, String> payload) {
        return announcementService.createAnnouncement(classroomId, payload.get("message"), authenticatedUser);
    }

    @GetMapping(path = "{classroomId}")
    public List<Announcement> getAnnouncements(@PathVariable Long classroomId,
                                               @AuthenticationPrincipal User authenticatedUser) {
        return announcementService.getAnnouncements(classroomId);
    }
}
