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
        title = "Request for a trial session",
        description = "An object containing data for requesting a trial session"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionDataRequest {

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            format = "email",
            description = "Email of the tutor the lesson is planned",
            example = "test.tutor1.ieltswise67@gmail.com"
    )
    @Email(message = "should be valid")
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String tutorEmail;

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
    private String studentEmail;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            description = "Student's name",
            example = "Bob"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String studentName;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            description = "Name of requested service",
            example = "Business English"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String requestedService;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            description = "Date and time of the lesson start",
            example = "2023-12-07T12:00:00+01:00"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String startDate;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            description = "Date and time of the end of the lesson",
            example = "2023-12-07T13:00:00+01:00"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String endDate;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            format = "url",
            description = "Link to event in Google calendar",
            example = "https://www.google.com/calendar/event?eid=MzdvMWk0bXQzcTI3OXAwNjdjdjBqMDhjbTggdm9sa29ub3Zza2lq" +
                    "X21yXzIxQG1mLmdyc3UuYnk"
    )
    private String eventLink;
}
