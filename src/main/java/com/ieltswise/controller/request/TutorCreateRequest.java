package com.ieltswise.controller.request;

import com.ieltswise.dto.TimeSlot;
import jakarta.validation.constraints.Email;
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
public class TutorCreateRequest {

    @Email(message = "should be valid")
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String email;

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String name;

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private Map<DayOfWeek, List<TimeSlot>> updatedTimeInfo;

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String clientId;

    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String clientSecret;
}
