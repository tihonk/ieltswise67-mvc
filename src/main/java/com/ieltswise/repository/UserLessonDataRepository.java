package com.ieltswise.repository;

import com.ieltswise.entity.UserLessonData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLessonDataRepository extends JpaRepository<UserLessonData, Long> {

    UserLessonData findByEmail(String userEmail);
}
