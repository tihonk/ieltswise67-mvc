package com.ieltswise.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Schema(
        title = "Lesson Booking Response",
        description = "Response containing information about the booked session"
)
@Builder
@Getter
public class SessionDataResponse {

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
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
            description = "Date and time of the lesson start",
            example = "2023-12-07T12:00:00+01:00"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String sessionTime;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "url",
            description = "Link to event in Google calendar",
            example = "https://www.google.com/calendar/event?eid=MzdvMWk0bXQzcTI3OXAwNjdjdjBqMDhjbTggdm9sa29ub3Zza2lq" +
                    "X21yXzIxQG1mLmdyc3UuYnk"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String eventLink;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "Name of requested service",
            example = "Business English"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String requestedService;
}
