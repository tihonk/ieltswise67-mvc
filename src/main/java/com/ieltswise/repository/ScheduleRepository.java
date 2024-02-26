package com.ieltswise.repository;

import com.ieltswise.entity.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT t.schedule FROM TutorInfo t WHERE t.email = :email")
    Optional<Schedule> findScheduleByEmail(@Param("email") String email);
}