package com.ieltswise.service.impl;

import com.ieltswise.dto.TimeSlot;
import com.ieltswise.entity.Schedule;
import com.ieltswise.exception.EmailNotFoundException;
import com.ieltswise.repository.ScheduleRepository;
import com.ieltswise.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Schedule getSchedulesTutor(String email) throws EmailNotFoundException {
        return getScheduleByEmail(email);
    }

    @Override
    public Schedule updateSchedule(String email, Map<DayOfWeek, List<TimeSlot>> updatedTimeInfo)
            throws EmailNotFoundException {
        Schedule existingSchedule = getScheduleByEmail(email);
        existingSchedule.setTimeInfo(updatedTimeInfo);
        return scheduleRepository.save(existingSchedule);
    }

    private Schedule getScheduleByEmail(String email) throws EmailNotFoundException {
        return scheduleRepository.findScheduleByTutorEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(
                        String.format("Schedule for tutor with email %s not found", email)));
    }
}
