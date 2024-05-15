package com.ieltswise.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        title = "Request to create a comment",
        description = "An object containing data for creating a comment"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCommentRequest {

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            format = "email",
            description = "Email of the student the lesson is planned",
            example = "BobRTY145@gmail.com"
    )
    @Email(message = "should be valid")
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String email;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "Student's comment",
            example = "I like learning English!"
    )
    @Size(min = 1, max = 1000, message = "must contain from 1 to 1000 characters")
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String value;
}
