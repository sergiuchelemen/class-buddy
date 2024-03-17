package com.demo.ClassBuddy.dto;

import com.demo.ClassBuddy.model.Classroom;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ClassroomDTOMapper implements Function<Classroom, ClassroomDTO> {
    @Override
    public ClassroomDTO apply(Classroom classroom) {
        return new ClassroomDTO(
                classroom.getId(),
                classroom.getName(),
                classroom.getSubject(),
                classroom.getCode(),
                classroom.getOwner().getActualUsername()
        );
    }
}
