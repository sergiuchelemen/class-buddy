package com.demo.ClassBuddy.user;

import com.demo.ClassBuddy.classroom.Classroom;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;


@Entity
@Table(name = "user")
public class User implements UserDetails {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private LocalDate dateOfBirth;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Classroom> ownedClassrooms = new ArrayList<>();

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH }
    )
    @JoinTable(name = "student_classroom", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "classroom_id"))
    private List<Classroom> joinedClassrooms = new ArrayList<>();

    @Transient
    private int age;

    public User(String firstname, String lastname, String username, String email, String password, LocalDate dateOfBirth) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    public User(long id, String firstname, String lastname, String username, String email, String password, LocalDate dateOfBirth) {
        this(firstname, lastname, username, email, password, dateOfBirth);
        this.id = id;
    }

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getActualUsername() {
        return this.username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public List<Classroom> getJoinedClassrooms() {
        return joinedClassrooms;
    }

    public Long getId() {
        return this.id;
    }
}
