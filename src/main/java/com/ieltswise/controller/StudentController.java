package com.ieltswise.controller;

import com.ieltswise.entity.BookingSessionData;
import com.ieltswise.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final BookingService calendarMailService;

    @Autowired
    StudentController(BookingService calendarMailService) {
        this.calendarMailService = calendarMailService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/bookTrialSession", consumes={APPLICATION_JSON_VALUE})
    public ResponseEntity<String> bookTrialSession(@RequestBody BookingSessionData sessionData) {
        return ResponseEntity.ok(calendarMailService.bookTrialSession(sessionData));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/bookRegularSession", consumes={APPLICATION_JSON_VALUE})
    public ResponseEntity<String> bookRegularSession(@RequestBody BookingSessionData sessionData) {
        return ResponseEntity.ok(calendarMailService.bookRegularSession(sessionData));
    }

    @GetMapping("/lessonCount/{email}")
    public ResponseEntity<String> getUserLessonCount(@PathVariable("email") String email) {
        int lessonCount = calendarMailService.getNumberOfAvailableLessons(email);
        return new ResponseEntity<>("{\"Number of lessons available\": \"" + lessonCount + "\"}", HttpStatus.OK);
    }
}
