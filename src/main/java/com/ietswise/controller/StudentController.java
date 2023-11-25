package com.ietswise.controller;

import com.ietswise.entity.BookingSessionData;
import com.ietswise.service.BookingService;
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

    @PostMapping(value = "/booksession", consumes={APPLICATION_JSON_VALUE})
    public String bookSession(@RequestBody BookingSessionData sessionData) {
        return calendarMailService.bookSession(sessionData);
    }
}
