package com.ietswise.repository;

import com.ietswise.entity.UsedTrialLessons;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrialSessions extends JpaRepository<UsedTrialLessons, Long> {
    UsedTrialLessons findByEmail(String email);
}
