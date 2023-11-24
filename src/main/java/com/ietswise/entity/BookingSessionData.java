package com.ietswise.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class BookingSessionData {

    private String tutorEmail;
    private String tutorCode;
    private String studentEmail;
    private String startDate;
    private String endDate;
}
