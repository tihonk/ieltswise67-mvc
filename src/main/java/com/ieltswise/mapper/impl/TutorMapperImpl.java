package com.ieltswise.mapper.impl;

import com.ieltswise.controller.request.TutorCreateRequest;
import com.ieltswise.entity.TutorInfo;
import com.ieltswise.mapper.TutorMapper;
import org.springframework.stereotype.Component;

@Component
public class TutorMapperImpl implements TutorMapper {
    @Override
    public TutorInfo mapTutorCreateRequestToTutorInfo(TutorCreateRequest tutorCreateRequest) {
        if (tutorCreateRequest == null) {
            return null;
        } else {
            TutorInfo tutorInfo = new TutorInfo();
            tutorInfo.setEmail(tutorCreateRequest.getEmail());
            tutorInfo.setName(tutorCreateRequest.getName());
            return tutorInfo;
        }
    }
}
