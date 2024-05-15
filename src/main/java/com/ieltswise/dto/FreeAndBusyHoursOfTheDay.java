package com.ieltswise.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(
        title = "Free and busy Hours of the day",
        description = "Represents the availability and unavailability of time slots for a specific day"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeAndBusyHoursOfTheDay {

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "Date in Unix timestamps format",
            example = "1722470400000"
    )
    private Long date;

    @ArraySchema(schema = @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "List of time slots"
    ))
    private List<TimeStatus> time;
}
