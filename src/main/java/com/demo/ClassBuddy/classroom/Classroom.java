package com.demo.ClassBuddy.classroom;

import com.demo.ClassBuddy.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "classroom")
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String subject;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    private String code;

    public Classroom(){}
}
