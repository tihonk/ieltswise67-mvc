package com.ietswise.service;



import com.ietswise.entity.Event;

import java.util.List;

public interface GoogleEventsService {

    List<Event> getEvents(String tutorId);
}
