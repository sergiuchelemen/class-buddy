package com.demo.ClassBuddy.repository;

import com.demo.ClassBuddy.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Optional<Classroom> findClassroomByCode(String code);

    Optional<Classroom> findClassroomById(Long id);
}
