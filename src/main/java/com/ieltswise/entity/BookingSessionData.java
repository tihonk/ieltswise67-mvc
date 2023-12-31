package com.ieltswise.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookingSessionData {

    private String tutorEmail;
    private String tutorCode;
    private String studentEmail;
    private String startDate;
    private String endDate;
}
