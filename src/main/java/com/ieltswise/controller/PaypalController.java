package com.ieltswise.controller;

import com.ieltswise.service.PayPalPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public PaypalController(PayPalPaymentService payPalService) {
        this.payPalService = payPalService;
    }

    @GetMapping("/paymentLink")
    public ResponseEntity<String> getPaymentLink(@RequestParam("studentEmail") String studentEmail,
                                                 @RequestParam("tutorEmail") String tutorEmail,
                                                 @RequestParam("startDate") String startDate,
                                                 @RequestParam("endDate") String endDate) {
        final String successUrl = new StringBuilder()
                .append("?studentEmail=")
                .append(studentEmail)
                .append("&tutorEmail=")
                .append(tutorEmail)
                .append("&startDate=")
                .append(startDate)
                .append("&endDate=")
                .append(endDate).toString();
        final String paymentLink = payPalService.preparePaymentLink(studentEmail, successUrl);
        if (paymentLink != null) {
            return new ResponseEntity<>(paymentLink, OK);
        }
        return new ResponseEntity<>("Failed to create payment link", INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPay() {
        return new ResponseEntity<>("Cancellation of payment", OK);
    }
}
