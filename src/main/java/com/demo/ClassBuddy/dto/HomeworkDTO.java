package com.demo.ClassBuddy.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record HomeworkDTO(String title, String description, MultipartFile file,
                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                          LocalDateTime dueDate) {
}
