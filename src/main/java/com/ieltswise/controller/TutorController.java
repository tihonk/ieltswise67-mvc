package com.ieltswise.controller;

import com.ieltswise.dto.PaymentCredentialsDto;
import com.ieltswise.entity.Event;
import com.ieltswise.entity.FreeAndBusyHoursOfTheDay;
import com.ieltswise.entity.PaymentCredentials;
import com.ieltswise.service.GoogleEventsService;
import com.ieltswise.service.PaymentCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.time.ZonedDateTime.now;

@RestController
@RequestMapping("/tutor")
public class TutorController {

    private final GoogleEventsService googleEventsService;
    private final PaymentCredentialService paymentCredentialService;

    @Autowired
    TutorController(GoogleEventsService googleEventsService, PaymentCredentialService paymentCredentialService) {
        this.googleEventsService = googleEventsService;
        this.paymentCredentialService = paymentCredentialService;
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

    @PostMapping("/payment")
    public ResponseEntity<?> savePaymentInformation(@RequestBody PaymentCredentialsDto paymentCredentialsDto) {
        try {
            PaymentCredentials paymentCredentials = paymentCredentialService.savePaymentInfo(paymentCredentialsDto);
            return ResponseEntity.ok(paymentCredentials);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to save comment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
