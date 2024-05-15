package com.ieltswise.dto;

import com.ieltswise.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        title = "Time status",
        description = "Represents a time status"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeStatus {

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "Time",
            example = "1735689600000"
    )
    private Long time;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "Indicates the time status",
            example = "AVAILABLE"
    )
    private Status status;
}
