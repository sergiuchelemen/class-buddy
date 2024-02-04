package com.demo.ClassBuddy.classroom;

import com.demo.ClassBuddy.user.User;
import com.demo.ClassBuddy.user.UserRepository;
import com.demo.ClassBuddy.utility.NewClassRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class ClassroomService {
    UserRepository userRepository;
    ClassroomRepository classroomRepository;

    public ClassroomService(UserRepository userRepository, ClassroomRepository classroomRepository) {
        this.userRepository = userRepository;
        this.classroomRepository = classroomRepository;
    }

    public void createClass(NewClassRequest newClassRequest, String userEmail) {
        Optional<User> owner = userRepository.findByEmail(userEmail);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        Classroom classroom = new Classroom(newClassRequest.name(), newClassRequest.subject(), owner.get(), sb.toString());
        classroomRepository.save(classroom);
    }

    @Transactional
    public void joinClass(String userEmail, String code) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        Optional<Classroom> classroom = classroomRepository.findClassroomByCode(code);
        user.get().getJoinedClassrooms().add(classroom.get());
        classroom.get().getStudents().add(user.get());
    }

    @Transactional
    public String getClasses(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        Long userId = user.get().getId();
        return user.get().getJoinedClassrooms().toString();
    }
}
