package com.ieltswise.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(
        title = "Request for a regular session",
        description = "An object containing data for requesting a regular lesson"
)
@Getter
@Setter
public class RegularSessionDataRequest extends SessionDataRequest {

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            description = "Payment ID received on PayPal in case of payment",
            example = "PAYID-MYFOPQQ95684077M72601402"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String paymentId;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            description = "Payer ID received on the PayPal in case of payment",
            example = "BZBP4WVQ2PNJ2"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String payerID;
}
