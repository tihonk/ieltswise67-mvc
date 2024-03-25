package com.ieltswise.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ieltswise.entity.schedule.TimeSlot;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Converter
public class JsonConverter implements AttributeConverter<Map<DayOfWeek, List<TimeSlot>>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<DayOfWeek, List<TimeSlot>> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            System.out.println("Failed to convert Map to JSON string: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Map<DayOfWeek, List<TimeSlot>> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            TypeReference<HashMap<DayOfWeek, List<TimeSlot>>> typeReference = new TypeReference<>() {};
            return objectMapper.readValue(dbData, typeReference);
        } catch (JsonProcessingException e) {
            System.out.println("Failed to convert JSON string to Map: " + e.getMessage());
            return null;
        }
    }
}
