package com.ieltswise.controller.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class Event {

    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private String status;
}
