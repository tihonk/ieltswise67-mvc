package com.ieltswise.repository;

import com.ieltswise.entity.StudentComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCommentRepository extends JpaRepository<StudentComment, Long> {
}
