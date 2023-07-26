package com.ietswise.controller;

import com.ietswise.entity.Event;
import com.ietswise.service.GoogleEventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.time.ZonedDateTime.now;

@RestController
@RequestMapping("/tutor")
public class TutorController {

    private GoogleEventsService googleEventsService;

    @Autowired
    TutorController(GoogleEventsService googleEventsService) {
        this.googleEventsService = googleEventsService;
    }

    @GetMapping("/events")
    public List<Event> getEvents() {
        List<Event> events = googleEventsService.getEvents("ieltswise67@gmail.com");
        return events.stream().filter(ev -> ev.getEndDate().isAfter(now())).toList();
    }
}
