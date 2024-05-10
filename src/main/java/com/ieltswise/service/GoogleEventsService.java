package com.ieltswise.service;

import com.ieltswise.controller.response.Event;
import com.ieltswise.dto.FreeAndBusyHoursOfTheDay;
import com.ieltswise.exception.EmailNotFoundException;
import com.ieltswise.exception.EventFetchingException;

import java.util.List;

public interface GoogleEventsService {

    /**
     * @param tutorId tutor's email address
     * @return all future scheduled events for the selected teacher
     * @throws EmailNotFoundException if the tutor with the specified email address is not registered
     * @throws EventFetchingException if an exception occurred when receiving events from the tutor's calendar
     */
    List<Event> getEvents(String tutorId) throws EmailNotFoundException, EventFetchingException;

    /**
     * A method for getting information about all available, unavailable and booked hours for all days of the month
     *
     * @param tutorId tutor's email
     * @param year    year
     * @param month   month number
     * @return a list of available time with a tutor for the requested month with an interval of 1 hour
     * @throws EmailNotFoundException if the tutor with the specified email address is not registered
     * @throws EventFetchingException if an exception occurred when receiving events from the tutor's
     */
    List<FreeAndBusyHoursOfTheDay> getEventsByYearAndMonth(String tutorId, int year, int month)
            throws EmailNotFoundException, EventFetchingException;
}
