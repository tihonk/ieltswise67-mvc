package com.ieltswise.controller.request;

import com.ieltswise.entity.schedule.TimeSlot;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class TutorCreateRequest {
    private String email;
    private String name;
    private Map<DayOfWeek, List<TimeSlot>> updatedTimeInfo;
}
