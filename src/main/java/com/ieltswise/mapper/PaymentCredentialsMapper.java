package com.ieltswise.mapper;

import com.ieltswise.controller.request.PaymentCredentialsRequest;
import com.ieltswise.entity.PaymentCredentials;
import com.ieltswise.exception.TutorEmailNotFoundException;

public interface PaymentCredentialsMapper {

    /**
     * Transform a PaymentCredentialsRequest object into a PaymentCredentials object.
     *
     * @param paymentCredentialsRequest DTO containing payment credentials data.
     * @return PaymentCredentials object after transformation.
     * @throws TutorEmailNotFoundException if the tutor's email is not found
     *
     */
    PaymentCredentials mapToPaymentCredentials(PaymentCredentialsRequest paymentCredentialsRequest)
            throws TutorEmailNotFoundException;
}
