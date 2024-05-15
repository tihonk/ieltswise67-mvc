package com.ieltswise.controller.impl;

import com.ieltswise.controller.TutorController;
import com.ieltswise.controller.request.PaymentCredentialsRequest;
import com.ieltswise.controller.request.ScheduleUpdateRequest;
import com.ieltswise.controller.request.TutorCreateRequest;
import com.ieltswise.controller.response.Event;
import com.ieltswise.dto.FreeAndBusyHoursOfTheDay;
import com.ieltswise.entity.PaymentCredentials;
import com.ieltswise.entity.Schedule;
import com.ieltswise.entity.TutorInfo;
import com.ieltswise.exception.EmailNotFoundException;
import com.ieltswise.exception.EventFetchingException;
import com.ieltswise.exception.TutorCreationException;
import com.ieltswise.service.GoogleEventsService;
import com.ieltswise.service.PaymentCredentialService;
import com.ieltswise.service.ScheduleService;
import com.ieltswise.service.TutorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.time.ZonedDateTime.now;

@RestController
@RequestMapping("/tutor")
public class TutorControllerImpl implements TutorController {

    private final GoogleEventsService googleEventsService;
    private final TutorInfoService tutorInfoService;
    private final ScheduleService scheduleService;
    private final PaymentCredentialService paymentCredentialService;

    @Autowired
    TutorControllerImpl(GoogleEventsService googleEventsService,
                        TutorInfoService tutorInfoService,
                        ScheduleService scheduleService,
                        PaymentCredentialService paymentCredentialService) {
        this.googleEventsService = googleEventsService;
        this.tutorInfoService = tutorInfoService;
        this.scheduleService = scheduleService;
        this.paymentCredentialService = paymentCredentialService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/events/{tutorId}")
    public ResponseEntity<List<Event>> getEvents(@PathVariable String tutorId)
            throws EmailNotFoundException, EventFetchingException {
        final List<Event> events = googleEventsService.getEvents(tutorId);
        final List<Event> futureEvents = events.stream().filter(ev -> ev.getEndDate().isAfter(now())).toList();
        return ResponseEntity.ok(futureEvents);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/events/{tutorId}/{year}/{month}")
    public ResponseEntity<List<FreeAndBusyHoursOfTheDay>> getEventsByYearAndMonth(@PathVariable String tutorId,
                                                                                  @PathVariable int year,
                                                                                  @PathVariable int month)
            throws EmailNotFoundException, EventFetchingException {
        return ResponseEntity.ok(googleEventsService.getEventsByYearAndMonth(tutorId, year, month));
    }

    @PostMapping()
    public ResponseEntity<TutorInfo> createTutor(@RequestBody TutorCreateRequest tutorCreateRequest)
            throws TutorCreationException {
        TutorInfo createdTutor = tutorInfoService.createTutor(tutorCreateRequest);
        return new ResponseEntity<>(createdTutor, HttpStatus.CREATED);
    }

    @GetMapping("/schedule/{tutorId}")
    public ResponseEntity<Schedule> schedule(@PathVariable String tutorId) throws EmailNotFoundException {
        Schedule schedule = scheduleService.getSchedulesTutor(tutorId);
        return ResponseEntity.ok(schedule);
    }

    @PutMapping("/schedule/{tutorId}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable String tutorId,
                                                   @RequestBody ScheduleUpdateRequest scheduleUpdateRequest)
            throws EmailNotFoundException {
        Schedule schedule = scheduleService.updateSchedule(tutorId, scheduleUpdateRequest.getUpdatedTimeInfo());
        return ResponseEntity.ok(schedule);
    }

    @PutMapping("/payment")
    public ResponseEntity<PaymentCredentials> updatePaymentInformation(
            @RequestBody PaymentCredentialsRequest paymentCredentialsRequest) throws EmailNotFoundException {
        PaymentCredentials paymentCredentials = paymentCredentialService.updatePaymentInfo(paymentCredentialsRequest);
        return ResponseEntity.ok(paymentCredentials);
    }
}
