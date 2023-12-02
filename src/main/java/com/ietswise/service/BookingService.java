package com.ietswise.service;

import com.ietswise.entity.BookingSessionData;

public interface BookingService {
    /**
     * A method for booking a free trial lesson with a tutor in Google Calendar
     * @param sessionData contains information about the participants and the time of the planned event
     * @return link to the generated event in Google Calendar
     */
    String bookTrialSession(final BookingSessionData sessionData);

    /**
     * A method for booking a regular lesson with a tutor in Google Calendar
     * @param sessionData contains information about the participants and the time of the planned event
     * @return link to the generated event in Google Calendar
     */
    String bookRegularSession(final BookingSessionData sessionData);
}
