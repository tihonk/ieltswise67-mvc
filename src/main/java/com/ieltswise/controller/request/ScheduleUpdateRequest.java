package com.ieltswise.controller.request;

import com.ieltswise.dto.TimeSlot;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleUpdateRequest {

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private Map<DayOfWeek, List<TimeSlot>> updatedTimeInfo;
}
