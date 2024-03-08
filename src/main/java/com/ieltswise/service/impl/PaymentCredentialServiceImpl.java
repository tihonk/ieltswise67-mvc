package com.ieltswise.service.impl;

import com.ieltswise.dto.PaymentCredentialsDto;
import com.ieltswise.entity.PaymentCredentials;
import com.ieltswise.mapper.PaymentCredentialsMapper;
import com.ieltswise.repository.PaymentCredentialsRepository;
import com.ieltswise.service.PaymentCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentCredentialServiceImpl implements PaymentCredentialService {
    private final PaymentCredentialsRepository paymentCredentialsRepository;
    private final PaymentCredentialsMapper mapper;

    @Autowired
    public PaymentCredentialServiceImpl(PaymentCredentialsRepository paymentCredentialsRepository,
                                        PaymentCredentialsMapper mapper) {
        this.paymentCredentialsRepository = paymentCredentialsRepository;
        this.mapper = mapper;
    }

    @Override
    public PaymentCredentials updatePaymentInfo(PaymentCredentialsDto paymentCredentialsDto) {
        try {
            PaymentCredentials paymentCredentials = mapper.mapToPaymentCredentials(paymentCredentialsDto);
            return paymentCredentialsRepository.save(paymentCredentials);
        } catch (Exception e) {
            System.err.printf("Failed to update payment credentials: %s", e.getMessage());
            throw new RuntimeException();
        }
    }
}
