package com.ieltswise.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationErrorCodes {

    FATAL_ERROR(1),
    EMAIL_NOT_FOUND_ERROR(5),
    BOOKING_SESSION_ERROR(7),
    PAYPAL_ERROR(9),
    ILLEGAL_ARGUMENT_ERROR(10),
    NO_AVAILABLE_LESSONS_ERROR(13),
    NO_PURCHASED_LESSONS_ERROR(15),
    METHOD_ARGUMENT_NOT_VALID_ERROR(17),
    TUTOR_CREATION_ERROR(19),
    EVENT_FETCHING_ERROR(20);

    private final int codeId;
}
