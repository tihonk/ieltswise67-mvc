package com.ieltswise.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ieltswise.entity.schedule.TimeSlot;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Converter
public class JsonConverter implements AttributeConverter<Map<DayOfWeek, List<TimeSlot>>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<DayOfWeek, List<TimeSlot>> attribute) {
        try {
            log.info("Converting Map to JSON string");
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert Map to JSON string", e);
            return null;
        }
    }

    @Override
    public Map<DayOfWeek, List<TimeSlot>> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            log.warn("Database data is null or empty");
            return null;
        }
        try {
            log.info("Converting JSON string to Map");
            TypeReference<HashMap<DayOfWeek, List<TimeSlot>>> typeReference = new TypeReference<>() {};
            return objectMapper.readValue(dbData, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert JSON string to Map", e);
            return null;
        }
    }
}
