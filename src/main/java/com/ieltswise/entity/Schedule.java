package com.ieltswise.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ieltswise.converter.JsonConverter;
import com.ieltswise.dto.TimeSlot;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Schema(
        title = "Schedule",
        description = "Represents the tutor's schedule, including busy and free hours for each day of the week"
)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SCHEDULE")
public class Schedule {

    @Schema(
            description = "Unique identifier for the schedule",
            example = "1"
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;

    @Schema(
            description = "The tutor's schedule for the week with the specified busy and free hours of the day",
            example = "{\"MONDAY\": [{\"time\": \"09:00\", \"engaged\": false}]," +
                    " \"TUESDAY\": [{\"time\": \"10:00\", \"engaged\": true}]}"
    )
    @Convert(converter = JsonConverter.class)
    @Column(name = "TIME_INFO", columnDefinition = "CLOB", nullable = false)
    private Map<DayOfWeek, List<TimeSlot>> timeInfo;

    @Hidden
    @OneToOne
    @JoinColumn(name = "TUTOR_ID")
    @JsonBackReference
    private TutorInfo tutor;
}
