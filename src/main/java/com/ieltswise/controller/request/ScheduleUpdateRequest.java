package com.ieltswise.controller.request;

import com.ieltswise.dto.TimeSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Schema(
        title = "Request to update the schedule",
        description = "An object containing data for updating information about the tutor's schedule"
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleUpdateRequest {

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "The tutor's schedule for the week with the specified busy and free hours of the day",
            example = "{\"MONDAY\": [{\"time\": \"09:00\", \"engaged\": false}]," +
                    " \"TUESDAY\": [{\"time\": \"10:00\", \"engaged\": true}]}"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private Map<DayOfWeek, List<TimeSlot>> updatedTimeInfo;
}
