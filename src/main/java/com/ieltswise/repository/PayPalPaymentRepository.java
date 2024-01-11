package com.ieltswise.repository;

import com.ieltswise.entity.UserLessonData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayPalPaymentRepository extends JpaRepository<UserLessonData, Long> {
}
