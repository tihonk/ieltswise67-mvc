package com.ieltswise.service.impl;

import com.ieltswise.entity.schedule.Schedule;
import com.ieltswise.entity.schedule.TimeSlot;
import com.ieltswise.repository.ScheduleRepository;
import com.ieltswise.repository.TutorInfoRepository;
import com.ieltswise.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final TutorInfoRepository tutorInfoRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, TutorInfoRepository tutorInfoRepository) {
        this.scheduleRepository = scheduleRepository;
        this.tutorInfoRepository = tutorInfoRepository;
    }

    @Override
    public Schedule getSchedulesTutor(String email) {
        getTutorByEmail(email);
        return getScheduleByEmail(email);
    }

    @Override
    public Schedule updateSchedule(String email, Map<DayOfWeek, List<TimeSlot>> updatedTimeInfo) {
        getTutorByEmail(email);
        Schedule existingSchedule = getScheduleByEmail(email);
        existingSchedule.setTimeInfo(updatedTimeInfo);
        return scheduleRepository.save(existingSchedule);
    }

    private void getTutorByEmail(String email) {
        tutorInfoRepository.findByEmail(email)
                .orElseThrow(() -> notFoundException("Tutor", email));
    }

    private Schedule getScheduleByEmail(String email) {
        return scheduleRepository.findScheduleByEmail(email)
                .orElseThrow(() -> notFoundException("Schedule for tutor", email));
    }

    private RuntimeException notFoundException(String entity, String email) {
        return new RuntimeException(entity + " with email " + email + " not found");
    }
}
