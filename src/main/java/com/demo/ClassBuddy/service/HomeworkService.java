package com.demo.ClassBuddy.service;

import com.demo.ClassBuddy.dto.HomeworkDTO;
import com.demo.ClassBuddy.model.Homework;
import com.demo.ClassBuddy.model.User;
import com.demo.ClassBuddy.repository.HomeworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkService {

    private final static String BASE_URL = "http://localhost:8080";

    private final HomeworkRepository homeworkRepository;

    private final FileStorageService fileStorageService;

    @Transactional
    public List<Homework> getAll(Long classroomId) {
        var homeworks =  homeworkRepository.findAllByClassroomId(classroomId);
        for (Homework hw : homeworks) {
            if (hw.getFileName() != null && !hw.getFileName().isEmpty()) {
                String uri = BASE_URL + "/api/files/" + hw.getFileName();
                hw.setFileDownloadUri(uri);
            }
        }
        return homeworks;
    }

    @Transactional
    public Homework get(Long id) {
        return homeworkRepository.findById(id).get();
    }

    public Homework add(Long classroomId, HomeworkDTO homeworkDto, User user) {
        Homework newHomework = new Homework();
        newHomework.setClassroomId(classroomId);
        newHomework.setCreatedBy(user.getId());
        newHomework.setCreatedAt(LocalDateTime.now());
        newHomework.setTitle(homeworkDto.title());
        newHomework.setDescription(homeworkDto.description());
        newHomework.setDueDate(homeworkDto.dueDate());

        MultipartFile file = homeworkDto.file();
        if (file != null) {
            String storedFilename = fileStorageService.store(file);
            newHomework.setFileName(storedFilename);
        }
        return homeworkRepository.save(newHomework);
    }
}
