package com.demo.ClassBuddy.user;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "user")
public class User {
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private Date dateOfBirth;
    @Transient
    private int age;

    public User(long id, String firstname, String lastname, String username, String email, String password, Date dateOfBirth) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    public User() {}

}
