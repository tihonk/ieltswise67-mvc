package com.ieltswise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ieltswise.controller.request.PaymentCredentialsRequest;
import com.ieltswise.controller.request.ScheduleUpdateRequest;
import com.ieltswise.controller.request.TutorCreateRequest;
import com.ieltswise.controller.response.Event;
import com.ieltswise.dto.FreeAndBusyHoursOfTheDay;
import com.ieltswise.dto.TimeSlot;
import com.ieltswise.entity.PaymentCredentials;
import com.ieltswise.entity.Schedule;
import com.ieltswise.entity.TutorInfo;
import com.ieltswise.service.GoogleEventsService;
import com.ieltswise.service.PaymentCredentialService;
import com.ieltswise.service.ScheduleService;
import com.ieltswise.service.TutorInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ieltswise.enums.Status.AVAILABLE;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TutorController.class)
public class TutorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GoogleEventsService googleEventsService;
    @MockBean
    private TutorInfoService tutorInfoService;
    @MockBean
    private ScheduleService scheduleService;
    @MockBean
    private PaymentCredentialService paymentCredentialService;

    String tutorEmail = "test.tutor1.ieltswise67@gmail.com";

    @Test
    public void testGetEventsReturnEvents() throws Exception {

        // Given
        Event pastEvent = Event.builder()
                .startDate(ZonedDateTime.parse("2023-06-05T02:00:00+03:00"))
                .endDate(ZonedDateTime.parse("2023-06-05T02:59:00+03:00"))
                .status("confirmed")
                .build();

        Event fetureEvent = Event.builder()
                .startDate(ZonedDateTime.parse("2030-01-05T02:00:00+03:00"))
                .endDate(ZonedDateTime.parse("2030-01-05T02:59:00+03:00"))
                .status("confirmed")
                .build();

        List<Event> events = new ArrayList<>();
        events.add(pastEvent);
        events.add(fetureEvent);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

        // When
        when(googleEventsService.getEvents(anyString())).thenReturn(events);

        // Then
        mockMvc.perform(get("/tutor/events/{tutorId}", tutorEmail))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].startDate").value(fetureEvent.getStartDate().format(formatter)))
                .andExpect(jsonPath("$[0].endDate").value(fetureEvent.getEndDate().format(formatter)))
                .andExpect(jsonPath("$[0].status").value(fetureEvent.getStatus()))
                .andExpect(status().isOk());
        verify(googleEventsService, times(1)).getEvents(anyString());
    }

    @Test
    public void testGetEventsByYearAndMonthReturnFreeAndBusyHoursOfTheDayList() throws Exception {

        // Given
        Map<String, Object> hourStatus = new HashMap<>();
        hourStatus.put("time", 1722470400000L);
        hourStatus.put("status", AVAILABLE.name());

        List<Map<String, Object>> time = Collections.singletonList(hourStatus);

        FreeAndBusyHoursOfTheDay freeAndBusyHoursOfTheDay = FreeAndBusyHoursOfTheDay.builder()
                .date(1722470400000L)
                .time(time)
                .build();

        List<FreeAndBusyHoursOfTheDay> freeAndBusyHoursOfTheDayList =
                Collections.singletonList(freeAndBusyHoursOfTheDay);

        // When
        when(googleEventsService.getEventsByYearAndMonth(anyString(), anyInt(), anyInt()))
                .thenReturn(freeAndBusyHoursOfTheDayList);

        // Then
        mockMvc.perform(get("/tutor/events/{tutorId}/{year}/{month}", tutorEmail, 2024, 8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(freeAndBusyHoursOfTheDayList.size())))
                .andExpect(jsonPath("$[0].date").value(freeAndBusyHoursOfTheDay.getDate()))
                .andExpect(jsonPath("$[0].time[0]").value(hourStatus))
                .andExpect(status().isOk());
        verify(googleEventsService, times(1))
                .getEventsByYearAndMonth(anyString(), anyInt(), anyInt());
    }

    private List<TimeSlot> createTimeSlots() {
        TimeSlot timeSlot = TimeSlot.builder()
                .time("12.00")
                .engaged(true)
                .build();
        return Collections.singletonList(timeSlot);
    }

    private Map<DayOfWeek, List<TimeSlot>> createdTimeInfo() {
        Map<DayOfWeek, List<TimeSlot>> timeInfo = new HashMap<>();
        timeInfo.put(DayOfWeek.MONDAY, createTimeSlots());
        return timeInfo;
    }

    private Schedule createSchedule() {
        return Schedule.builder()
                .id(1L)
                .timeInfo(createdTimeInfo())
                .build();
    }

    @Test
    public void testCreateTutorReturnCreated() throws Exception {

        // Given
        TutorInfo createdTutor = TutorInfo.builder()
                .id(1L)
                .email(tutorEmail)
                .name("Neil")
                .created(1715197655L)
                .schedule(createSchedule())
                .build();

        TutorCreateRequest tutorCreateRequest = TutorCreateRequest.builder()
                .email(tutorEmail)
                .name("Neil")
                .updatedTimeInfo(createdTimeInfo())
                .clientId("AcTtU8_JdRJdA2B6eDsAsVj896ZkFM34F0xsyJFz6HJwZtIw4MM")
                .clientSecret("EDVanIUr5KRtKq_Sgh7tkQg_1Q_wIv51n_kO2lOpp")
                .build();

        // When
        when(tutorInfoService.createTutor(isA(TutorCreateRequest.class))).thenReturn(createdTutor);

        // Then
        mockMvc.perform(post("/tutor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tutorCreateRequest)))
                .andExpect(jsonPath("$.id").value(createdTutor.getId()))
                .andExpect(jsonPath("$.email").value(createdTutor.getEmail()))
                .andExpect(jsonPath("$.name").value(createdTutor.getName()))
                .andExpect(jsonPath("$.created").value(createdTutor.getCreated()))
                .andExpect(jsonPath("$.schedule").value(createdTutor.getSchedule()))
                .andExpect(status().isCreated());
        verify(tutorInfoService, times(1)).createTutor(isA(TutorCreateRequest.class));
    }

    @Test
    public void testScheduleReturnSchedule() throws Exception {

        // When
        when(scheduleService.getSchedulesTutor(anyString())).thenReturn(createSchedule());

        // Then
        mockMvc.perform(get("/tutor/schedule/{tutorId}", tutorEmail))
                .andExpect(jsonPath("$.id").value(createSchedule().getId()))
                .andExpect(jsonPath("$.timeInfo.MONDAY[0].time").value("12.00"))
                .andExpect(jsonPath("$.timeInfo.MONDAY[0].engaged").value(true))
                .andExpect(status().isOk());
        verify(scheduleService, times(1)).getSchedulesTutor(anyString());
    }

    @Test
    public void testUpdateScheduleReturnUpdatedSchedule() throws Exception {

        // Given
        ScheduleUpdateRequest scheduleUpdateRequest = ScheduleUpdateRequest.builder()
                .updatedTimeInfo(createdTimeInfo())
                .build();

        // When
        when(scheduleService.updateSchedule(anyString(), anyMap())).thenReturn(createSchedule());

        // Then
        mockMvc.perform(put("/tutor/schedule/{tutorId}", tutorEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleUpdateRequest)))
                .andExpect(jsonPath("$.id").value(createSchedule().getId()))
                .andExpect(jsonPath("$.timeInfo.MONDAY[0].time").value("12.00"))
                .andExpect(jsonPath("$.timeInfo.MONDAY[0].engaged").value(true))
                .andExpect(status().isOk());
        verify(scheduleService, times(1)).updateSchedule(anyString(), anyMap());
    }

    @Test
    public void testUpdatePaymentInformationReturnUpdated() throws Exception {

        // Given
        PaymentCredentials paymentCredentials = PaymentCredentials.builder()
                .id(1L)
                .clientId("AcTtU8_JdRJdA2B6eDsAsVj896ZkFM34F0xsyJFz6HJwZtIw4MM")
                .clientSecret("EDVanIUr5KRtKq_Sgh7tkQg_1Q_wIv51n_kO2lOpp")
                .build();

        PaymentCredentialsRequest paymentCredentialsRequest = PaymentCredentialsRequest.builder()
                .tutorEmail(tutorEmail)
                .clientId("AcTtU8_JdRJdA2B6eDsAsVj896ZkFM34F0xsyJFz6HJwZtIw4MM")
                .clientSecret("EDVanIUr5KRtKq_Sgh7tkQg_1Q_wIv51n_kO2lOpp")
                .build();

        // When
        when(paymentCredentialService.updatePaymentInfo(isA(PaymentCredentialsRequest.class)))
                .thenReturn(paymentCredentials);

        // Then
        mockMvc.perform(put("/tutor/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentCredentialsRequest)))
                .andExpect(jsonPath("$.id").value(paymentCredentials.getId()))
                .andExpect(jsonPath("$.clientId").value(paymentCredentialsRequest.getClientId()))
                .andExpect(jsonPath("$.clientSecret").value(paymentCredentialsRequest.getClientSecret()))
                .andExpect(status().isOk());
    }
}
