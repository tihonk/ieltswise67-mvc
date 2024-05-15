package com.ieltswise.controller.impl;

import com.ieltswise.controller.PaypalController;
import com.ieltswise.exception.EmailNotFoundException;
import com.ieltswise.service.PayPalPaymentService;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaypalControllerImpl implements PaypalController {

    private final PayPalPaymentService payPalService;

    @Value("${google.email.tutor}")
    private String tutorEmail;

    @Autowired
    public PaypalControllerImpl(PayPalPaymentService payPalService) {
        this.payPalService = payPalService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/paymentLink")
    public ResponseEntity<String> getPaymentLink(@RequestParam("successUrl") String successUrl,
                                                 @RequestParam("cancelUrl") String cancelUrl,
                                                 // TODO:
                                                 //@RequestParam("tutorEmail") String tutorEmail,
                                                 @RequestParam("studentEmail") String studentEmail)
            throws EmailNotFoundException, PayPalRESTException {
        final String paymentLink = payPalService.preparePaymentLink(successUrl, cancelUrl, tutorEmail, studentEmail);
        return ResponseEntity.ok(paymentLink);
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPay() {
        return ResponseEntity.ok("Cancellation of payment");
    }
}
