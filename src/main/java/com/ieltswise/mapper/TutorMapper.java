package com.ieltswise.mapper;

import com.ieltswise.controller.request.TutorCreateRequest;
import com.ieltswise.entity.TutorInfo;

public interface TutorMapper {
    /**
     * Maps a TutorCreateRequest to a TutorInfo entity
     *
     * @param tutorCreateRequest the request object containing information to create a tutor
     * @return the mapped TutorInfo entity
     */
    TutorInfo mapTutorCreateRequestToTutorInfo(TutorCreateRequest tutorCreateRequest);
}
