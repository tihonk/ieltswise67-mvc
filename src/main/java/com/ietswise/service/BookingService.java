package com.ietswise.service;

import com.ietswise.entity.BookingSessionData;

public interface BookingService {

    /**
     *  Method for booking a lesson with a tutor in Google Calendar
     *  @param sessionData contains information about the members and time of the planned event
     *  @return link to the generated event in Google calendar
     */
    String bookSession(final BookingSessionData sessionData);

}
