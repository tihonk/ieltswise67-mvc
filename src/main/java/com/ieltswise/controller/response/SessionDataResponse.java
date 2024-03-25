package com.ieltswise.controller.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SessionDataResponse {

    private String studentEmail;
    private String sessionTime;
    private String eventLink;
    private String requestedService;
}
