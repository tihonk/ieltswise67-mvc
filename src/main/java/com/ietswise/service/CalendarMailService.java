package com.ietswise.service;

import com.ietswise.entity.BookingSessionData;

public interface CalendarMailService {
    boolean bookFreeTrailLesson(BookingSessionData sessionData);
}
