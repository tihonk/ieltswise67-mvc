package com.ieltswise.service;

import com.ieltswise.dto.PaymentCredentialsDto;
import com.ieltswise.entity.PaymentCredentials;

public interface PaymentCredentialService {
    /**
     * Saving information about payment credentials based on the transferred DTO
     *
     * @param paymentCredentialsDto DTO with data on payment credentials
     * @return saved payment data
     */
    PaymentCredentials savePaymentInfo(PaymentCredentialsDto paymentCredentialsDto);
}
