package com.ietswise.service.impl;

import com.ietswise.entity.Event;
import com.ietswise.entity.EventsByYearAndMonth;
import com.ietswise.service.GoogleEventsService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

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
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject myResponse = new JSONObject(response.toString());
            return extractEvents(myResponse.getJSONArray("items"));
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

    private ZonedDateTime extractDate(JSONObject dateTime) {  // Извлечение даты и времени из JSON объекта и преобразование в ZonedDateTime
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

    //    ===============================================================================================    //
    @Override
    public List<EventsByYearAndMonth> getEventsByYearAndMonth(String tutorId, int year, int month) {
        try {
            ZonedDateTime startOfMonth = YearMonth.of(year, month).atDay(1).atStartOfDay(ZoneId.systemDefault());
            ZonedDateTime endOfMonth = YearMonth.of(year, month).atEndOfMonth().atStartOfDay(ZoneId.systemDefault());

            String formattedTimeMin = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(startOfMonth);
            String formattedTimeMax = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(endOfMonth);

            String encodedTimeMin = URLEncoder.encode(formattedTimeMin, StandardCharsets.UTF_8);
            String encodedTimeMax = URLEncoder.encode(formattedTimeMax, StandardCharsets.UTF_8);

            String apiUrl = "https://www.googleapis.com/calendar/v3/calendars/" + tutorId + "/events";
            String fullUrl = apiUrl + "?timeMin=" + encodedTimeMin + "&timeMax=" + encodedTimeMax + "&key=" + googleCredentialKey;

            URL obj = new URL(fullUrl);

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

            JSONObject myResponse = new JSONObject(response.toString());
            return findAllEventsByYearAndMonth(myResponse.getJSONArray("items"), startOfMonth, endOfMonth);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<EventsByYearAndMonth> findAllEventsByYearAndMonth(JSONArray jsonArray, ZonedDateTime startOfMonth, ZonedDateTime endOfMonth) {

        TreeMap<Long, TreeMap<String, Boolean>> datesTimeStatus = new TreeMap<>();
        TreeMap<String, Boolean> times;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject eventItem = jsonArray.getJSONObject(i);
            if (!eventItem.getString(JSON_STATUS).equals(STATUS_CANCELED)) {

                JSONObject start = eventItem.getJSONObject(JSON_START);
                JSONObject end = eventItem.getJSONObject(JSON_END);

                ZonedDateTime eventStartDate = extractDate(start);
                ZonedDateTime eventEndDate = extractDate(end);

                LocalTime localTimeStart = Objects.requireNonNull(eventStartDate).toLocalTime();
                LocalTime localTimeEnd = Objects.requireNonNull(eventEndDate).toLocalTime();

                times = new TreeMap<>();

                while (!localTimeStart.withMinute(0).isAfter(localTimeEnd.withMinute(0))) {
                    String formattedTime = localTimeStart.format(DateTimeFormatter.ofPattern("HH:00"));
                    times.put(formattedTime, true);
                    localTimeStart = localTimeStart.plusHours(1);
                }
                Instant instant = eventStartDate.with(LocalTime.MIDNIGHT).toInstant();
                Long day = instant.toEpochMilli();

                if (datesTimeStatus.containsKey(day)) {
                    TreeMap<String, Boolean> existingValuesTime = datesTimeStatus.get(day);
                    existingValuesTime.putAll(times);
                } else {
                    datesTimeStatus.put(day, times);
                }
            }
        }

        for (ZonedDateTime dateOne = startOfMonth.with(LocalTime.MIDNIGHT); !dateOne.isAfter(endOfMonth); dateOne = dateOne.plusDays(1)) {

            Instant instant = dateOne.toInstant();
            Long proverkaData = instant.toEpochMilli();

            if (datesTimeStatus.containsKey(proverkaData)) {
                TreeMap<String, Boolean> existingValuesTime = datesTimeStatus.get(proverkaData);
                for (int i = 0; i < 24; i++) {
                    LocalTime time = LocalTime.of(i, 0);
                    String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));
                    if (!existingValuesTime.containsKey(formattedTime)) {
                        existingValuesTime.put(formattedTime, false);
                    }
                }
            } else {
                times = new TreeMap<>();
                for (int i = 0; i < 24; i++) {
                    LocalTime time = LocalTime.of(i, 0);
                    String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));
                    times.put(formattedTime, false);
                }
                datesTimeStatus.put(proverkaData, times);
            }
        }

        return extractEventsByYearAndMonth(datesTimeStatus);
    }


    private List<EventsByYearAndMonth> extractEventsByYearAndMonth(TreeMap<Long, TreeMap<String, Boolean>> datesTimeStatus) {

        List<EventsByYearAndMonth> events = new ArrayList<>();
        EventsByYearAndMonth eventsByYearAndMonth;
        List<Map<String, Object>> list;
        Map<String, Object> oneHour;

        for (Map.Entry<Long, TreeMap<String, Boolean>> entry1 : datesTimeStatus.entrySet()) {
            Long key1 = entry1.getKey();
            TreeMap<String, Boolean> values1 = entry1.getValue();
            list = new ArrayList<>();
            for (Map.Entry<String, Boolean> entry2 : values1.entrySet()) {

                String key2 = entry2.getKey();
                Boolean value2 = entry2.getValue();
                oneHour = new HashMap<>();
                oneHour.put("time", key2);
                oneHour.put("occupied", value2);
                list.add(oneHour);
            }

            eventsByYearAndMonth = new EventsByYearAndMonth();
            eventsByYearAndMonth.setDate(key1);
            eventsByYearAndMonth.setTime(list);
            events.add(eventsByYearAndMonth);
        }

        return events;
    }
}
