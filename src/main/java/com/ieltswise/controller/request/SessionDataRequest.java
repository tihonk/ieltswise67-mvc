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
@NoArgsConstructor
@AllArgsConstructor
public class SessionDataRequest {

    @Email(message = "should be valid")
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String tutorEmail;

    @Email(message = "should be valid")
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String studentEmail;

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String studentName;

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String requestedService;

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String startDate;

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String endDate;
    private String eventLink;
}
