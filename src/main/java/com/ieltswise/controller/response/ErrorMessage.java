package com.ieltswise.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        title = "Error message",
        description = "Represents an error message"
)
@Data
@AllArgsConstructor
public class ErrorMessage {

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "The error code",
            example = "1"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private Integer errorCode;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "The error message",
            example = "Internal server error"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String errorMessage;
}
