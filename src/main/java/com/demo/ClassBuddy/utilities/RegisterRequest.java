package com.demo.ClassBuddy.utilities;

import java.time.LocalDate;


public record RegisterRequest(String firstname, String lastname, String username, String password, String email, LocalDate dateOfBirth) { }
