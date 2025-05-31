package com.demo.ClassBuddy.repository;

import com.demo.ClassBuddy.model.Homework;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface HomeworkRepository extends JpaRepository<Homework, Long> {

    List<Homework> findAllByClassroomId(Long classroomId);
}
