package com.ieltswise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeAndBusyHoursOfTheDay {

    private Long date;
    private List<Map<String, Object>> time;
}
