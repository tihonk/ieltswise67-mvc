package com.ieltswise.service;



import com.ieltswise.entity.Event;
import com.ieltswise.entity.FreeAndBusyHoursOfTheDay;

import java.util.List;

public interface GoogleEventsService {

    List<Event> getEvents(String tutorId);

    /**
     * Method for getting all free and busy hours of all days of the month
     * @param tutorId tutor's email
     * @param year year
     * @param month month number
     * @return List<FreeAndBusyHoursOfTheDay>
     */
    List<FreeAndBusyHoursOfTheDay> getEventsByYearAndMonth(String tutorId, int year, int month);
}
