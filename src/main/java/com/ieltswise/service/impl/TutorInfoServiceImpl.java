package com.ieltswise.service.impl;

import com.ieltswise.controller.request.TutorCreateRequest;
import com.ieltswise.entity.PaymentCredentials;
import com.ieltswise.entity.Schedule;
import com.ieltswise.entity.TutorInfo;
import com.ieltswise.exception.TutorCreationException;
import com.ieltswise.mapper.TutorMapper;
import com.ieltswise.repository.PaymentCredentialsRepository;
import com.ieltswise.repository.ScheduleRepository;
import com.ieltswise.repository.TutorInfoRepository;
import com.ieltswise.service.TutorInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
public class TutorInfoServiceImpl implements TutorInfoService {

    private final PaymentCredentialsRepository paymentCredentialsRepository;
    private final TutorInfoRepository tutorInfoRepository;
    private final ScheduleRepository scheduleRepository;
    private final TutorMapper tutorMapper;

    @Autowired
    public TutorInfoServiceImpl(PaymentCredentialsRepository paymentCredentialsRepository,
                                TutorInfoRepository tutorInfoRepository,
                                ScheduleRepository scheduleRepository,
                                TutorMapper tutorMapper) {
        this.paymentCredentialsRepository = paymentCredentialsRepository;
        this.tutorInfoRepository = tutorInfoRepository;
        this.scheduleRepository = scheduleRepository;
        this.tutorMapper = tutorMapper;
    }

    @Override
    @Transactional
    public TutorInfo createTutor(TutorCreateRequest tutorCreateRequest) throws TutorCreationException {
        try {
            log.info("Creating tutor: {}", tutorCreateRequest);

            TutorInfo tutorInfo = tutorMapper.mapTutorCreateRequestToTutorInfo(tutorCreateRequest);
            tutorInfo.setCreated(Instant.now().toEpochMilli());
            tutorInfoRepository.save(tutorInfo);
            log.info("Tutor info saved: {}", tutorInfo);

            Schedule schedule = Schedule.builder()
                    .tutor(tutorInfo)
                    .timeInfo(tutorCreateRequest.getUpdatedTimeInfo())
                    .build();
            scheduleRepository.save(schedule);
            log.info("Schedule saved: {}", schedule);

            PaymentCredentials paymentCredentials = PaymentCredentials.builder()
                    .tutor(tutorInfo)
                    .clientId(tutorCreateRequest.getClientId())
                    .clientSecret(tutorCreateRequest.getClientSecret())
                    .build();
            paymentCredentialsRepository.save(paymentCredentials);
            log.info("Payment credentials saved: {}", paymentCredentials);

            log.info("Tutor created successfully: {}", tutorInfo);

            return tutorInfo;
        } catch (Exception e) {
            log.error("Error occurred while creating tutor", e);
            throw new TutorCreationException(String.format("Failed to create tutor: %s", e.getMessage()));
        }
    }
}
