package com.ieltswise.mapper;

import com.ieltswise.dto.PaymentCredentialsDto;
import com.ieltswise.entity.PaymentCredentials;

public interface PaymentCredentialsMapper {
    /**
     * Transform a PaymentCredentialsDto object into a PaymentCredentials object.
     *
     * @param paymentCredentialsDto DTO containing payment credentials data.
     * @return PaymentCredentials object after transformation.
     */
    PaymentCredentials mapToPaymentCredentials(PaymentCredentialsDto paymentCredentialsDto);
}
