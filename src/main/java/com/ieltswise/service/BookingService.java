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
     * Extracts the number of available lessons for the user based on the email address.
     *
     * @param email the user's email address
     * @return the number of user lessons available
     */
    int getNumberOfAvailableLessons(String email);
}
