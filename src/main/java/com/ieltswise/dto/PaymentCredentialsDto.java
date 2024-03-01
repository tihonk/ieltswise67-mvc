package com.ieltswise.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentCredentialsDto {
    private String tutorEmail;
    private String clientId;
    private String clientSecret;
}
