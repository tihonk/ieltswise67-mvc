package com.ieltswise.repository;

import com.ieltswise.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findScheduleByTutorEmail(String email);
}