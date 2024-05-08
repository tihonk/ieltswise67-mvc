package com.ieltswise.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCredentialsRequest {

    @Email(message = "should be valid")
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String tutorEmail;

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String clientId;

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String clientSecret;
}
