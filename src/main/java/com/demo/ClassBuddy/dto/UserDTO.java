package com.demo.ClassBuddy.dto;

import lombok.Builder;

@Builder
public record UserDTO(
        Long id,
        String firstname,
        String lastname,
        String email
) {
}
