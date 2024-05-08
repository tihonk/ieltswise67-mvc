package com.ieltswise.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ieltswise.converter.JsonConverter;
import com.ieltswise.dto.TimeSlot;
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

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SCHEDULE")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;
    @Convert(converter = JsonConverter.class)
    @Column(name = "TIME_INFO", columnDefinition = "CLOB", nullable = false)
    private Map<DayOfWeek, List<TimeSlot>> timeInfo;
    @OneToOne
    @JoinColumn(name = "TUTOR_ID")
    @JsonBackReference
    private TutorInfo tutor;
}
