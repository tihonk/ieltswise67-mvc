package com.ieltswise.controller;

import com.ieltswise.entity.BookingSessionData;
import com.ieltswise.service.BookingService;
import com.ieltswise.service.PayPalPaymentService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final BookingService calendarMailService;

    private final PayPalPaymentService payPalService;

    @Autowired
    StudentController(BookingService calendarMailService,
                      PayPalPaymentService payPalService) {
        this.calendarMailService = calendarMailService;
        this.payPalService = payPalService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/bookTrialSession", consumes={APPLICATION_JSON_VALUE})
    public ResponseEntity<String> bookTrialSession(@RequestBody BookingSessionData sessionData) {
        return ResponseEntity.ok(calendarMailService.bookTrialSession(sessionData));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/bookRegularSession", consumes={APPLICATION_JSON_VALUE})
    public ResponseEntity<String> bookRegularSession(@RequestBody BookingSessionData sessionData) {
        try {
            final Payment payment = payPalService.executePayment(sessionData.getPaymentId(), sessionData.getPayerID());
            if (payment.getState().equals("approved")) {
                return new ResponseEntity<>(calendarMailService.bookRegularSession(sessionData), OK);
            } else {
                return new ResponseEntity<>("Payment failed", BAD_REQUEST);
            }
        } catch (PayPalRESTException e) {
            return new ResponseEntity<>("Error processing payment", HttpStatus.INTERNAL_SERVER_ERROR);
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
