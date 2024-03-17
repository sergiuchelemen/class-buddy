package com.demo.ClassBuddy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Classroom {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private Long id;

    private String name;

    private String subject;

    private String code;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @ManyToMany(mappedBy = "enrolledClassrooms")
    private List<User> students;
}
