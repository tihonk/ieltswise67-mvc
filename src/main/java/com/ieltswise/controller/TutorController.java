package com.ieltswise.controller;

import com.ieltswise.entity.Event;
import com.ieltswise.entity.FreeAndBusyHoursOfTheDay;
import com.ieltswise.service.GoogleEventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @CrossOrigin(origins = "*")
    @GetMapping("/events/{tutorId}")
    public ResponseEntity<List<Event>> getEvents(@PathVariable("tutorId") String tutorId) {
        final List<Event> events = googleEventsService.getEvents(tutorId);
        final List<Event> futureEvents = events.stream().filter(ev -> ev.getEndDate().isAfter(now())).toList();
        return ResponseEntity.ok(futureEvents);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/events/{tutorId}/{year}/{month}")
    public ResponseEntity<List<FreeAndBusyHoursOfTheDay>> getEventsByYearAndMonth(
            @PathVariable String tutorId,
            @PathVariable int year,
            @PathVariable int month) {
        return ResponseEntity.ok(googleEventsService.getEventsByYearAndMonth(tutorId, year, month));
    }
}
