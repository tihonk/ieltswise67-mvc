package com.ieltswise.service;

import com.ieltswise.controller.request.RegularSessionDataRequest;
import com.ieltswise.controller.request.SessionDataRequest;
import com.ieltswise.controller.response.SessionDataResponse;
import com.ieltswise.exception.EmailNotFoundException;

public interface BookingService {

    /**
     * A method for booking a free trial lesson with a tutor in Google Calendar
     *
     * @param sessionDataRequest contains information about the participants and the time of the planned event
     * @return booking session data
     * @throws Exception if an error occurs during the booking process
     */
    SessionDataResponse bookTrialSession(final SessionDataRequest sessionDataRequest) throws Exception;

    /**
     * A method for booking a regular lesson with a tutor in Google Calendar
     *
     * @param regularSessionDataRequest an object containing data for requesting a regular lesson
     * @return booking session data
     * @throws Exception if an error occurs during the booking process
     */
    SessionDataResponse bookRegularSession(final RegularSessionDataRequest regularSessionDataRequest) throws Exception;

    /**
     * Extracts the number of available lessons for the user based on the email address.
     *
     * @param email the user's email address
     * @return the number of user lessons available
     * @throws EmailNotFoundException if the user with the specified email addresses is not registered
     */
    int getNumberOfAvailableLessons(String email) throws EmailNotFoundException;

    /**
     * Determines the availability of a trial session for a student
     *
     * @param studentEmail student's email
     * @return availability of a trial session
     */
    Boolean isTrialAvailable(String studentEmail);
}
