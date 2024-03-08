package com.ieltswise.mapper.impl;

import com.ieltswise.dto.PaymentCredentialsDto;
import com.ieltswise.entity.PaymentCredentials;
import com.ieltswise.mapper.PaymentCredentialsMapper;
import org.springframework.stereotype.Component;

@Component
public class PaymentCredentialsMapperImpl implements PaymentCredentialsMapper {

    @Override
    public PaymentCredentials mapToPaymentCredentials(PaymentCredentialsDto paymentCredentialsDto) {
        if (paymentCredentialsDto == null) {
            return null;
        } else {
            PaymentCredentials paymentCredentials = new PaymentCredentials();
            paymentCredentials.setTutorEmail(paymentCredentialsDto.getTutorEmail());
            paymentCredentials.setClientId(paymentCredentialsDto.getClientId());
            paymentCredentials.setClientSecret(paymentCredentialsDto.getClientSecret());
            return paymentCredentials;
        }
    }
}
