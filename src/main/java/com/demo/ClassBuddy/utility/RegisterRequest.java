package com.demo.ClassBuddy.utility;

import java.time.LocalDate;


public record RegisterRequest(String firstname, String lastname, String username, String password, String email, LocalDate dateOfBirth) { }
