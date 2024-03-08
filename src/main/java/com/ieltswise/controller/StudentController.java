package com.ieltswise.controller;


import com.ieltswise.controller.request.SessionDataRequest;
import com.ieltswise.exception.BookingSessionException;
import com.ieltswise.service.BookingService;
import com.ieltswise.service.PayPalPaymentService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.PAYMENT_REQUIRED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final BookingService calendarMailService;

    private final PayPalPaymentService payPalService;

    @Value("${google.email.tutor}")
    private String tutorEmail;

    @Autowired
    StudentController(BookingService calendarMailService,
                      PayPalPaymentService payPalService) {
        this.calendarMailService = calendarMailService;
        this.payPalService = payPalService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/bookTrialSession", consumes = {APPLICATION_JSON_VALUE})
    public ResponseEntity<?> bookTrialSession(@RequestBody SessionDataRequest sessionData) {
        try {
            return ResponseEntity.ok(calendarMailService.bookTrialSession(sessionData));
        } catch (BookingSessionException e) {
            return ResponseEntity.status(PAYMENT_REQUIRED).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/bookRegularSession", consumes = {APPLICATION_JSON_VALUE})
    public ResponseEntity<?> bookRegularSession(@RequestBody SessionDataRequest sessionData) {
        try {
            // TODO: Change tutorEmail to value from sessionData
            final Payment payment = payPalService.executePayment(sessionData.getPaymentId(), sessionData.getPayerID(),
                    tutorEmail);
            if (payment.getState().equals("approved")) {
                return ResponseEntity.ok(calendarMailService.bookRegularSession(sessionData));
            } else {
                return ResponseEntity.status(BAD_REQUEST).build();
            }
        } catch (PayPalRESTException | IllegalArgumentException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/lessonCount/{email}")
    public ResponseEntity<String> getUserLessonCount(@PathVariable("email") String email) {
        int lessonCount = calendarMailService.getNumberOfAvailableLessons(email);
        return new ResponseEntity<>("{\"Number of lessons available\": \"" + lessonCount + "\"}", OK);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/trialavailability/{studentEmail}")
    public ResponseEntity<Boolean> isTrialAvailable(@PathVariable String studentEmail) {
        return ResponseEntity.ok(calendarMailService.isTrialAvailable(studentEmail));
    }
}
