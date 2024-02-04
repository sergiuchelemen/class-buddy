package com.demo.ClassBuddy;

import com.demo.ClassBuddy.classroom.Classroom;
import com.demo.ClassBuddy.user.User;
import com.demo.ClassBuddy.user.UserRepository;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        User user1 = new User( "Sergiu","Jonas", "sergiu", "sergiu@gmail.com", "1234", LocalDate.now());
        Classroom classroom = new Classroom("Calculus", "Math", user1, "1234");

    }
}
