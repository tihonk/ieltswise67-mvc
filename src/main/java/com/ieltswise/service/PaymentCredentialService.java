package com.ieltswise.service;

import com.ieltswise.dto.PaymentCredentialsDto;
import com.ieltswise.entity.PaymentCredentials;

public interface PaymentCredentialService {
    /**
     * Updating information about payment credentials based on the transferred DTO
     *
     * @param paymentCredentialsDto DTO with data on payment credentials
     * @return updated payment data
     */
    PaymentCredentials updatePaymentInfo(PaymentCredentialsDto paymentCredentialsDto);
}
