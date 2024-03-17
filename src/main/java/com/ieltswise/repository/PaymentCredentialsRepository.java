package com.ieltswise.repository;

import com.ieltswise.entity.PaymentCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentCredentialsRepository extends JpaRepository<PaymentCredentials, Long> {

    Optional<PaymentCredentials> findByTutorEmail(String email);
}
