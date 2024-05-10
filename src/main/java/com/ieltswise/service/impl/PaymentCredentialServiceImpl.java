package com.ieltswise.service.impl;

import com.ieltswise.controller.request.PaymentCredentialsRequest;
import com.ieltswise.entity.PaymentCredentials;
import com.ieltswise.exception.EmailNotFoundException;
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
    public PaymentCredentials updatePaymentInfo(PaymentCredentialsRequest paymentCredentialsRequest)
            throws EmailNotFoundException {
        PaymentCredentials paymentCredentials = mapper.mapToPaymentCredentials(paymentCredentialsRequest);
        return paymentCredentialsRepository.save(paymentCredentials);
    }
}
