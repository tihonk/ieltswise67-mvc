package com.ieltswise.service;

import com.ieltswise.entity.schedule.Schedule;
import com.ieltswise.entity.schedule.TimeSlot;
import com.ieltswise.exception.TutorEmailNotFoundException;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public interface ScheduleService {

    /**
     * Retrieves the schedule for a tutor based on their email
     *
     * @param email the email of the tutor
     * @return the schedule for the tutor
     * @throws TutorEmailNotFoundException if the tutor's email is not found.
     */
    Schedule getSchedulesTutor(String email) throws TutorEmailNotFoundException;

    /**
     * Updates the schedule for a tutor with the provided time information
     *
     * @param tutorId         the email of the tutor
     * @param updatedTimeInfo the updated time information for the schedule
     * @return the updated schedule for the tutor
     * @throws TutorEmailNotFoundException if the tutor's email is not found.
     */
    Schedule updateSchedule(String tutorId, Map<DayOfWeek, List<TimeSlot>> updatedTimeInfo)
            throws TutorEmailNotFoundException;
}
