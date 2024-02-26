package com.ieltswise.repository;

import com.ieltswise.entity.TutorInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorInfoRepository extends JpaRepository<TutorInfo, Long> {
    Optional<TutorInfo> findByEmail(String email);
}
