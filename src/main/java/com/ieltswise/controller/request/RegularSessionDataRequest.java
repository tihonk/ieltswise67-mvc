package com.ieltswise.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegularSessionDataRequest extends SessionDataRequest {

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String paymentId;

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String payerID;
}
