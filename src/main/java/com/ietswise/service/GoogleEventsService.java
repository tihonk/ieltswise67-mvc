package com.ietswise.service;


import com.ietswise.entity.Event;
import com.ietswise.entity.EventsByYearAndMonth;

import java.util.List;

public interface GoogleEventsService {

    List<Event> getEvents(String tutorId);

    List<EventsByYearAndMonth> getEventsByYearAndMonth(String tutorId, int year, int month);
}
