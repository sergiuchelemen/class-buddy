package com.demo.ClassBuddy.dto;

import com.demo.ClassBuddy.model.Classroom;
import lombok.Builder;

@Builder
public record ClassroomDTO(
        Long id,
        String name,
        String subject,
        String code,
        String owner
) {
}
