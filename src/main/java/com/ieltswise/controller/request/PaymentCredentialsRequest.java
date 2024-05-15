package com.ieltswise.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        title = "Request to update the tutor's payment details",
        description = "An object containing data for updating the tutor's payment details"
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCredentialsRequest {

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            format = "email",
            description = "The teacher's email address",
            example = "test.tutor1.ieltswise67@gmail.com"
    )
    @Email(message = "should be valid")
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String tutorEmail;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            description = "Payment ID received on PayPal in case of payment",
            example = "PAYID-MYFOPQQ95684077M72601402"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String clientId;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            description = "Payment ID received on PayPal in case of payment",
            example = "PAYID-MYFOPQQ95684077M72601402"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String clientSecret;
}
