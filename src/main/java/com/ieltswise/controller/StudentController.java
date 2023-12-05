package com.ieltswise.controller;

import com.ieltswise.entity.BookingSessionData;
import com.ieltswise.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final BookingService calendarMailService;

    @Autowired
    StudentController(BookingService calendarMailService) {
        this.calendarMailService = calendarMailService;
    }

    @PostMapping(value = "/bookTrialSession", consumes={APPLICATION_JSON_VALUE})
    public String bookTrialSession(@RequestBody BookingSessionData sessionData) {
        return calendarMailService.bookTrialSession(sessionData);
    }

    @PostMapping(value = "/bookRegularSession", consumes={APPLICATION_JSON_VALUE})
    public String bookRegularSession(@RequestBody BookingSessionData sessionData) {
        return calendarMailService.bookRegularSession(sessionData);
    }
}
