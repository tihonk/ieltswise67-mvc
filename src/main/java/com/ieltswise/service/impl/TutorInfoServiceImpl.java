package com.ieltswise.service.impl;

import com.ieltswise.controller.request.TutorCreateRequest;
import com.ieltswise.entity.TutorInfo;
import com.ieltswise.entity.schedule.Schedule;
import com.ieltswise.mapper.TutorMapper;
import com.ieltswise.repository.ScheduleRepository;
import com.ieltswise.repository.TutorInfoRepository;
import com.ieltswise.service.TutorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class TutorInfoServiceImpl implements TutorInfoService {

    private final TutorInfoRepository tutorInfoRepository;
    private final ScheduleRepository scheduleRepository;
    private final TutorMapper tutorMapper;

    @Autowired
    public TutorInfoServiceImpl(TutorInfoRepository tutorInfoRepository, ScheduleRepository scheduleRepository, TutorMapper tutorMapper) {
        this.tutorInfoRepository = tutorInfoRepository;
        this.scheduleRepository = scheduleRepository;
        this.tutorMapper = tutorMapper;
    }

    @Override
    @Transactional
    public TutorInfo createTutor(TutorCreateRequest tutorCreateRequest) {
        TutorInfo tutorInfo = tutorMapper.mapTutorCreateRequestToTutorInfo(tutorCreateRequest);
        tutorInfo.setCreated(Instant.now().toEpochMilli());
        tutorInfoRepository.save(tutorInfo);

        Schedule schedule = Schedule.builder()
                .tutor(tutorInfo)
                .timeInfo(tutorCreateRequest.getUpdatedTimeInfo())
                .build();
        scheduleRepository.save(schedule);

        return tutorInfo;
    }
}
