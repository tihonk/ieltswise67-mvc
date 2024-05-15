package com.ieltswise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ieltswise.controller.impl.StudentControllerImpl;
import com.ieltswise.controller.request.RegularSessionDataRequest;
import com.ieltswise.controller.request.SessionDataRequest;
import com.ieltswise.controller.response.SessionDataResponse;
import com.ieltswise.exception.BookingSessionException;
import com.ieltswise.service.BookingService;
import com.ieltswise.service.PayPalPaymentService;
import com.paypal.api.payments.Payment;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentControllerImpl.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private BookingService calendarMailService;
    @MockBean
    private PayPalPaymentService payPalService;

    String email = "student@example.com";

    SessionDataRequest sessionDataRequest = SessionDataRequest.builder()
            .tutorEmail("test.tutor1.ieltswise67@gmail.com")
            .studentEmail(email)
            .studentName("Bob")
            .requestedService("Business English")
            .startDate("2023-12-07T12:00:00+01:00")
            .endDate("2023-12-07T13:00:00+01:00")
            .build();

    SessionDataResponse sessionDataResponse = SessionDataResponse.builder()
            .studentEmail(email)
            .sessionTime("2023-12-07T12:00:00+01:00")
            .eventLink("https://www.google.com/calendar/event?eid=MzdvMWk0bXQzcTI3OXAwNjdjdjBqMDhjbTggdm9sa29ub3Z" +
                    "za2lqX21yXzIxQG1mLmDyc3UuYnk")
            .requestedService("Business English")
            .build();

    @Test
    public void testBookTrialSessionReturnTrialSession() throws Exception {

        // When
        when(calendarMailService.bookTrialSession(isA(SessionDataRequest.class))).thenReturn(sessionDataResponse);

        // Then
        mockMvc.perform(post("/student/bookTrialSession")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDataRequest)))
                .andExpect(jsonPath("$.studentEmail").value(sessionDataResponse.getStudentEmail()))
                .andExpect(jsonPath("$.sessionTime").value(sessionDataResponse.getSessionTime()))
                .andExpect(jsonPath("$.eventLink").value(sessionDataResponse.getEventLink()))
                .andExpect(jsonPath("$.requestedService").value(sessionDataResponse.getRequestedService()))
                .andExpect(status().isOk());
        verify(calendarMailService, times(1)).bookTrialSession(isA(SessionDataRequest.class));
    }

    @Test
    public void testBookTrialSessionThrowBookingSessionException() throws Exception{

        // When
        when(calendarMailService.bookTrialSession(isA(SessionDataRequest.class))).thenThrow(
                new BookingSessionException(String.format("Already used a trial lesson for email: %s", email)));

        // Then
        mockMvc.perform(post("/student/bookTrialSession")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDataRequest)))
                .andExpect(jsonPath("$.errorCode").value(7))
                .andExpect(status().isConflict());
    }

    @Test
    public void testBookRegularSessionReturnTrialSession() throws Exception {

        // Given
        Payment payment = new Payment();
        payment.setState("approved");

        // When
        when(payPalService.executePayment(anyString(), anyString(), anyString())).thenReturn(payment);
        when(calendarMailService.bookRegularSession(isA(RegularSessionDataRequest.class)))
                .thenReturn(sessionDataResponse);

        // Then
        mockMvc.perform(post("/student/bookRegularSession")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRegularSessionDataRequest())))
                .andExpect(jsonPath("$.studentEmail").value(sessionDataResponse.getStudentEmail()))
                .andExpect(jsonPath("$.sessionTime").value(sessionDataResponse.getSessionTime()))
                .andExpect(jsonPath("$.eventLink").value(sessionDataResponse.getEventLink()))
                .andExpect(jsonPath("$.requestedService").value(sessionDataResponse.getRequestedService()))
                .andExpect(status().isOk());
        verify(payPalService, times(1)).executePayment(anyString(), anyString(), anyString());
        verify(calendarMailService, times(1)).bookRegularSession(
                isA(RegularSessionDataRequest.class));
    }

    @Test
    public void testBookRegularSessionReturnBadRequest() throws Exception {

        // Given
        Payment failedPayment = new Payment();
        failedPayment.setState("failed");

        // When
        when(payPalService.executePayment(anyString(), anyString(), anyString())).thenReturn(failedPayment);

        // Then
        mockMvc.perform(post("/student/bookRegularSession")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRegularSessionDataRequest())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUserLessonCountReturnUserLessonCount() throws Exception {

        // Given
        JSONObject jsonResponse = new JSONObject().put("Number of lessons available: ", 3);

        // When
        when(calendarMailService.getNumberOfAvailableLessons(email)).thenReturn(3);

        // Then
        mockMvc.perform(get("/student/lessonCount/{email}", email))
                .andExpect(content().string(jsonResponse.toString()))
                .andExpect(status().isOk());
        verify(calendarMailService, times(1)).getNumberOfAvailableLessons(anyString());
    }

    @Test
    public void testIsTrialAvailableReturnBoolean() throws Exception {

        // When
        when((calendarMailService.isTrialAvailable(email))).thenReturn(true);

        // Then
        mockMvc.perform(get("/student/trialavailability/{studentEmail}", email))
                .andExpect(content().string("true"))
                .andExpect(status().isOk());
        verify(calendarMailService, times(1)).isTrialAvailable(email);
    }

    private RegularSessionDataRequest createRegularSessionDataRequest() {
        RegularSessionDataRequest regularSessionDataRequest;
        regularSessionDataRequest = new RegularSessionDataRequest();
        regularSessionDataRequest.setTutorEmail("test.tutor1.ieltswise67@gmail.com");
        regularSessionDataRequest.setStudentEmail(email);
        regularSessionDataRequest.setStudentName("Bob");
        regularSessionDataRequest.setRequestedService("Business English");
        regularSessionDataRequest.setStartDate("2023-12-07T12:00:00+01:00");
        regularSessionDataRequest.setEndDate("2023-12-07T13:00:00+01:00");
        regularSessionDataRequest.setPaymentId("PAYID-MYFOPQQ95684077M72601402");
        regularSessionDataRequest.setPayerID("BZBP4WVQ2PNJ2");
        return regularSessionDataRequest;
    }
}
