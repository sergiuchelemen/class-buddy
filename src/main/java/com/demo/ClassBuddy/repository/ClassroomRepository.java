package com.demo.ClassBuddy.repository;

import com.demo.ClassBuddy.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Optional<Classroom> findClassroomByCode(String code);

    @Query("select c from Classroom c where c.name = ?1")
    Optional<Classroom> findClassroomByName(String name);
}
