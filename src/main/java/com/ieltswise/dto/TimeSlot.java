package com.ieltswise.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        title = "Time slot",
        description = "Represents a time slot"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "Time",
            example = "12.00"
    )
    private String time;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "Indicates whether this time slot is engaged or available",
            example = "true"
    )
    private boolean engaged;
}
