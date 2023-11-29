package com.demo.ClassBuddy.classroom;

import com.demo.ClassBuddy.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "classroom")
public class Classroom {
    @Id
    private Long id;
    private String name;
    private String subject;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;

    public Classroom(){}
}
