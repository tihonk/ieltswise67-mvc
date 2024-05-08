package com.ieltswise.service.impl;

import com.ieltswise.dto.FreeAndBusyHoursOfTheDay;
import com.ieltswise.dto.TimeSlot;
import com.ieltswise.service.ScheduleService;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.DayOfWeek;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.FilesPath.BOOKED_MONDAY;
import static util.FilesPath.EVENTS_GMT;
import static util.FilesPath.EVENTS_GMT_MINUS_4;
import static util.FilesPath.EVENTS_GMT_PLUS_3;
import static util.FilesPath.FREE_SCHEDULE;
import static util.FilesPath.GMT_AND_FREE_SCHEDULE;
import static util.FilesPath.GMT_AND_NOT_FREE_SCHEDULE;
import static util.FilesPath.GMT_MINUS_4_AND_FREE_SCHEDULE;
import static util.FilesPath.GMT_MINUS_4_AND_NOT_FREE_SCHEDULE;
import static util.FilesPath.GMT_PLUS_3_AND_FREE_SCHEDULE;
import static util.FilesPath.GMT_PLUS_3_AND_NOT_FREE_SCHEDULE;
import static util.FilesPath.WITHOUT_EVENTS;
import static util.JsonDataReader.loadEvents;
import static util.JsonDataReader.loadFreeAndBusyHoursFromFile;
import static util.JsonDataReader.loadScheduleFromFile;

@ExtendWith(MockitoExtension.class)
public class GoogleEventsServiceImplTest {

    @InjectMocks
    private GoogleEventsServiceImpl googleEventsService;
    @Mock
    private ScheduleService scheduleService;

    private Method method;
    private JSONArray jsonArray;
    private ZonedDateTime startOfMonth;
    private ZonedDateTime endOfMonth;
    private Map<DayOfWeek, List<TimeSlot>> schedule;

    @BeforeEach
    void setUp() throws NoSuchMethodException {

        method = GoogleEventsServiceImpl.class.getDeclaredMethod("findAllEventsByYearAndMonth",
                JSONArray.class, ZonedDateTime.class, ZonedDateTime.class, Map.class);
        method.setAccessible(true);

        jsonArray = new JSONArray();
    }

    @Test
    public void FindAllEventsByYearAndMonth_WhenNoEvents()
            throws InvocationTargetException, IllegalAccessException {

        // Given
        startOfMonth = ZonedDateTime.of(2025, 3, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        schedule = loadScheduleFromFile(FREE_SCHEDULE);

        // When
        @SuppressWarnings("unchecked")
        List<FreeAndBusyHoursOfTheDay> result = (List<FreeAndBusyHoursOfTheDay>) method.invoke(googleEventsService,
                jsonArray, startOfMonth, endOfMonth, schedule);

        // Then
        assertEquals(31, result.size());
        assertEquals(loadFreeAndBusyHoursFromFile(WITHOUT_EVENTS), result);
    }

    @Test
    public void FindAllEventsByYearAndMonth_GmtPlus3_FreeSchedule()
            throws InvocationTargetException, IllegalAccessException, JSONException {

        // Given
        jsonArray = loadEvents(EVENTS_GMT_PLUS_3);
        startOfMonth = ZonedDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        schedule = loadScheduleFromFile(FREE_SCHEDULE);

        // When
        @SuppressWarnings("unchecked")
        List<FreeAndBusyHoursOfTheDay> result = (List<FreeAndBusyHoursOfTheDay>) method.invoke(googleEventsService,
                jsonArray, startOfMonth, endOfMonth, schedule);

        // Then
        assertEquals(31, result.size());
        assertEquals(loadFreeAndBusyHoursFromFile(GMT_PLUS_3_AND_FREE_SCHEDULE), result);
    }

    @Test
    public void FindAllEventsByYearAndMonth_GmtPlus3_NotFreeSchedule()
            throws InvocationTargetException, IllegalAccessException, JSONException {

        // Given
        jsonArray = loadEvents(EVENTS_GMT_PLUS_3);
        startOfMonth = ZonedDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        schedule = loadScheduleFromFile(BOOKED_MONDAY);

        // When
        @SuppressWarnings("unchecked")
        List<FreeAndBusyHoursOfTheDay> result = (List<FreeAndBusyHoursOfTheDay>) method.invoke(googleEventsService,
                jsonArray, startOfMonth, endOfMonth, schedule);

        // Then
        assertEquals(31, result.size());
        assertEquals(loadFreeAndBusyHoursFromFile(GMT_PLUS_3_AND_NOT_FREE_SCHEDULE), result);
    }

    @Test
    public void FindAllEventsByYearAndMonth_Gmt_FreeSchedule()
            throws InvocationTargetException, IllegalAccessException, JSONException {

        // Given
        jsonArray = loadEvents(EVENTS_GMT);
        startOfMonth = ZonedDateTime.of(2025, 2, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        schedule = loadScheduleFromFile(FREE_SCHEDULE);

        // When
        @SuppressWarnings("unchecked")
        List<FreeAndBusyHoursOfTheDay> result = (List<FreeAndBusyHoursOfTheDay>) method.invoke(googleEventsService,
                jsonArray, startOfMonth, endOfMonth, schedule);

        // Then
        assertEquals(28, result.size());
        assertEquals(loadFreeAndBusyHoursFromFile(GMT_AND_FREE_SCHEDULE), result);
    }

    @Test
    public void FindAllEventsByYearAndMonth_Gmt_NotFreeSchedule()
            throws InvocationTargetException, IllegalAccessException, JSONException {

        // Given
        jsonArray = loadEvents(EVENTS_GMT);
        startOfMonth = ZonedDateTime.of(2025, 2, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        schedule = loadScheduleFromFile(BOOKED_MONDAY);

        // When
        @SuppressWarnings("unchecked")
        List<FreeAndBusyHoursOfTheDay> result = (List<FreeAndBusyHoursOfTheDay>) method.invoke(googleEventsService,
                jsonArray, startOfMonth, endOfMonth, schedule);

        // Then
        assertEquals(28, result.size());
        assertEquals(loadFreeAndBusyHoursFromFile(GMT_AND_NOT_FREE_SCHEDULE), result);
    }

    @Test
    public void FindAllEventsByYearAndMonth_GmtMinus4_FreeSchedule()
            throws InvocationTargetException, IllegalAccessException, JSONException {

        // Given
        jsonArray = loadEvents(EVENTS_GMT_MINUS_4);
        startOfMonth = ZonedDateTime.of(2025, 4, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        schedule = loadScheduleFromFile(FREE_SCHEDULE);

        // When
        @SuppressWarnings("unchecked")
        List<FreeAndBusyHoursOfTheDay> result = (List<FreeAndBusyHoursOfTheDay>) method.invoke(googleEventsService,
                jsonArray, startOfMonth, endOfMonth, schedule);

        // Then
        assertEquals(30, result.size());
        assertEquals(loadFreeAndBusyHoursFromFile(GMT_MINUS_4_AND_FREE_SCHEDULE), result);
    }

    @Test
    public void FindAllEventsByYearAndMonth_GmtMinus4_NotFreeSchedule()
            throws InvocationTargetException, IllegalAccessException, JSONException {

        // Given
        jsonArray = loadEvents(EVENTS_GMT_MINUS_4);
        startOfMonth = ZonedDateTime.of(2025, 4, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        schedule = loadScheduleFromFile(BOOKED_MONDAY);

        // When
        @SuppressWarnings("unchecked")
        List<FreeAndBusyHoursOfTheDay> result = (List<FreeAndBusyHoursOfTheDay>) method.invoke(googleEventsService,
                jsonArray, startOfMonth, endOfMonth, schedule);

        // Then
        assertEquals(30, result.size());
        assertEquals(loadFreeAndBusyHoursFromFile(GMT_MINUS_4_AND_NOT_FREE_SCHEDULE), result);
    }
}
