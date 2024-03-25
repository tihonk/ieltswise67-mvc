package com.ieltswise.controller;

import com.ieltswise.service.PayPalPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/payment")
public class PaypalController {

    private final PayPalPaymentService payPalService;

    @Value("${google.email.tutor}")
    private String tutorEmail;

    @Autowired
    public PaypalController(PayPalPaymentService payPalService) {
        this.payPalService = payPalService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/paymentLink")
    public ResponseEntity<String> getPaymentLink(@RequestParam("successUrl") String successUrl,
                                                 @RequestParam("cancelUrl") String cancelUrl,
                                                 // TODO:
                                                 //@RequestParam("tutorEmail") String tutorEmail,
                                                 @RequestParam("studentEmail") String studentEmail) {
        final String paymentLink = payPalService.preparePaymentLink(successUrl, cancelUrl, tutorEmail, studentEmail);
        if (paymentLink != null) {
            return ResponseEntity.ok(paymentLink);
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Failed to create payment link");
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPay() {
        return new ResponseEntity<>("Cancellation of payment", OK);
    }
}
