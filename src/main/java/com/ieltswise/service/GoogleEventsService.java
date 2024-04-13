package com.ieltswise.service;

import com.ieltswise.controller.response.Event;
import com.ieltswise.entity.FreeAndBusyHoursOfTheDay;

import java.util.List;

public interface GoogleEventsService {

    List<Event> getEvents(String tutorId);

    /**
     * A method for getting information about all available, unavailable and booked hours for all days of the month
     *
     * @param tutorId tutor's email
     * @param year    year
     * @param month   month number
     * @return List<FreeAndBusyHoursOfTheDay>
     * @throws Exception if there are problems when executing a request or processing data
     */
    List<FreeAndBusyHoursOfTheDay> getEventsByYearAndMonth(String tutorId, int year, int month) throws Exception;
}
