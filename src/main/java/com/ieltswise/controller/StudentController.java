package com.ieltswise.controller;

import com.ieltswise.controller.request.RegularSessionDataRequest;
import com.ieltswise.controller.request.SessionDataRequest;
import com.ieltswise.controller.response.SessionDataResponse;
import com.ieltswise.exception.EmailNotFoundException;
import com.ieltswise.service.BookingService;
import com.ieltswise.service.PayPalPaymentService;
import com.paypal.api.payments.Payment;
import jakarta.validation.Valid;
import org.json.JSONObject;
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
    public ResponseEntity<SessionDataResponse> bookTrialSession(@RequestBody @Valid SessionDataRequest sessionData)
            throws Exception {
        return ResponseEntity.ok(calendarMailService.bookTrialSession(sessionData));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/bookRegularSession", consumes = {APPLICATION_JSON_VALUE})
    public ResponseEntity<SessionDataResponse> bookRegularSession(
            @RequestBody @Valid RegularSessionDataRequest regularSessionDataRequest) throws Exception {
        // TODO: Change tutorEmail to value from sessionData
        final Payment payment = payPalService.executePayment(regularSessionDataRequest.getPaymentId(),
                regularSessionDataRequest.getPayerID(), tutorEmail);
        if (payment.getState().equals("approved")) {
            return ResponseEntity.ok(calendarMailService.bookRegularSession(regularSessionDataRequest));
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    @GetMapping("/lessonCount/{email}")
    public ResponseEntity<String> getUserLessonCount(@PathVariable("email") String email)
            throws EmailNotFoundException {
        int lessonCount = calendarMailService.getNumberOfAvailableLessons(email);
        JSONObject jsonResponse = new JSONObject().put("Number of lessons available: ", lessonCount);
        return ResponseEntity.ok(jsonResponse.toString());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/trialavailability/{studentEmail}")
    public ResponseEntity<Boolean> isTrialAvailable(@PathVariable String studentEmail) {
        return ResponseEntity.ok(calendarMailService.isTrialAvailable(studentEmail));
    }
}
