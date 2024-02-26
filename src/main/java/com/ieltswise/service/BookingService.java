package com.ieltswise.service;

import com.ieltswise.controller.request.SessionDataRequest;
import com.ieltswise.controller.response.SessionDataResponse;
import com.ieltswise.exception.BookingSessionException;

public interface BookingService {
    /**
     * A method for booking a free trial lesson with a tutor in Google Calendar
     * @param sessionData contains information about the participants and the time of the planned event
     * @return booking session data
     */
    SessionDataResponse bookTrialSession(final SessionDataRequest sessionData) throws BookingSessionException;

    /**
     * A method for booking a regular lesson with a tutor in Google Calendar
     * @param sessionData contains information about the participants and the time of the planned event
     * @return booking session data
     */
    SessionDataResponse bookRegularSession(final SessionDataRequest sessionData);

    /**
     * Extracts the number of available lessons for the user based on the email address.
     *
     * @param email the user's email address
     * @return the number of user lessons available
     */
    int getNumberOfAvailableLessons(String email);

    /**
     * Determines the availability of a trial session for a student
     * @param studentEmail student's email
     * @return availability of a trial session
     */
    Boolean isTrialAvailable(String studentEmail);
}
