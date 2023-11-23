package com.ietswise.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class FreeAndBusyHoursOfTheDay {
    private Long date;
    private List<Map<String, Object>> time;
}
