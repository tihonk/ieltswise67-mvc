package com.ieltswise.repository;

import com.ieltswise.entity.StudentUsedTrial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentUsedTrialRepository extends JpaRepository<StudentUsedTrial, Long> {
    StudentUsedTrial findByEmail(String email);
}
