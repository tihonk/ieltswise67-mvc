package com.ieltswise.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SessionDataRequest {
    private String tutorEmail;
    private String studentEmail;
    private String studentName;
    private String requestedService;
    private String startDate;
    private String endDate;
    private String paymentId;
    private String payerID;
    private String eventLink;
}
