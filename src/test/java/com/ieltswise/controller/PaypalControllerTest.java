package com.ieltswise.controller;

import com.ieltswise.controller.impl.PaypalControllerImpl;
import com.ieltswise.exception.EmailNotFoundException;
import com.ieltswise.service.PayPalPaymentService;
import com.paypal.base.rest.PayPalRESTException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaypalControllerImpl.class)
public class PaypalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PayPalPaymentService payPalService;

    @Value("${google.email.tutor}")
    private String tutorEmail;

    String successUrl = "http://localhost:8080/payment/sucsses";
    String cancelUrl = "http://localhost:8080/payment/cancel";
    String studentEmail = "student@example.com";
    String expectedPaymentLink =
            "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-73788278942856907";

    @Test
    public void testGetPaymentLinkReturnPaymentLink() throws Exception {

        // When
        when(payPalService.preparePaymentLink(successUrl, cancelUrl, tutorEmail, studentEmail))
                .thenReturn(expectedPaymentLink);

        // Then
        mockMvc.perform(get("/payment/paymentLink")
                        .param("successUrl", successUrl)
                        .param("cancelUrl", cancelUrl)
                        .param("tutorEmail", tutorEmail)
                        .param("studentEmail", studentEmail))
                .andExpect(content().string(expectedPaymentLink))
                .andExpect(status().isOk());
        verify(payPalService, times(1))
                .preparePaymentLink(successUrl, cancelUrl, tutorEmail, studentEmail);
    }

    @Test
    public void testGetPaymentLinkThrowEmailNotFoundException() throws Exception {

        // When
        when(payPalService.preparePaymentLink(successUrl, cancelUrl, tutorEmail, studentEmail))
                .thenThrow(new EmailNotFoundException(String.format("Tutor with email %s not found", tutorEmail)));

        // Then
        mockMvc.perform(get("/payment/paymentLink")
                        .param("successUrl", successUrl)
                        .param("cancelUrl", cancelUrl)
                        .param("tutorEmail", tutorEmail)
                        .param("studentEmail", studentEmail))
                .andExpect(jsonPath("$.errorCode").value(5))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetPaymentLinkThrowPayPalRESTException() throws Exception {

        // When
        when(payPalService.preparePaymentLink(successUrl, cancelUrl, tutorEmail, studentEmail))
                .thenThrow(new PayPalRESTException("Payment has been done already for this cart."));

        // Then
        mockMvc.perform(get("/payment/paymentLink")
                        .param("successUrl", successUrl)
                        .param("cancelUrl", cancelUrl)
                        .param("tutorEmail", tutorEmail)
                        .param("studentEmail", studentEmail))
                .andExpect(jsonPath("$.errorCode").value(9))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testCancelPayReturnString() throws Exception {

        // Then
        mockMvc.perform(get("/payment/cancel"))
                .andExpect(content().string("Cancellation of payment"))
                .andExpect(status().isOk());
    }
}
