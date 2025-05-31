package com.demo.ClassBuddy.dto;

import lombok.Builder;

@Builder
public record ClassroomDTO(
        Long id,
        String name,
        String subject,
        String code,
        UserDTO owner
) {
}
