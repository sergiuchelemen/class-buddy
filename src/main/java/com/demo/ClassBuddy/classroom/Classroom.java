package com.demo.ClassBuddy.classroom;

import com.demo.ClassBuddy.user.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "classroom")
public class Classroom {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "classroom_id")
    private Long id;
    private String name;
    private String subject;
    //@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;
    private String code;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "student_classroom", joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> students = new ArrayList<>();

    public Classroom(String name, String subject, User owner, String code) {
        this.name = name;
        this.subject = subject;
        this.owner = owner;
        this.code = code;
    }

    public Classroom() {

    }

    public List<User> getStudents() {
        return students;
    }
}
