package com.ietswise.controller;

import com.ietswise.entity.BookingSessionData;
import com.ietswise.service.CalendarMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Value("${google.mail.test.tutor1.code}")
    private String tutorCode;
    @Value("${google.mail.test.tutor1.email}")
    private String tutorEmail;

    private final CalendarMailService calendarMailService;

    @Autowired
    StudentController(CalendarMailService calendarMailService) {
        this.calendarMailService = calendarMailService;
    }

    @GetMapping("/test")
    public String test() {
        return "Hello";
    }

    @PostMapping(value = "/booksession", consumes={APPLICATION_JSON_VALUE})
    public void bookSession(@RequestBody BookingSessionData sessionData) {
        sessionData.setTutorEmail(tutorEmail);
        sessionData.setTutorCode(tutorCode);
        calendarMailService.bookFreeTrailLesson(sessionData);
    }
}
