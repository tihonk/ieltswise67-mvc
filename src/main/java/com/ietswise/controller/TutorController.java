package com.ietswise.controller;

import com.ietswise.entity.Event;
import com.ietswise.service.GoogleEventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.time.ZonedDateTime.now;

@RestController
@RequestMapping("/tutor")
public class TutorController {

    private final GoogleEventsService googleEventsService;

    @Autowired
    TutorController(GoogleEventsService googleEventsService) {
        this.googleEventsService = googleEventsService;
    }

    @GetMapping("/events/{tutorId}")
    public List<Event> getEvents(@PathVariable("tutorId") String tutorId) {
        final List<Event> events = googleEventsService.getEvents(tutorId);
        return events.stream().filter(ev -> ev.getEndDate().isAfter(now())).toList();
    }
}
