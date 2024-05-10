package com.ieltswise.service;

import com.ieltswise.controller.request.PaymentCredentialsRequest;
import com.ieltswise.entity.PaymentCredentials;
import com.ieltswise.exception.EmailNotFoundException;

public interface PaymentCredentialService {

    /**
     * Updating information about payment credentials based on the transferred DTO
     *
     * @param paymentCredentialsRequest DTO with data on payment credentials
     * @return updated payment data
     * @throws EmailNotFoundException if the tutor's email is not found.
     */
    PaymentCredentials updatePaymentInfo(PaymentCredentialsRequest paymentCredentialsRequest)
            throws EmailNotFoundException;
}
