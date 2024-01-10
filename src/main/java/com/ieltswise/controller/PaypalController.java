package com.ieltswise.controller;

import com.ieltswise.service.PayPalPaymentService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class PaypalController {
    private final PayPalPaymentService service;

    @Autowired
    public PaypalController(PayPalPaymentService service) {
        this.service = service;
    }

    public static final String BASE_URL = "http://localhost:8080/";

    @PostMapping("/{quantity}/{email}")
    public ResponseEntity<String> payment(@PathVariable int quantity, @PathVariable String email) {
        try {
            Payment payment = service.createPayment(quantity, email, BASE_URL + "pay/cancel",
                    BASE_URL + "pay/success");

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    String approvalUrl = link.getHref();
                    return new ResponseEntity<>("{\"approval_url\": \"" + approvalUrl + "\"}", HttpStatus.OK);
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Failed to create payment", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/success")
    public ResponseEntity<String> successPay(@RequestParam("paymentId") String paymentId,
                                             @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());

            if (payment.getState().equals("approved")) {
                return new ResponseEntity<>("Payment was successful", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Payment failed", HttpStatus.BAD_REQUEST);
            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error processing payment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPay() {
        return new ResponseEntity<>("Cancellation of payment", HttpStatus.OK);
    }
}
