package com.ieltswise.controller.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SessionDataResponse {

    @Email(message = "should be valid")
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String studentEmail;

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String sessionTime;

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String eventLink;

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String requestedService;
}
