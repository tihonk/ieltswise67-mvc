package com.ieltswise.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCommentRequest {

    @Email(message = "should be valid")
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String email;

    @Size(min = 1, max = 1000, message = "must contain from 1 to 1000 characters")
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String value;
}
