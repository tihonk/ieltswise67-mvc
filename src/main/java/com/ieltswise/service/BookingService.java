package com.ieltswise.service;

import com.ieltswise.entity.BookingSessionData;

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

    /**
     * Determines the availability of a trial session for a student
     * @param studentEmail student's email
     * @return availability of a trial session
     */
    Boolean isTrialAvailable(String studentEmail);
}
