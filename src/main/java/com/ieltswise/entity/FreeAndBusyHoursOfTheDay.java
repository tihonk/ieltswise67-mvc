package com.ieltswise.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Class describing all dates of the month with the status of all hours in a day
 * @author Mark "markvl836@gmail.com"
 */
@Data
@NoArgsConstructor
public class FreeAndBusyHoursOfTheDay {
    private Long date;
    private List<Map<String, Object>> time;
}
