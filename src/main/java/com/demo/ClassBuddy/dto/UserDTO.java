package com.demo.ClassBuddy.dto;

public record UserDTO(
        Long id,
        String firstname,
        String lastname,
        String email
) {
}
