package com.ietswise.service.impl;

import com.ietswise.entity.Event;
import com.ietswise.entity.FreeAndBusyHoursOfTheDay;
import com.ietswise.service.GoogleEventsService;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static java.time.YearMonth.of;
import static java.time.ZoneId.systemDefault;
import static java.time.ZonedDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.util.Locale.ROOT;
import static org.apache.http.protocol.HTTP.USER_AGENT;


@Service
public class GoogleEventsServiceImpl implements GoogleEventsService {
    private static final String JSON_START = "start";
    private static final String JSON_END = "end";
    private static final String JSON_STATUS = "status";
    private static final String JSON_DATETIME = "dateTime";
    private static final String JSON_DATE = "date";
    private static final String STATUS_CANCELED = "cancelled";

    @Value("${google.credentials.key}")
    private String googleCredentialKey;

    @Autowired
    public GoogleEventsServiceImpl() {
    }


    @Override
    public List<Event> getEvents(String tutorID) {
        try {
            URL obj = new URL("https://www.googleapis.com/calendar/v3/calendars/" + tutorID
                    + "/events?key=" + googleCredentialKey);
            return extractEvents(createJSONObjectResponse(obj).getJSONArray("items"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            return localDate.atStartOfDay(systemDefault());
        }
        return null;
    }

    @Override
    public List<FreeAndBusyHoursOfTheDay> getEventsByYearAndMonth(String tutorId, int year, int month) {
        try {
            ZonedDateTime startOfMonth = of(year, month).atDay(1).atStartOfDay(systemDefault());
            ZonedDateTime endOfMonth = of(year, month).atEndOfMonth().atStartOfDay(systemDefault());

            URL obj = new URL(createUrl(tutorId, startOfMonth, endOfMonth));

            return findAllEventsByYearAndMonth(createJSONObjectResponse(obj).getJSONArray("items"), startOfMonth,
                    endOfMonth);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject createJSONObjectResponse(URL obj) throws IOException {
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
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

    private List<FreeAndBusyHoursOfTheDay> findAllEventsByYearAndMonth(JSONArray jsonArray, ZonedDateTime startOfMonth,
                                                                       ZonedDateTime endOfMonth) {
        TreeMap<Long, TreeMap<String, Boolean>> dateClockStatus = new TreeMap<>();
        TreeMap<String, Boolean> hourStatus;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject eventItem = jsonArray.getJSONObject(i);
            if (!eventItem.getString(JSON_STATUS).equals(STATUS_CANCELED)) {

                JSONObject start = eventItem.getJSONObject(JSON_START);
                JSONObject end = eventItem.getJSONObject(JSON_END);

                ZonedDateTime eventStartDate = extractDate(start);
                ZonedDateTime eventEndDate = extractDate(end);

                LocalTime localTimeStart = Objects.requireNonNull(eventStartDate).toLocalTime();
                LocalTime localTimeEnd = Objects.requireNonNull(eventEndDate).toLocalTime();

                hourStatus = new TreeMap<>();
                while (!localTimeStart.withMinute(0).isAfter(localTimeEnd.withMinute(0))) {
                    String formattedTime = localTimeStart.format(DateTimeFormatter.ofPattern("HH:00"));
                    hourStatus.put(formattedTime, true);
                    localTimeStart = localTimeStart.plusHours(1);
                }
                Instant instant = eventStartDate.with(LocalTime.MIDNIGHT).toInstant();
                Long day = instant.toEpochMilli();
                if (dateClockStatus.containsKey(day)) {
                    TreeMap<String, Boolean> existingValuesTime = dateClockStatus.get(day);
                    existingValuesTime.putAll(hourStatus);
                } else {
                    dateClockStatus.put(day, hourStatus);
                }
            }
        }

        for (ZonedDateTime dateOne = startOfMonth.with(LocalTime.MIDNIGHT); !dateOne.isAfter(endOfMonth);
             dateOne = dateOne.plusDays(1)) {
            Instant instant = dateOne.toInstant();
            Long dateToCheck = instant.toEpochMilli();
            if (dateClockStatus.containsKey(dateToCheck)) {
                TreeMap<String, Boolean> existingValuesTime = dateClockStatus.get(dateToCheck);
                for (int i = 0; i < 24; i++) {
                    LocalTime time = LocalTime.of(i, 0);
                    String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));
                    if (!existingValuesTime.containsKey(formattedTime)) {
                        existingValuesTime.put(formattedTime, false);
                    }
                }
            } else {
                hourStatus = new TreeMap<>();
                for (int i = 0; i < 24; i++) {
                    LocalTime time = LocalTime.of(i, 0);
                    String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));
                    hourStatus.put(formattedTime, false);
                }
                dateClockStatus.put(dateToCheck, hourStatus);
            }
        }
        return getAllHoursAndTheirStatusForAllDaysOfTheMonth(dateClockStatus);
    }

    private List<FreeAndBusyHoursOfTheDay> getAllHoursAndTheirStatusForAllDaysOfTheMonth(TreeMap<Long, TreeMap<String,
            Boolean>> dateClockStatus) {
        List<FreeAndBusyHoursOfTheDay> eventsOfMonth = new ArrayList<>();
        FreeAndBusyHoursOfTheDay eventsOfDay;
        List<Map<String, Object>> informationAboutAllHoursOfTheDay;
        Map<String, Object> hourStatus;

        for (Map.Entry<Long, TreeMap<String, Boolean>> entry1 : dateClockStatus.entrySet()) {
            Long dateKey = entry1.getKey();
            TreeMap<String, Boolean> hoursAndTheirStatus = entry1.getValue();
            informationAboutAllHoursOfTheDay = new ArrayList<>();
            for (Map.Entry<String, Boolean> entry2 : hoursAndTheirStatus.entrySet()) {
                String hourKey = entry2.getKey();
                Boolean status = entry2.getValue();
                hourStatus = new HashMap<>();
                hourStatus.put("time", hourKey);
                hourStatus.put("occupied", status);
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
