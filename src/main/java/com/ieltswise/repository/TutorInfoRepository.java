package com.ieltswise.repository;

import com.ieltswise.entity.TutorInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorInfoRepository extends JpaRepository<TutorInfo, Long> {
}
