package com.demo.ClassBuddy.repository;

import com.demo.ClassBuddy.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    @Query(
            value = "SELECT * FROM announcement a WHERE a.classroom_id = :classroomId ORDER BY a.timestamp DESC",
            nativeQuery = true
    )
    List<Announcement> findByClassroomOrderByTimestampDesc(@Param("classroomId") Long classroomId);

}
