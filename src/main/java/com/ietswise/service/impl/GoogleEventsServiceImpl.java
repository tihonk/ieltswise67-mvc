package com.ietswise.service.impl;

import com.ietswise.entity.Event;
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
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;

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
        for (int i=0; i < eventItems.length(); i++) {
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
        if(dateTime.toMap().get(JSON_DATETIME) != null) {
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
}
