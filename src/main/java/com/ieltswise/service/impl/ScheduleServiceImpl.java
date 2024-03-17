package com.ieltswise.service.impl;

import com.ieltswise.entity.schedule.Schedule;
import com.ieltswise.entity.schedule.TimeSlot;
import com.ieltswise.exception.TutorEmailNotFoundException;
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
    public Schedule getSchedulesTutor(String email) throws TutorEmailNotFoundException {
        return getScheduleByEmail(email);
    }

    @Override
    public Schedule updateSchedule(String email, Map<DayOfWeek, List<TimeSlot>> updatedTimeInfo)
            throws TutorEmailNotFoundException {
        Schedule existingSchedule = getScheduleByEmail(email);
        existingSchedule.setTimeInfo(updatedTimeInfo);
        return scheduleRepository.save(existingSchedule);
    }

    private Schedule getScheduleByEmail(String email) throws TutorEmailNotFoundException {
        return scheduleRepository.findScheduleByTutorEmail(email)
                .orElseThrow(() -> new TutorEmailNotFoundException(
                        String.format("Schedule for tutor with email %s not found", email)));
    }
}
