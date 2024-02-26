package com.ieltswise.entity.schedule;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TimeSlot {
    private String time;
    private boolean availability;
}
