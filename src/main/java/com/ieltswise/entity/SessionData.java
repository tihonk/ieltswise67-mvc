package com.ieltswise.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SessionData {

    private String studentEmail;
    private String sessionTime;
    private String eventLink;
}
