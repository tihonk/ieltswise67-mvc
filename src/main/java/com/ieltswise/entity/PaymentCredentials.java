package com.ieltswise.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PAYMENT_CREDENTIALS")
public class PaymentCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;
    @Column(name = "EMAIL", unique = true, nullable = false)
    private String tutorEmail;
    @Column(name = "CLIENT_ID", unique = true, nullable = false)
    private String clientId;
    @Column(name = "CLIENT_SECRET", unique = true, nullable = false)
    private String clientSecret;
    @Column(name = "PAYMENT_ID", unique = true)
    private String paymentId;
    @Column(name = "ACCESS_TOKEN", unique = true)
    private String accessToken;
    @Column(name = "TOKEN_EXPIRES")
    private Long expires;
}
