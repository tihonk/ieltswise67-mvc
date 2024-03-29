package util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ieltswise.entity.FreeAndBusyHoursOfTheDay;
import com.ieltswise.entity.schedule.TimeSlot;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JsonDataReader {

    protected static ObjectMapper objectMapper;

    public static List<FreeAndBusyHoursOfTheDay> loadFreeAndBusyHoursFromFile(String filePath) {
        objectMapper = new ObjectMapper();
        List<FreeAndBusyHoursOfTheDay> freeAndBusyHoursList = null;
        try {
            freeAndBusyHoursList = Arrays.asList(objectMapper.readValue(new File(filePath),
                    FreeAndBusyHoursOfTheDay[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return freeAndBusyHoursList;
    }

    public static Map<DayOfWeek, List<TimeSlot>> loadScheduleFromFile(String filePath) {
        objectMapper = new ObjectMapper();
        TypeReference<Map<DayOfWeek, List<TimeSlot>>> typeReference = new TypeReference<>() {};
        Map<DayOfWeek, List<TimeSlot>> schedules = null;
        try {
            schedules = objectMapper.readValue(new File(filePath), typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    public static JSONArray loadEvents(String filePath) {
        objectMapper = new ObjectMapper();
        JSONArray jsonArray = null;
        try {
            List<Object> list = objectMapper.readValue(new File(filePath), new TypeReference<>() {});
            jsonArray = new JSONArray(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
