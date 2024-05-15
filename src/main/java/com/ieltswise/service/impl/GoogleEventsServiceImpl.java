package com.ieltswise.service.impl;

import com.ieltswise.controller.response.Event;
import com.ieltswise.dto.FreeAndBusyHoursOfTheDay;
import com.ieltswise.dto.TimeSlot;
import com.ieltswise.dto.TimeStatus;
import com.ieltswise.enums.Status;
import com.ieltswise.exception.EmailNotFoundException;
import com.ieltswise.exception.EventFetchingException;
import com.ieltswise.repository.TutorInfoRepository;
import com.ieltswise.service.GoogleEventsService;
import com.ieltswise.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static java.time.YearMonth.of;
import static java.time.ZonedDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.util.Locale.ROOT;
import static org.apache.http.protocol.HTTP.USER_AGENT;

@Slf4j
@Service
public class GoogleEventsServiceImpl implements GoogleEventsService {

    private static final String ITEMS = "items";
    private static final String JSON_START = "start";
    private static final String JSON_END = "end";
    private static final String JSON_STATUS = "status";
    private static final String JSON_DATETIME = "dateTime";
    private static final String JSON_DATE = "date";
    private static final String STATUS_CANCELED = "cancelled";

    @Value("${google.credentials.key}")
    private String googleCredentialKey;

    private final ScheduleService scheduleService;
    private final TutorInfoRepository tutorInfoRepository;

    @Autowired
    public GoogleEventsServiceImpl(ScheduleService scheduleService, TutorInfoRepository tutorInfoRepository) {
        this.scheduleService = scheduleService;
        this.tutorInfoRepository = tutorInfoRepository;
    }

    @Override
    public List<Event> getEvents(String tutorID) throws EmailNotFoundException, EventFetchingException {
        isTutorRegistered(tutorID);
        try {
            URL obj = new URL("https://www.googleapis.com/calendar/v3/calendars/" + tutorID
                    + "/events?key=" + googleCredentialKey);
            return extractEvents(createJSONObjectResponse(obj).getJSONArray(ITEMS));
        } catch (IOException e) {
            log.error("Failed to fetch events for tutor ID: {}", tutorID, e);
            throw new EventFetchingException(e.getMessage());
        }
    }

    private void isTutorRegistered(String email) throws EmailNotFoundException {
        tutorInfoRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException("Tutor", email));
    }

    private List<Event> extractEvents(JSONArray eventItems) {
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < eventItems.length(); i++) {
            JSONObject eventItem = eventItems.getJSONObject(i);
            if (!eventItem.getString(JSON_STATUS).equals(STATUS_CANCELED)) {

                JSONObject start = eventItem.getJSONObject(JSON_START);
                JSONObject end = eventItem.getJSONObject(JSON_END);

                Event event = new Event();
                event.setStartDate(extractDate(start));
                event.setEndDate(extractDate(end));
                event.setStatus(eventItem.getString(JSON_STATUS));
                events.add(event);
            }
        }
        return events;
    }

    private ZonedDateTime extractDate(JSONObject dateTime) {
        if (dateTime.toMap().get(JSON_DATETIME) != null) {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .append(ISO_DATE_TIME)
                    .toFormatter(ROOT);
            return parse(dateTime.getString(JSON_DATETIME), formatter);
        } else if (dateTime.toMap().get(JSON_DATE) != null) {
            LocalDate localDate = LocalDate.parse(dateTime.getString(JSON_DATE));
            return localDate.atStartOfDay(ZoneId.of("UTC"));
        }
        return null;
    }

    @Override
    public List<FreeAndBusyHoursOfTheDay> getEventsByYearAndMonth(String tutorId, int year, int month)
            throws EmailNotFoundException, EventFetchingException {

        isTutorRegistered(tutorId);
        Map<DayOfWeek, List<TimeSlot>> schedule = scheduleService.getSchedulesTutor(tutorId).getTimeInfo();

        try {
            ZonedDateTime startOfMonth = of(year, month).atDay(1).atStartOfDay(ZoneId.of("UTC"));
            ZonedDateTime endOfMonth = of(year, month).atEndOfMonth().atStartOfDay(ZoneId.of("UTC"));

            URL url = new URL(createUrl(tutorId, startOfMonth, endOfMonth));
            return findAllEventsByYearAndMonth(createJSONObjectResponse(url).getJSONArray(ITEMS), startOfMonth,
                    endOfMonth, schedule);
        } catch (IOException e) {
            log.error("Failed to fetch events for tutor ID: {}, year: {}, month: {}", tutorId, year, month, e);
            throw new EventFetchingException(e.getMessage());
        }
    }

    private JSONObject createJSONObjectResponse(URL obj) throws IOException {
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return new JSONObject(response.toString());
    }

    private String createUrl(String tutorId, ZonedDateTime startOfMonth, ZonedDateTime endOfMonth) {
        String formattedTimeMin = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(startOfMonth);
        String formattedTimeMax = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(endOfMonth);
        String encodedTimeMin = URLEncoder.encode(formattedTimeMin, StandardCharsets.UTF_8);
        String encodedTimeMax = URLEncoder.encode(formattedTimeMax, StandardCharsets.UTF_8);
        String apiUrl = "https://www.googleapis.com/calendar/v3/calendars/" + tutorId + "/events";
        return apiUrl + "?timeMin=" + encodedTimeMin + "&timeMax=" + encodedTimeMax + "&key=" + googleCredentialKey;
    }

    private List<FreeAndBusyHoursOfTheDay> findAllEventsByYearAndMonth(JSONArray eventsArray,
                                                                       ZonedDateTime startOfMonth,
                                                                       ZonedDateTime endOfMonth,
                                                                       Map<DayOfWeek, List<TimeSlot>> schedule) {

        TreeMap<Long, TreeMap<Long, Status>> dateClockStatus = new TreeMap<>();
        TreeMap<Long, Status> hourStatus;

        for (int i = 0; i < eventsArray.length(); i++) {

            JSONObject eventItem = eventsArray.getJSONObject(i);

            if (!eventItem.getString(JSON_STATUS).equals(STATUS_CANCELED)) {

                JSONObject start = eventItem.getJSONObject(JSON_START);
                JSONObject end = eventItem.getJSONObject(JSON_END);

                ZonedDateTime eventStartDate = extractDate(start);
                ZonedDateTime eventEndDate = extractDate(end);

                TreeMap<Long, TreeMap<Long, Status>> temporarily = new TreeMap<>();

                ZonedDateTime eventUtcStartDate = Objects.requireNonNull(eventStartDate)
                        .withZoneSameInstant(ZoneOffset.UTC);
                ZonedDateTime eventUtcEndDate = Objects.requireNonNull(eventEndDate)
                        .withZoneSameInstant(ZoneOffset.UTC);

                Instant instant = eventUtcStartDate.toLocalDate().atStartOfDay(ZoneOffset.UTC).toInstant();
                long day = instant.toEpochMilli();

                hourStatus = new TreeMap<>();

                ZonedDateTime check = eventUtcStartDate.toLocalDate().atStartOfDay().atZone(eventUtcStartDate.getZone())
                        .plusDays(1).minusSeconds(1);

                while (eventUtcStartDate.isBefore(eventUtcEndDate)) {

                    if (eventUtcStartDate.isAfter(check)) {
                        temporarily.put(day, hourStatus);
                        instant = eventUtcStartDate.toInstant();
                        day = instant.toEpochMilli();
                        hourStatus = new TreeMap<>();
                        check = eventUtcStartDate.toLocalDate().atStartOfDay().atZone(eventUtcStartDate.getZone())
                                .plusDays(1).minusSeconds(1);
                    }
                    ZonedDateTime utcDateTime = eventUtcStartDate.withMinute(0)
                            .withZoneSameInstant(java.time.ZoneOffset.UTC);

                    Long timestamp = utcDateTime.toEpochSecond() * 1000;

                    hourStatus.put(timestamp, Status.BOOKED);

                    if (eventUtcStartDate.withMinute(0).equals(Objects.requireNonNull(eventUtcEndDate).withMinute(0)))
                        break;

                    eventUtcStartDate = eventUtcStartDate.withMinute(0).plusHours(1);
                }

                temporarily.put(day, hourStatus);

                for (Map.Entry<Long, TreeMap<Long, Status>> entry : temporarily.entrySet()) {
                    Long outerKey = entry.getKey();
                    TreeMap<Long, Status> innerMap = entry.getValue();

                    if (dateClockStatus.containsKey(outerKey)) {
                        TreeMap<Long, Status> existingValuesTime = dateClockStatus.get(outerKey);
                        existingValuesTime.putAll(innerMap);
                    } else {
                        dateClockStatus.put(outerKey, innerMap);
                    }

                }
            }
        }


        for (ZonedDateTime dateOne = startOfMonth.with(LocalTime.MIDNIGHT); !dateOne.isAfter(endOfMonth);
             dateOne = dateOne.plusDays(1)) {

            Long dateToCheck = dateOne.toInstant().toEpochMilli();
            ZonedDateTime utcDateTime = Instant.ofEpochMilli(dateToCheck).atZone(ZoneId.of("UTC"));

            hourStatus = getTutorSchedule(utcDateTime, schedule);
            if (dateClockStatus.containsKey(dateToCheck)) {
                TreeMap<Long, Status> existingValuesTime = dateClockStatus.get(dateToCheck);
                existingValuesTime.putAll(hourStatus);
                dateClockStatus.put(dateToCheck, existingValuesTime);
            } else
                dateClockStatus.put(dateToCheck, hourStatus);

            if (dateClockStatus.containsKey(dateToCheck)) {

                TreeMap<Long, Status> existingValuesTime = dateClockStatus.get(dateToCheck);

                for (int i = 0; i < 24; i++) {

                    ZonedDateTime currentHour = dateOne.withHour(i);
                    Instant instant = currentHour.toInstant();
                    Long timestamp = instant.toEpochMilli();

                    if (!existingValuesTime.containsKey(timestamp)) {
                        existingValuesTime.put(timestamp, Status.AVAILABLE);
                    }
                }
            } else {
                hourStatus = new TreeMap<>();
                for (int i = 0; i < 24; i++) {
                    ZonedDateTime currentHour = dateOne.withHour(i);
                    Instant instant = currentHour.toInstant();
                    Long timestamp = instant.toEpochMilli();
                    hourStatus.put(timestamp, Status.AVAILABLE);
                }
                dateClockStatus.put(dateToCheck, hourStatus);
            }
        }
        return getAllHoursAndTheirStatusForAllDaysOfTheMonth(dateClockStatus);
    }


    private TreeMap<Long, Status> getTutorSchedule(ZonedDateTime utcDateTime, Map<DayOfWeek,
            List<TimeSlot>> schedule) {

        TreeMap<Long, Status> hourStatus = new TreeMap<>();
        DayOfWeek dayOfWeek = utcDateTime.getDayOfWeek();

        List<TimeSlot> daySchedule;
        daySchedule = schedule.get(dayOfWeek);

        for (int i = 0; i < 24; i++) {
            getOccupiedHours(daySchedule, i, utcDateTime, hourStatus);
        }
        return hourStatus;
    }


    private void getOccupiedHours(List<TimeSlot> daySchedule, int i, ZonedDateTime utcDateTime,
                                  TreeMap<Long, Status> hourStatus) {

        TimeSlot timeSlot = daySchedule.get(i);

        if (timeSlot.isEngaged()) {
            LocalTime localTime = LocalTime.parse(timeSlot.getTime());
            long clock = localTime.atDate(LocalDate.from(utcDateTime))
                    .atZone(ZoneOffset.UTC)
                    .toInstant()
                    .toEpochMilli();

            hourStatus.put(clock, Status.UNAVAILABLE);
        }
    }


    private List<FreeAndBusyHoursOfTheDay> getAllHoursAndTheirStatusForAllDaysOfTheMonth(TreeMap<Long, TreeMap<Long,
            Status>> dateClockStatus) {

        List<FreeAndBusyHoursOfTheDay> eventsOfMonth = new ArrayList<>();

        FreeAndBusyHoursOfTheDay eventsOfDay;

        List<TimeStatus> informationAboutAllHoursOfTheDay;

        TimeStatus hourStatus;

        for (Map.Entry<Long, TreeMap<Long, Status>> entry1 : dateClockStatus.entrySet()) {
            Long dateKey = entry1.getKey();
            TreeMap<Long, Status> hoursAndTheirStatus = entry1.getValue();
            informationAboutAllHoursOfTheDay = new ArrayList<>();
            for (Map.Entry<Long, Status> entry2 : hoursAndTheirStatus.entrySet()) {
                Long hourKey = entry2.getKey();
                Status status = entry2.getValue();
                hourStatus = new TimeStatus();
                hourStatus.setTime(hourKey);
                hourStatus.setStatus(status);
                informationAboutAllHoursOfTheDay.add(hourStatus);
            }
            eventsOfDay = new FreeAndBusyHoursOfTheDay();
            eventsOfDay.setDate(dateKey);
            eventsOfDay.setTime(informationAboutAllHoursOfTheDay);
            eventsOfMonth.add(eventsOfDay);
        }
        return eventsOfMonth;
    }
}
