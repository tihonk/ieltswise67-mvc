package com.ieltswise.controller.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentCredentialsRequest {

    private String tutorEmail;
    private String clientId;
    private String clientSecret;
}
