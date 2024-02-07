package com.ieltswise.repository;

import com.ieltswise.entity.PaymentCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentCredentialsRepository extends JpaRepository<PaymentCredentials, Long> {
    PaymentCredentials findByTutorEmail(String email);
}
