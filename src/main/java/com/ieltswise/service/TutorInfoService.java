package com.ieltswise.service;

import com.ieltswise.controller.request.TutorCreateRequest;
import com.ieltswise.entity.TutorInfo;
import com.ieltswise.exception.TutorCreationException;

public interface TutorInfoService {

    /**
     * Creates a new teacher based on information from the TutorCreateRequest object
     *
     * @param tutorCreateRequest an object containing information about the new teacher
     * @return the created TutorInfo object
     * @throws TutorCreationException if an error occurs during tutor creation
     */
    TutorInfo createTutor(TutorCreateRequest tutorCreateRequest) throws TutorCreationException;
}
