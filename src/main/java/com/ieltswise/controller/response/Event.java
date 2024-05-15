package com.ieltswise.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Schema(
        title = "Event",
        description = "Represents a scheduled event"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "Start date and time of the event",
            example = "2024-06-05T02:00:00+03:00"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private ZonedDateTime startDate;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "End date and time of the event",
            example = "2024-06-05T02:59:00+03:00"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private ZonedDateTime endDate;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "Status of the event",
            example = "confirmed"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String status;
}
