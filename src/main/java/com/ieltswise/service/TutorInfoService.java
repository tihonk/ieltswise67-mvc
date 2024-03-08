package com.ieltswise.service;

import com.ieltswise.controller.request.TutorCreateRequest;
import com.ieltswise.entity.TutorInfo;

import java.util.Optional;

public interface TutorInfoService {
    /**
     * Creates a new teacher based on information from the TutorCreateRequest object
     *
     * @param tutorCreateRequest an object containing information about the new teacher
     * @return the created TutorInfo object
     */
    Optional<TutorInfo> createTutor(TutorCreateRequest tutorCreateRequest);
}
