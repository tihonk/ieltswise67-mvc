package com.ieltswise.controller.response;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private Integer errorCode;

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String errorMessage;
}
