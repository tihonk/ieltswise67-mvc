package com.ieltswise.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookingSessionData {

    private String tutorEmail;
    private String studentEmail;
    private String startDate;
    private String endDate;
    private String paymentId;
    private String payerID;
    private String eventLink;
}
