package com.demo.ClassBuddy.dto;

public record ClassroomDTO(
        Long id,
        String name,
        String subject,
        String code,
        String owner
) {
}
