package com.ieltswise.mapper.impl;

import com.ieltswise.dto.PaymentCredentialsDto;
import com.ieltswise.entity.PaymentCredentials;
import com.ieltswise.mapper.PaymentCredentialsMapper;
import com.ieltswise.repository.PaymentCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentCredentialsMapperImpl implements PaymentCredentialsMapper {
    private final PaymentCredentialsRepository paymentCredentialsRepository;

    @Autowired
    public PaymentCredentialsMapperImpl(PaymentCredentialsRepository paymentCredentialsRepository) {
        this.paymentCredentialsRepository = paymentCredentialsRepository;
    }

    @Override
    public PaymentCredentials mapToPaymentCredentials(PaymentCredentialsDto paymentCredentialsDto) {
        if (paymentCredentialsDto == null) {
            return null;
        } else {
            String email = paymentCredentialsDto.getTutorEmail();
            PaymentCredentials paymentCredentials = paymentCredentialsRepository.findByTutorEmail(email).orElseThrow(() ->
                    new RuntimeException("Tutor with email " + email + " not found"));
            paymentCredentials.setClientId(paymentCredentialsDto.getClientId());
            paymentCredentials.setClientSecret(paymentCredentialsDto.getClientSecret());
            return paymentCredentials;
        }
    }
}
